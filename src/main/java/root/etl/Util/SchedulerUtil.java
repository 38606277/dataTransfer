package root.etl.Util;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 11:48
 * @Description: !!! 此类最为关键，此类 通过定义 job 的增删改查与trigger结合，并且持久化到数据库
 * 并且能被controller调用，这就是实现动态管理任务的关键点
 */
public class SchedulerUtil {

    private static final Logger logger = Logger.getLogger(SchedulerUtil.class);

    /**
     * 功能描述:
     * 新增定时任务
     *
     * @param: jobClassName 类路径 ； jobName 任务名称 ； jobGroupName 组别 ； cronExpression cron表达式 ； jobDataMap 需要传递的参数
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 11:51
     */
    public static void addJob(String jobClassName, String jobName, String jobGroupName, String cronExpression, Map jobDataMap) throws Exception {

        // 通过 SchedulerFactory 获取一个调度器实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 启动任务调度器
        scheduler.start();

        // 构建 job 的信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobName, jobGroupName).build();
        // JobDataMap用于传递任务运行时的参数 , 比如定时发送邮件，可以用json形式存储收件人等等信息
        if (jobDataMap != null) {
            jobDetail.getJobDataMap().putAll(jobDataMap);
        }

        // 构建 表达式调度器 （即任务执行的时间）
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 从 cron 当中构建出 cronTrigger
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(cronScheduleBuilder).startNow().build();


        // 关联JOB 跟 trigger
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败");
            throw new Exception("创建定时任务失败");
        }

        // 至于对数据库的操作不要写在这里，让service去做数据库的操作，这里只是做 scheduler相关的操作
    }

    /**
     * 功能描述:
     * 停用掉指定的job
     *
     * @param: jobName 任务名称 ； jobGroupName 任务组别
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 12:12
     */
    public static void jobPause(String jobName, String jobGroupName) throws SchedulerException {
        // 通过 SchedulerFactory 获取一个调度器实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));    // 通过制定的key停用掉制定的 job
    }

    /**
     * 功能描述:
     * 手动启用一个定时任务
     *
     * @param: jobName 任务名称 ； jobGroupName 任务组别
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 12:13
     */
    public static void jobResume(String jobName, String jobGroupName) throws SchedulerException {
        // 通过 SchedulerFactory 获取一个调度器实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 功能描述:
     * 删除一个定时任务
     *
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 12:17
     */
    public static void deleteJob(String jobName, String jobGroupName) throws SchedulerException {
        // 通过 SchedulerFactory 获取一个调度器实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 停掉 trigger
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        // 卸载关联关系
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 功能描述:
     * 修改定时任务表达式
     *
     * @param: jobName 任务名称；jobGroupName 组别名称 ； cronExpression cron表达式
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 14:14
     */
    public static void updateJobCronExpression(String jobName, String jobGroupName, String cronExpression) throws Exception {
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow().build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败");
            throw new Exception("更新定时任务失败");
        }
    }

    // 重置下 指定job的 jobDataMap  信息
    //  // 重置下 jsonDataMap
    //            scheduler.getJobDetail(JobKey.jobKey(jobName,jobGroupName)).getJobDataMap().putAll(jsonDataMap);
    public static void updateJsonDataMap(String jobName, String jobGroupName, Map jsonDataMap) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(jobName,jobGroupName);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        if(jobDataMap!=null){
            jobDataMap.putAll(jsonDataMap);
            for(Object str : jsonDataMap.keySet()) {
                jobDetail.getJobDataMap().put(str.toString(),jsonDataMap.get(str));
            }

        }
       // scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName)).getJobDataMap().putAll(jsonDataMap);
    }

    /**
     * 功能描述:
     * 检查JOB是否存在
     *
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 14:19
     */
    public static boolean jobIsExist(String jobName, String jobGroupName) throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        Boolean state = scheduler.checkExists(triggerKey);
        return state;
    }

    /**
     * 功能描述:
     * 暂停所有的定时任务
     *
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 14:21
     */
    public static void pauseAllJob() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.pauseAll();
    }

    /**
     * 功能描述:
     * 唤醒所有任务
     *
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/11/27 14:26
     */
    public static void resumeAllJob() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.resumeAll();
    }

    /**
     * 获取Job实例
     *
     * @param classname
     * @return
     * @throws Exception
     */
    public static BaseJob getClass(String classname) throws Exception {
        try {
            Class<?> c = Class.forName(classname);
            return (BaseJob) c.newInstance();
        } catch (Exception e) {
            throw new Exception("类[" + classname + "]不存在！");
        }

    }
}