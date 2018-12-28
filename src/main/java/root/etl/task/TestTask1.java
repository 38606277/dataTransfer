package root.etl.task;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import root.etl.Util.BaseJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 10:45
 * @Description: 测试第一个任务的编写
 */
public class TestTask1 implements BaseJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // jobExecutionContext.getScheduler().getJobDetail("","").getJobDataMap().put("","");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        System.out.println(Thread.currentThread().getName() + " " + sdf.format(date) + this.getClass().getName()+
                ":"+jobKey.getName()+":"+jobKey.getGroup()+" Task： ----爱上学习,只为更多的价值----");
    }
}
