package root.job.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import root.job.Util.BaseJob;
import root.job.Util.Constant;
import root.job.service.JobService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 18:06
 * @Description: 这个类用于启动springboot 时，加载作业，run方法自动执行。
 * 另外可以使用 ApplicationRunner
 * ==> 我们在这里做有2个目的 ： 1. 加载要执行的定时任务 2. 做一个切点  打通spring容器跟quartz容器
 */
@Component
public class InitStartSchedule implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(InitStartSchedule.class);

    @Autowired
    private MyJobFactory myJobFactory;

    @Autowired
    JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("系统启动完成====================");

        //查询job状态为启用的
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("job_status", Constant.JOB_STATE.YES);
        // 1. 查询数据库是否有要执行的任务
       List<Map> mapList = jobService.getJobListByParam(paramMap);
        if (null == mapList || mapList.size() == 0) {
            logger.info("系统启动，没有需要执行的任务... ...");
        }

        // 对 scheduler  注入 myJobFactory ，让我们的job能调度 service 方法
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        // 如果不设置JobFactory，Service注入到Job会报空指针
        scheduler.setJobFactory(myJobFactory);
        // 启动调度器
        scheduler.start();

        int i = 0;
        int j = 0;
        // 启动状态为1的任务
        for (Map resultMap : mapList) {
            String job_name = resultMap.get("job_name").toString();
            String job_group = resultMap.get("job_group").toString();
            String job_param_json = resultMap.get("job_param").toString();
            String job_cron = resultMap.get("job_cron").toString();
            //构建job信息
            JSONObject jobParamJosn = JSON.parseObject(job_param_json);
            JobDetail jobDetail;
            if(jobParamJosn !=null && Constant.TASK_CLASS.TRANSFER_VALUE.equals(jobParamJosn.getString("task_path"))){
                jobDetail = JobBuilder.newJob(getClass(Constant.TASK_CLASS.TRANSFER_TASK_CLASS_PATH).getClass()).
                        withIdentity(job_name, job_group).build();
                i++;
            }  else {
                jobDetail = JobBuilder.newJob(getClass(Constant.TASK_CLASS.DEFAULT_TASK_CLASS_PATH).getClass()).
                        withIdentity(job_name, job_group).build();
                j++;
            }  //jobParamJosn.getString("task_path"))

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job_cron);
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job_name, job_group)
                    .withSchedule(scheduleBuilder).startNow().build();
            // 任务不存在的时候才添加
            if (!scheduler.checkExists(jobDetail.getKey())) {
                try {
                    scheduler.scheduleJob(jobDetail, trigger);
                } catch (SchedulerException e) {
                    logger.info("\n创建定时任务失败" + e);
                    throw new Exception("创建定时任务失败");
                }
            }
        }

        logger.info("启动了"+i+"个移动转换任务!==============");
        logger.info("启动了"+j+"个通用转换任务!==============");
    }

    public static BaseJob getClass(String classname) throws Exception {
        Class<?> c = Class.forName(classname);
        return (BaseJob) c.newInstance();
    }

    /*


    @Override
    public void run(String... args) throws Exception {



        // 2. 对 scheduler  注入 myJobFactory ，让我们的job能调度 service 方法
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        // 如果不设置JobFactory，Service注入到Job会报空指针
        scheduler.setJobFactory(myJobFactory);
        // 启动调度器
        scheduler.start();

        for (EtlJob sysJob : jobList) {
            String jobClassName = sysJob.getJobName();
            String jobGroupName = sysJob.getJobGroup();
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(sysJob.getJobClassPath()).getClass()).withIdentity(jobClassName, jobGroupName).build();
            if (StringUtils.isNotEmpty(sysJob.getJobDataMap())) {
                JSONObject jb = JSONObject.parseObject(sysJob.getJobDataMap());
                Map<String, Object> itemMap = JSONObject.toJavaObject(jb, Map.class);
                // 把  map 中所有的元素加载到当前的 jobDetail 当中
                for (Map.Entry<String, Object> entry : itemMap.entrySet()) {   // 普通的方式遍历map
                    jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
                }
            }
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getJobCron());
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                    .withSchedule(scheduleBuilder).startNow().build();
            // 任务不存在的时候才添加
            if (!scheduler.checkExists(jobDetail.getKey())) {
                try {
                    scheduler.scheduleJob(jobDetail, trigger);
                } catch (SchedulerException e) {
                    logger.info("\n创建定时任务失败" + e);
                    throw new Exception("创建定时任务失败");
                }
            }
        }

    }


    public static BaseJob getClass(String classname) throws Exception {
        Class<?> c = Class.forName(classname);
        return (BaseJob) c.newInstance();
    }*/
}
