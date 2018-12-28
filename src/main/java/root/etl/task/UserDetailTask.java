package root.etl.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.etl.Util.BaseJob;
import root.etl.entity.EtlJob;
import root.transfer.main.TransferWithMultiThread;
import root.transfer.pojo.Root;
import root.transfer.pojo.TransferInfo;
import root.transfer.util.XmlUtil;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: pccw
 * @Date: 2018/12/14 14:05
 * @Description:
 *      用户明细表 -> 导入中间表
 *      cron : 每天 晚上定时全量跟新一次
 */
public class UserDetailTask implements BaseJob {

    private static final Logger logger = Logger.getLogger(UserDetailTask.class);

    private  final String contentPath = "/transfer_budget.sql";    // 导库源sql存放处

    private  final String contentXML = "/transfer_budget.xml";     // 导库源xml存放处

    @Autowired
    IEtlJobService etlJobService;

    @Autowired
    IEtlJobExecuteService etlJobExecuteService;   // 得到此对象传递到后面方法当中去

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();   // 得到 dataMap 以便进行 动态找到对应的sql
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey jobKey = jobDetail.getKey();

        // 查询 jobKey.getName()+jobKey.getGroup()  根据 组名跟 名称唯一确定 job 得到JOBID
        EtlJob etlJob = etlJobService.getEtlJobByNameAndGroup(jobKey.getName(),jobKey.getGroup());
        // *********   preStep 1:  job 存在性校验
        if(etlJob==null){
            logger.error("数据库查询不到job信息,无法继续执行");
            return;
        }

        logger.info("开始抽取数据......");
        // 得到当前的 Job的 name 跟 groupName ，关联查询正在执行的脚本， 组装成 root 节点
        String content = null;
        Root root = null;
        String year = null;
        // ******** step 1: 得到指定路径下的 文件当中的内容
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                            new InputStreamReader(UserDetailTask.class.getResourceAsStream(contentPath),"UTF-8"));
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

        // ********* step 2: 替换此 内容当中的 占位符为 当前 year 或者是 默认的 year;
        if(StringUtils.isBlank(content)){
            logger.error("读取到的sql内容为空,无法继续执行...");
            return;
        }
        year = dataMap.getString("year");
        if(StringUtils.isBlank(year)) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            year = sdf.format(date);    // 默认为当前年份
        }
        logger.debug("未转换之前的SQL为:"+content);
        content = content.replace("${budget_year}",year);   // 替换掉占位符
        logger.debug("sql为："+content);

        // **********  step 3: 组装好 root6 对象，把 content 塞入到 root.srcInfo.sql 当中去
        try {
            root = XmlUtil.pareseXmlToJavaBean(UserDetailTask.class.getResourceAsStream(contentXML));
            if(root!=null){
                root.getTransferInfo().get(0).getSrcInfo().setSql(content);   // 把第一个srcInfo的content设置为sql
            }else {
                logger.error("解析"+contentXML+"内容异常,无法继续...");
                return;
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        // *********   step 4 : 多线程导库
        List<TransferInfo> pojos = root.getTransferInfo();   // 得到所有要转换的节点信息
        TransferWithMultiThread transferWithMultiThread = new TransferWithMultiThread();
        try {
            // 执行最先需要执行的sql  ： 删除掉临时表的数据
            transferWithMultiThread.executePreInfo(root.getPreInfo());

            for (int i = 0; i < pojos.size(); i++) {
                transferWithMultiThread.transfer(pojos.get(i), etlJob.getId(), this.etlJobExecuteService,Integer.parseInt(year),false);    // 对每一个 对象进行导库

                // 执行最后需要的回调sql  ： 把临时表的数据导入到本地真正的存放数据库的地方
                transferWithMultiThread.executeCallBack(root.getCallBackInfo(), etlJob.getId(), this.etlJobExecuteService,year);
            }
        }catch (Exception e) {
            logger.error("执行回调sql出错：", e);
        }

    }
}
