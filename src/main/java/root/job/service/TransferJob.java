package root.job.service;



import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;


public class TransferJob implements org.quartz.Job {

    @Autowired
    JobService jobService;

    public TransferJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {

        //拿到transfer_id
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String transfer_id = dataMap.getString("transfer_id");

        //到数据库中拿到脚本
        //jobService.getJobByID()


        //执行ETL


    }

}
