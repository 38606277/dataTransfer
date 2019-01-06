package root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import root.job.service.JobService;
import root.job.service.QuartzService;


@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    JobService jobService;

    @Override
    public void run(ApplicationArguments var1) throws Exception {

        //系统启动时，启动Quartz
        jobService.ExecuteAllJob();
        System.out.println("启动成功!");
    }

}