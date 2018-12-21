package root.etl.config;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.DelegatingJob;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 16:50
 * @Description:
 */
public class AdaptableJobFactory implements JobFactory {

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        return newJob(bundle);
    }

    // 我们自己再写个 job
    public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
        try {
            Object jobObject = createJobInstance(bundle);
            return adaptJob(jobObject);
        } catch (Exception ex) {
            throw new SchedulerException("Job instantiation failed", ex);
        }
    }

    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Method getJobDetail = bundle.getClass().getMethod("getJobDetail");
        Object jobDetail = ReflectionUtils.invokeMethod(getJobDetail, bundle);
        Method getJobClass = jobDetail.getClass().getMethod("getJobClass");
        Class jobClass = (Class) ReflectionUtils.invokeMethod(getJobClass, jobDetail);
        return jobClass.newInstance();
    }

    protected Job adaptJob(Object jobObject) throws Exception {
        if (jobObject instanceof Job) {
            return (Job) jobObject;
        } else if (jobObject instanceof Runnable) {
            // 需要引入 context-support 的jar 包才行
            return new DelegatingJob((Runnable) jobObject);
        } else {
            throw new IllegalArgumentException("Unable to execute job class [" + jobObject.getClass().getName() +
                    "]: only [org.quartz.Job] and [java.lang.Runnable] supported.");
        }
    }
}
