package root.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.etl.Util.BaseJob;
import root.etl.Util.Constant;
import root.etl.entity.EtlJob;
import root.job.GlodbalVar;
import root.job.service.JobExecuteService;
import root.job.service.JobService;
import root.job.service.TransferService;
import root.transfer.main.TransferWithMultiThread;
import root.transfer.main.TransferWithMultiThreadForMemory;
import root.transfer.pojo.Item;
import root.transfer.pojo.PreItem;
import root.transfer.pojo.Root;
import root.transfer.pojo.TransferInfo;
import root.transfer.util.XmlUtil;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: pccw
 * @Date: 2018/11/29 15:56
 * @Description:
 *      导库的定时任务代码
 *      基于 etl_transfer 里面的xml 脚本 （通用性版本）
 */
public class DefaultTask implements BaseJob {

    private static final Logger logger = Logger.getLogger(DefaultTask.class);

    /*
    编写逻辑 ：
    1. 从 jobExecutionContext 当中得到 job_name  job_group ,
        查询出 etl_job 信息
    2. 通过 etl_job 当中的 transfer_id 关联查询出 transfer 信息，最重要的是 得到 transfer_content 内容
    3. 利用 XML 跟 Bean 的转换API 把 transfer_content当中的内容转换到实体  root 上
    4. 对 root 进行 解析完成导库工作 ，且在一开始导库的时候 写入信息到 etl_transfer 上去
     */
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

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();   // 得到 dataMap 以便进行 动态找到对应的sql
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

        // TODO: ====================== 带测试  -》 可以抽取出去放到 另一个方法里面。测试可以在 TEST类当中测试
        // 3.2 GLOBAL  VAR   全局系统变量转换  （对root对象进行全局变量替换）
        // 先要得到 当前 JOB类当中的 param 当中的参数
        if(null!=resultJobMap.get("job_param") &&  !resultJobMap.get("job_param").equals("")) {
            String jobParamStr = resultJobMap.get("job_param").toString();
            if (StringUtils.isNotBlank(jobParamStr)) {
                JSONObject jobParamJosn = JSON.parseObject(jobParamStr);  // 反序列成JSON
                String paramStr = jobParamJosn.getString("global_param");  // 里面用逗号进行分割
                if (StringUtils.isNotBlank(paramStr)) {
                    // =======转换 global 全局变量
                    String[] str = paramStr.split(",");
                    Map<String, Object> mapGlobal = this.glodbalVar.findTransferVars(Arrays.asList(str));
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

        // 5. 开始转换工作
        List<TransferInfo> pojos = root.getTransferInfo();   // 得到所有要转换的节点信息
        TransferWithMultiThreadForMemory transferWithMultiThread = new TransferWithMultiThreadForMemory();
        try {
            logger.info("开始抽取数据......");
            // 执行最先需要执行的sql
            transferWithMultiThread.executePreInfo(root.getPreInfo());
            for (int i = 0; i < pojos.size(); i++) {
                transferWithMultiThread.transfer(pojos.get(i),
                        Integer.parseInt(jobExecuteMap.get("id").toString()), this.jobExecuteService,
                        0,0,false);    // 对每一个 对象进行导库
                // 执行最后需要的回调sql
                transferWithMultiThread.executeCallBack(root.getCallBackInfo(),this.jobExecuteService,null,null,
                        Integer.parseInt(jobExecuteMap.get("id").toString()));
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

}
