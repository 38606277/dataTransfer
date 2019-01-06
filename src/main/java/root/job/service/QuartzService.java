package root.job.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzService {

    private static Scheduler scheduler;

    public  static Scheduler GetScheduler()  {
        if(scheduler==null)
        {
            Scheduler scheduler = null;
            try {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        }
        return scheduler;
    }

}
