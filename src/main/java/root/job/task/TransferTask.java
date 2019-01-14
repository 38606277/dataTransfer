package root.job.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import root.job.Util.BaseJob;
import root.job.Util.Constant;
import root.job.service.JobExecuteService;
import root.job.service.JobService;
import root.transfer.main.TransferWithMultiThreadForMemory;
import root.transfer.pojo.Root;
import root.transfer.pojo.TransferInfo;
import root.transfer.util.XmlUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2019-1-8
 * @Description:
 *      完成历史数据的导入 : 指定从10年的1月份开始导入到 18年12月份
 */
public class TransferTask implements BaseJob {

    private static final Logger logger = Logger.getLogger(TransferTask.class);

    private  final String contentPath = "/transfer_budget.sql";    // 导库源sql存放处

    private  final String contentXML = "/transfer_budget.xml";     // 导库源xml存放处

    private final int[] yearArray = {2010,2011,2012,2013,2014,2015,2016,2017,2018};

    private final int[] monthArray = {1,2,3,4,5,6,7,8,9,10,11,12};

    @Autowired
    JobService jobService;

    @Autowired
    JobExecuteService jobExecuteService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();   // 得到 dataMap 以便进行 动态找到对应的sql
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey jobKey = jobDetail.getKey();

        // 查询 jobKey.getName()+jobKey.getGroup()  根据 组名跟 名称唯一确定 job 得到JOBID
        Map map = new HashMap();
        map.put("job_name",jobKey.getName());
        map.put("job_group",jobKey.getGroup());
        Map resultMap = jobService.getJobByParam(map);
        // ==============  步骤1 ：  job 存在性校验
        if(resultMap==null){
            logger.error("数据库查询不到job信息,无法继续执行");
            return;
        }

        logger.info("开始抽取数据......");
        // 得到当前的 Job的 name 跟 groupName ，关联查询正在执行的脚本， 组装成 root 节点
        String content = null;
        Root root = null;

        // ================= 步骤2 ： 得到指定路径下的 文件当中的内容
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                            new InputStreamReader(TransferTask.class.getResourceAsStream(contentPath),"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
                sb.append("\r\n");   // *** 一定要加上 \r\n 保留原格式 ，否则拼接成一条语句SQL执行会出错
            }
            reader.close();
            content = sb.toString();
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ============ 步骤3 ： xml 内容存在性校验
        if(StringUtils.isBlank(content)){
            logger.error("读取到的sql内容为空,无法继续执行...");
            return;
        }

        // 3.2 先往 job_execute 当中插入记录
        int job_id = Integer.parseInt(resultMap.get("id").toString());
        Map jobExecuteMap = new HashMap();
        jobExecuteMap.put("job_id",job_id);
        jobExecuteMap.put("job_status",Constant.JOB_STATE.STARTING);   // 导入中
        jobExecuteMap.put("begin_time",new Date());
        jobExecuteMap.put("job_process",new BigDecimal("0.00"));
        this.jobExecuteService.addJobExecute(jobExecuteMap);

        int job_execute_id = Integer.parseInt(jobExecuteMap.get("id").toString());

        try {

            // ============== 步骤4 ： for 循环执行脚本
            for(int i=0; i<this.yearArray.length; i++){
                String transfer_sql_year = content;
                int transferYear = yearArray[i];
                logger.info("开始同步"+transferYear+"年份的数据.......");
                transfer_sql_year = transfer_sql_year.replace("${budget_year}",String.valueOf(transferYear));   // 替换掉占位符
                for(int j=0; j<this.monthArray.length; j++){
                    String transfer_sql_temp = transfer_sql_year;
                    int transferMonth = monthArray[j];
                    // ===》 导库
                    // ============= 步骤5.1
                    logger.info("开始同步"+transferYear+"年份"+transferMonth+"月份的数据.......");
                    transfer_sql_temp = transfer_sql_temp.replace("${budget_month}",String.valueOf(transferMonth));
                   //  logger.info("当前转换后的SQL为:\n"+content);
                    // ============= 步骤5.2 : 组装好 root 对象，把 content 塞入到 root.srcInfo.sql 当中去
                    root = XmlUtil.pareseXmlToJavaBean(TransferTask.class.getResourceAsStream(contentXML));
                    if(root!=null){
                        root.getTransferInfo().get(0).getSrcInfo().setSql(transfer_sql_temp);
                    }else {
                        logger.error("解析"+contentXML+"内容异常,无法继续...");
                        return;
                    }
                    // ============ 步骤5.3 多线程导库
                    List<TransferInfo> pojos = root.getTransferInfo();   // 得到所有要转换的节点信息
                    TransferWithMultiThreadForMemory transferWithMultiThread = new TransferWithMultiThreadForMemory();
                    // 执行最先需要执行的sql  ： 删除掉临时表的数据
                    transferWithMultiThread.executePreInfo(root.getPreInfo());

                    for (int dataCount = 0; dataCount < pojos.size(); dataCount++) {
                        transferWithMultiThread.transfer(pojos.get(dataCount),
                                job_execute_id,
                                this.jobExecuteService,transferYear,transferMonth,false);

                        // 执行最后需要的回调sql  ： 把临时表的数据导入到本地真正的存放数据库的地方
                        transferWithMultiThread.executeCallBack(root.getCallBackInfo(),
                                this.jobExecuteService,String.valueOf(transferYear),String.valueOf(transferMonth),
                                job_execute_id
                                );
                    }
                }
                logger.info("同步"+transferYear+"年份的数据结束");
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
