package root.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import root.job.GlodbalVar;
import root.job.Util.BaseJob;
import root.job.Util.Constant;
import root.job.service.JobExecuteService;
import root.job.service.JobService;
import root.job.service.TransferService;
import root.transfer.main.TransferWithMultiThreadForMemory;
import root.transfer.pojo.Item;
import root.transfer.pojo.PreItem;
import root.transfer.pojo.Root;
import root.transfer.pojo.TransferInfo;
import root.transfer.util.DbHelper;
import root.transfer.util.XmlUtil;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: pccw
 * @Date: 2018/11/29 15:56
 * @Description:
 *      导库的定时任务代码
 *      基于 etl_transfer 里面的xml 脚本 （通用性 移动业务 版本）
 */
public class TransferTaskByEtlXml implements BaseJob {

    private static final Logger logger = Logger.getLogger(TransferTaskByEtlXml.class);

    @Autowired
    TransferService transferService;

    @Autowired
    JobService jobService;

    @Autowired
    JobExecuteService jobExecuteService;

    @Autowired
    GlodbalVar glodbalVar;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();   // 得到 dataMap 以便进行 动态找到对应的sql
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey jobKey = jobDetail.getKey();

        // 1. 从 jobExecutionContext 当中得到 job_name  job_group ,查询出 etl_job 信息
        String jobName = jobKey.getName();
        String jobGroup = jobKey.getGroup();
        Map paramJobMap = new HashMap();
        paramJobMap.put("job_name",jobName);
        paramJobMap.put("job_group",jobGroup);
        Map resultJobMap = this.jobService.getJobByParam(paramJobMap);
        if(resultJobMap==null){
            logger.error("数据库查询不到job信息,无法继续执行");
           return;
        }

        String content = null;
        Root root = null;
        try {
            // 2. 通过 etl_job 当中的 transfer_id 关联查询出 transfer 信息，最重要的是 得到 transfer_content 内容
            Map transferMap = transferService.getTransferById(Integer.parseInt(resultJobMap.get("transfer_id").toString()));
            content = transferMap.get("transfer_content").toString();
            if (StringUtils.isBlank(content)) {
                throw new Exception("脚本内容为空,无法执行");
            }
            // 3. 利用 XML 跟 Bean 的转换API 把 transfer_content当中的内容转换到实体  root 上
            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(content.getBytes());
            root = XmlUtil.pareseXmlToJavaBean(tInputStringStream);
        } catch (JobExecutionException e) {
            logger.error("解析transfer的内容出错,内容为:" + content);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        if (root == null) return;
        String department_sql = null;
        // 3.2 GLOBAL  VAR   全局系统变量转换  （对root对象进行全局变量替换）
        // 先要得到 当前 JOB类当中的 param 当中的参数
        if(null != resultJobMap.get("job_param") &&  !resultJobMap.get("job_param").equals("")) {
            String jobParamStr = resultJobMap.get("job_param").toString();
            if (StringUtils.isNotBlank(jobParamStr)) {
                JSONObject jobParamJosn = JSON.parseObject(jobParamStr);  // 反序列成JSON
                String paramStr = jobParamJosn.getString("global_param");  // 里面用逗号进行分割
                if (StringUtils.isNotBlank(paramStr)) {
                    // =======转换 global 全局变量
                    String[] str = paramStr.split(",");
                    Map<String, Object> mapGlobal = this.glodbalVar.findTransferVars(Arrays.asList(str));

                    // 转换 department的  变量
                    department_sql = "select DISTINCT a.DEPARTMENT_ID from TBMPRD.TBM_BUDGET_PROJECT_SUMMARY a " +
                            "where a.BUDGET_YEAR=${budget_year}";
                    department_sql = this.glodbalVar.replaceGlobalVar(mapGlobal,department_sql);

                    // 转换 preItem
                    for (PreItem preItem : root.getPreInfo().getItem()) {
                        List<String> preSQLList = new ArrayList<>();
                        preItem.getSql().forEach(
                                e -> preSQLList.add(this.glodbalVar.replaceGlobalVar(mapGlobal, e))
                        );
                        preItem.setSql(preSQLList);
                    }
                    // 转换 transfer
                    for (TransferInfo transferInfo : root.getTransferInfo()) {
                        transferInfo.getSrcInfo().setSql(this.glodbalVar.replaceGlobalVar(mapGlobal, transferInfo.getSrcInfo().getSql()));
                        List<String> targetSQLList = new ArrayList<>();
                        transferInfo.getTargetInfo().getSql().forEach(
                                e -> targetSQLList.add(this.glodbalVar.replaceGlobalVar(mapGlobal, e))
                        );
                        transferInfo.getTargetInfo().setSql(targetSQLList);
                    }
                    // 转换 callback
                    for (Item item : root.getCallBackInfo().getItem()) {
                        List<String> preSQLList = new ArrayList<>(item.getSql().size());
                        item.getSql().forEach(
                                e -> preSQLList.add(this.glodbalVar.replaceGlobalVar(mapGlobal, e))
                        );
                        item.setSql(preSQLList);
                    }
                } else {
                    logger.info("此任务当中的SQL无需转换变量");
                }
            } else {
                logger.info("此任务当中的SQL无需转换变量");
            }
        } else {
            logger.info("此任务当中的SQL无需转换变量");
        }

        // 4. 先往 job_execute 当中插入记录
        int job_id = Integer.parseInt(resultJobMap.get("id").toString());
        Map jobExecuteMap = new HashMap();
        jobExecuteMap.put("job_id",job_id);
        jobExecuteMap.put("job_status",Constant.JOB_STATE.STARTING);   // 导入中
        jobExecuteMap.put("begin_time",new Date());
        jobExecuteMap.put("job_process",new BigDecimal("0.00"));
        this.jobExecuteService.addJobExecute(jobExecuteMap);

        // 5-pre  TODO ： 查询出 当前年份的 部门明细表的数据   for 循环导库
        // 4.1 先查询 当前指定年份的 部门情况
        List<String> departIdList = (List<String>)DbHelper.executeQuery("budget",department_sql);
        logger.info("当前全局变量配置的年度一共有 "+departIdList.size()+" 个部门");
        TransferWithMultiThreadForMemory  transferWithMultiThread = new TransferWithMultiThreadForMemory();
        // 执行最先需要执行的sql
        transferWithMultiThread.executePreInfo(root.getPreInfo());
        Root tempRoot = null;
        for(int j=0; j< departIdList.size(); j++){
            tempRoot = root;
            String department_id = departIdList.get(j);
            logger.info("正在同步"+department_id+"部门的数据");
            // 转换 preItem
            for (PreItem preItem : tempRoot.getPreInfo().getItem()) {
                List<String> preSQLList = new ArrayList<>();
                preItem.getSql().forEach(
                        e -> preSQLList.add(e.replace("${department_id}",department_id)));
                preItem.setSql(preSQLList);
            }
            // 转换 transfer
            for (TransferInfo transferInfo : tempRoot.getTransferInfo()) {
                transferInfo.getSrcInfo().setSql(transferInfo.getSrcInfo().getSql().replace("${department_id}",department_id));
                List<String> targetSQLList = new ArrayList<>();
                transferInfo.getTargetInfo().getSql().forEach(
                        e -> targetSQLList.add(e.replace("${department_id}",department_id))
                );
                transferInfo.getTargetInfo().setSql(targetSQLList);
            }
            // 转换 callback
            for (Item item : tempRoot.getCallBackInfo().getItem()) {
                List<String> preSQLList = new ArrayList<>(item.getSql().size());
                item.getSql().forEach(
                        e -> preSQLList.add(e.replace("${department_id}",department_id))
                );
                item.setSql(preSQLList);
            }

            // 5. 开始转换工作
            List<TransferInfo> pojos = tempRoot.getTransferInfo();   // 得到所有要转换的节点信息


            try {
                logger.info("开始抽取数据......");
                for (int i = 0; i < pojos.size(); i++) {
                    transferWithMultiThread.transfer(pojos.get(i),
                            Integer.parseInt(jobExecuteMap.get("id").toString()), this.jobExecuteService,
                            0,0,pojos.get(i).isCreatetable());    // 对每一个 对象进行导库
                }

            }catch (Exception e) {
                logger.error("执行回调sql出错：", e);
                e.printStackTrace();
                jobExecuteMap.put("job_process",new BigDecimal("100.00"));
                jobExecuteMap.put("job_status",Constant.JOB_STATE.NO);   // 失败了
                jobExecuteMap.put("job_failure_reason",e.getMessage().substring(0,499));  // 截取500 个字符
                this.jobExecuteService.updateEtlJobExecute(jobExecuteMap);
            }
        }
        try{
        // 执行最后需要的回调sql
            transferWithMultiThread.executeCallBack(tempRoot.getCallBackInfo(),this.jobExecuteService,null,null,
                    Integer.parseInt(jobExecuteMap.get("id").toString()));
        }catch (Exception e){
            logger.error("执行回调SQL 异常!");
            e.printStackTrace();
        }


    }

}
