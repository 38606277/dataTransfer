package root.job.service;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.quartz.CronScheduleBuilder.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

// 直接与 mapper.xml 对应
@Mapper
public interface JobService {

    /*返回把有的QuartzJob*/
    List<Map> getAllJob();

    Map<String,Object> getJobByParam(Map map);

    List<Map> getJobListByParam(Map map);

    int addJob(Map map);

    Map getJobById(int id);

    int updateJobById(Map map);

    int deleteJobById(int id);

   /* *//*在数据库中返回一个Job
    *
    *
    *
    * *//*
    public JSONObject GetJobByID(SqlSession sqlSession, String job_id){


        return null;
    }
    *//*返回所有运行中QuartzJob
    *
    *
    *
    *
    * **********************//*
    public List GetAllRuningJob() {

       //查询运行中的Scheduler
        return new ArrayList<>();
    }

    *//****************创建一个QuartzJob*****************
     *
     * 参数说明：
     *
     * 输入参数：
     *   {
     *    job_name:“任务名称”
     *    job_describe:“任务描述”
     *    job_group:“任务组名”
     *    transfer_id:“脚本ID”
     *    transfer_name:“脚本名称”
     *    job_cron:“定时表达式”
     *    job_status:"任务状态"
     *    job_param:"任务参数"
     *   }
     * 输出参数
     * success:
     * error:
     **************************************************//*
    public String CreateJob(SqlSession sqlSession, JSONObject jsonJob) {

        //1.检查JobName必须唯一


        //2.数据库中增加一条Job

        Map<String, Object> mapJob = new HashMap<>();   // 必须设定为Object,因为我们想要让其返回自增长类型值
        mapJob.put("job_name", jsonJob.getString("job_name"));
        mapJob.put("job_describe", jsonJob.getString("job_describe"));
        mapJob.put("job_group", jsonJob.getString("job_group"));
        mapJob.put("transfer_id", jsonJob.getString("transfer_id"));
        mapJob.put("job_cron", jsonJob.getString("job_cron"));
        mapJob.put("job_status", jsonJob.getString("job_status"));
        mapJob.put("job_param", jsonJob.getString("job_param"));

        sqlSession.insert("job.createJob", mapJob);


        //3.Quartz中增加一条Job

        JobDetail job = newJob(TransferJob.class)
                .withIdentity(jsonJob.getString("job_name"), jsonJob.getString("job_group"))
                .usingJobData("transfer_id",jsonJob.getString("transfer_id"))//传送脚本ID
                .usingJobData("job_param",jsonJob.getString("job_param"))//传送脚本的参数
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(jsonJob.getString("job_name"), jsonJob.getString("job_group"))
                .withSchedule(cronSchedule(jsonJob.getString("job_cron")))
                .build();

        try {
            QuartzService.GetScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        //4.返回创建Job的id
        return  mapJob.get("id").toString();
    }

    *//*修改一个QuartzJob*//*
    public String UpdateJob() {

        //数据库中修改一条Job


        //Quartz中删除一条Job，增加一条job

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey("", "");
            // 停止触发器
            QuartzService.GetScheduler().pauseTrigger(triggerKey);
            // 移除触发器
            QuartzService.GetScheduler().unscheduleJob(triggerKey);
            // 删除任务
            QuartzService.GetScheduler().deleteJob(JobKey.jobKey("", ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    *//*删除一个QuartzJob*//*
    public String DeleteJob() {

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey("", "");
            // 停止触发器
            QuartzService.GetScheduler().pauseTrigger(triggerKey);
            // 移除触发器
            QuartzService.GetScheduler().unscheduleJob(triggerKey);
            // 删除任务
            QuartzService.GetScheduler().deleteJob(JobKey.jobKey("", ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    *//*执行所有的QuartzJob*//*
    public String ExecuteAllJob() {

        //查找etl_job表中状态是1的
        List<Map<String,String>> jobs=this.GetAllJob();
        //循环创建任务
        for(int i=0;i<jobs.size();i++)
        {

//            HashMap<String,String> aJob=(HashMap)jobs.get(i);
//            JobDetail job = newJob(TransferJob.class)
//                    .withIdentity(aJob.get("job_name"), jsonJob.getString("job_group"))
//                    .usingJobData("transfer_id",jsonJob.getString("transfer_id"))//传送脚本ID
//                    .usingJobData("job_param",jsonJob.getString("job_param"))//传送脚本的参数
//                    .build();
//
//            Trigger trigger = newTrigger()
//                    .withIdentity(jsonJob.getString("job_name"), jsonJob.getString("job_group"))
//                    .withSchedule(cronSchedule(jsonJob.getString("job_cron")))
//                    .build();
//
//            try {
//                QuartzService.GetScheduler().scheduleJob(job, trigger);
//            } catch (SchedulerException e) {
//                e.printStackTrace();
//            }
        }




        //4.返回创建Job的id

        return "";
    }



    *//*暂停一个QuartzJob*//*
    public String PauseJob() {


        return "";
    }

    *//*还原一个QuartzJob*//*
    public String ResumeJob() {

        return "";
    }*/
}
