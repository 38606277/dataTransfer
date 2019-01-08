package root.etl.config;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import root.etl.Service.IEtlJobService;
import root.etl.Util.BaseJob;
import root.etl.entity.EtlJob;
import root.job.service.JobExecuteService;
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

    @Autowired
    private MyJobFactory myJobFactory;

    @Override
    public void run(String... args) throws Exception {
        logger.info("系统启动完成====================");
        // 对 scheduler  注入 myJobFactory ，让我们的job能调度 service 方法
        // 通过SchedulerFactory获取一个调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        // 如果不设置JobFactory，Service注入到Job会报空指针
        scheduler.setJobFactory(myJobFactory);
        // 启动调度器
        scheduler.start();
    }

    private static Logger logger = Logger.getLogger(InitStartSchedule.class);


    public static BaseJob getClass(String classname) throws Exception {
        Class<?> c = Class.forName(classname);
        return (BaseJob) c.newInstance();
    }

    /*
    @Autowired
    private IEtlJobService etlJobService;

    @Autowired
    private MyJobFactory myJobFactory;

    @Override
    public void run(String... args) throws Exception {

        *//**
         * 用于程序启动时加载定时任务，并执行已启动的定时任务(只会执行一次，在程序启动完执行)
         *//*
        //查询job状态为启用的
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("jobStatus", "1");

        // 1. 查询数据库是否有要执行的任务
        PageHelper.startPage(0,0);
        List<EtlJob> jobList = this.etlJobService.querySysJobList(map);
        if (null == jobList || jobList.size() == 0) {
            logger.info("系统启动，没有需要执行的任务... ...");
        }

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
