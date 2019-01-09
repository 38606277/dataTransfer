package root.job.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.common.RO;
import root.etl.Util.Constant;
import root.etl.Util.SchedulerUtil;
import root.etl.exception.BizException;
import root.job.service.JobExecuteService;
import root.job.service.JobService;
import root.job.task.TransferTask;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("transfer/job")
public class JobControl extends RO {

    private static Logger logger = Logger.getLogger(JobControl.class);

    @Autowired
    JobService jobService;

    @Autowired
    JobExecuteService jobExecuteService;

    /*返回把有的QuartzJob*/
    @RequestMapping(value = "/getAllJob", produces = "text/plain;charset=UTF-8")
    public String getAllJob(@RequestBody String pJson) {

        // 1. 获取分页参数
        JSONObject obj = JSON.parseObject(pJson);
        int startIndex = obj.getIntValue("pageNumd");
        int perPage = obj.getIntValue("perPaged");
        if (startIndex == 1 || startIndex == 0) {
            startIndex = 0;
        } else {
            startIndex = (startIndex - 1) * perPage;
        }

        // 2. 组装内存bounds
        PageHelper.startPage(startIndex,perPage);   // 分页 紧贴着的下一个对象

        try {
        List<Map> list = jobService.getAllJob();
            Map<String,Object> resultMap = new HashMap<>();
            PageInfo<Map> pageInfo = new PageInfo<>(list);
            //获得总条数
            long total = pageInfo.getTotal();
            resultMap.put("resultTotal",total);
            resultMap.put("resultRows",list);

            return SuccessMsg("", resultMap);
        } catch (Exception ex){
            ex.printStackTrace();
            return ExceptionMsg(ex.getMessage());
        }
    }

    /*创建一个QuartzJob*/
    @RequestMapping(value = "/createJob", produces = "text/plain;charset=UTF-8")
    public String createJob(@RequestBody String paramJson) {

        logger.info("添加任务开始... ...");
        int num = 0;
        Map<String, Object> jobDataMap = null;
        Map<String,Object> paramMap = null;
        // 1. 解析  pJson 参数，注意到  jobDataMap 一定不能为空，jobDataMap 存放 transfer_id 的map
        JSONObject jsonObject = JSON.parseObject(paramJson);
        String job_name = jsonObject.getString("job_name");
        String job_group = jsonObject.getString("job_group");
        String job_cron = jsonObject.getString("job_cron");
        String job_describe = jsonObject.getString("job_describe");
        int transfer_id = jsonObject.getIntValue("transfer_id");

        String job_param_json = jsonObject.getString("job_param");
        JSONObject jobParamJosn = JSON.parseObject(job_param_json);

        // 2. 校验参数不能为空 否则任务执行会出问题的
        if (StringUtils.isBlank(job_name)) {
            return ErrorMsg("3000","任务名称不能为空");
        }
        if (StringUtils.isBlank(job_group)) {
            return ErrorMsg("3000","任务组别不能为空");
        }
        if (StringUtils.isBlank(job_cron)) {
            return ErrorMsg("3000","Cron表达式不能为空");
        }
        if (StringUtils.isBlank(String.valueOf(transfer_id))) {
            return ErrorMsg("3000","关联脚本不能为空");
        }else {
            jobDataMap = new HashMap<>();
            jobDataMap.put("transfer_id",transfer_id);
        }

        // 3. 组装mapParam数据，验证是否已经存在
        paramMap = new HashMap<>();
        paramMap.put("job_name",job_name);
        paramMap.put("job_group",job_group);
        Object obj = this.jobService.getJobByParam(paramMap);
        if(obj != null){
            return ErrorMsg("3000","任务["+job_name+"]已经存在");
        }

        // 4. 验证通过，插入数据
        paramMap.put("job_cron",job_cron);
        paramMap.put("job_describe",job_describe);
        paramMap.put("transfer_id",transfer_id);
        paramMap.put("job_param",job_param_json);
        paramMap.put("job_status",1);    // 默认添加的时候是启动状态

        num = this.jobService.addJob(paramMap);
        if(num!=0){
            // 因为插入成功就代表开启了job,所以现在需要往jobExecute当中插入信息
           String id = String.valueOf(paramMap.get("id"));
           if(StringUtils.isNotBlank(id)){
               try {
                   // 对 param 参数解析
                   if(Constant.TASK_CLASS.TRANSFER_VALUE.equals(jobParamJosn.getString("task_path"))){
                       SchedulerUtil.addJob(Constant.TASK_CLASS.TRANSFER_TASK_CLASS_PATH,job_name, job_group, job_cron, jobDataMap);   // 在当前的 scheduler 管理当中加上这个任务
                   }else {
                       SchedulerUtil.addJob(Constant.TASK_CLASS.DEFAULT_TASK_CLASS_PATH,job_name, job_group, job_cron, jobDataMap);   // 在当前的 scheduler 管理当中加上这个任务
                   }
               } catch (Exception e) {
                   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
                   logger.error("内存定时任务管理器:加入任务[\"+job_name+\"]失败,具体错误如下\n"+e.getMessage());
                   e.printStackTrace();
                   return ErrorMsg("3000","内存定时任务管理器:加入任务["+job_name+"]失败");
               }
           }else {
               return ErrorMsg("3000","任务插入时获取自增长主键失败");
           }
        }

        logger.info("添加任务成功!");
        return SuccessMsg("1000", "新增定时任务成功");
    }


    /*修改一个QuartzJob*/
    @RequestMapping(value = "/updateJob", produces = "text/plain;charset=UTF-8")
    public String updateJob(@RequestBody String paramJson) {

        logger.info("修改定时任务信息开始... ...");
        JSONObject jsonObject = JSON.parseObject(paramJson);
        Map<String,Object> paramMap = null;

        // 1. 从json当中解析数据
        String job_cron = jsonObject.getString("job_cron");
        String job_describe = jsonObject.getString("job_describe");
        int id = jsonObject.getIntValue("id");
        int transfer_id = jsonObject.getIntValue("transfer_id");
        String job_param_str = jsonObject.getString("job_param");
        JSONObject jobParamJosn = JSON.parseObject(job_param_str);

        // 2. 非空校验
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ErrorMsg("3000","任务ID不能为空");
        }

        // 3. 验证数据库是否存在
        Map resultMap = this.jobService.getJobById(id);
        if(null == resultMap){
            return ErrorMsg("3000","未查询到该JOB信息,无法修改");
        }

        // 4. 修改数据库信息
        paramMap = new HashMap<>();
        paramMap.put("id",id);
        paramMap.put("job_cron",job_cron);
        paramMap.put("job_describe",job_describe);
        paramMap.put("transfer_id",transfer_id);
        paramMap.put("job_param",job_param_str);

        // 4. 修改掉 jobDataMap 当中的值的 必须在任务开启的时候才能更改掉JobDataMap 当中的信息
        Map jobDataMap = new HashMap();
        jobDataMap.put("transfer_id",transfer_id);
        try {
            this.jobService.updateJobById(paramMap);
            // 5. 从schduler 当中修改掉状态  (运行态必须重置 cronTrigger)  只有任务状态为启用，才开始运行
            if (Integer.parseInt(resultMap.get("job_status").toString()) == Constant.JOB_STATE.YES) {
                SchedulerUtil.updateJobCronExpressionForMemory(resultMap.get("job_name").toString(), resultMap.get("job_group").toString(), job_cron,jobDataMap);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
            logger.error("变更任务异常：" + e.getMessage());
            return ErrorMsg("3000", "变更任务异常" + e.getMessage());
        }

        logger.info("修改信息任务信息成功");
        return SuccessMsg("1000", "修改任务信息成功");
    }


    /*删除一个QuartzJob*/
    @RequestMapping(value = "/deleteJob", produces = "text/plain;charset=UTF-8")
    public String deleteJob(@RequestBody String paramJson) {

        logger.info("删除定时任务状态开始... ...");
        JSONObject jsonObject = JSON.parseObject(paramJson);
        int id = jsonObject.getIntValue("id");
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ErrorMsg("3000","参数ID不能为空");
        }

        // 1. 存在性校验
        Map resultMap = this.jobService.getJobById(id);
        if(null == resultMap){
            return ErrorMsg("3000","数据库无此JOB信息,无法删除");
        }

        // 2. 删除
        try {
            this.jobService.deleteJobById(id);
            SchedulerUtil.deleteJobForMemory(resultMap.get("job_name").toString(),resultMap.get("job_group").toString());
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
            logger.error("变更任务异常：" + e.getMessage());
            return ErrorMsg("3000", "删除任务异常" + e.getMessage());
        }

        logger.info("删除定时任务状态成功");
        return SuccessMsg("1000","删除定时任务状态成功");
    }

    /*立即执行一个QuartzJob*/
    @RequestMapping(value = "/executeJob", produces = "text/plain;charset=UTF-8")
    public String executeJob(@RequestBody String paramJson) {

        logger.info("立即执行一个job");
        JSONObject jsonObject = JSON.parseObject(paramJson);
        int id = jsonObject.getIntValue("id");

        // 1. 非空校验
        if(StringUtils.isBlank(String.valueOf(id))){
            return ErrorMsg("3000","ID不能为空");
        }

        // 2. 数据库存在性校验
        Map map  = this.jobService.getJobById(id);
        if(null == map){
            return ErrorMsg("3000","任务ID为" + id + "的任务不存在!");
        }

        // 3. 对此数据的状态进行判断 ，如果为0 则变为1  启用状态
        if(String.valueOf(Constant.JOB_STATE.NO).equals(map.get("job_status").toString())){
            try{
                map.put("job_status",Constant.JOB_STATE.YES);
                String job_name = map.get("job_name").toString();
                String job_group = map.get("job_group").toString();
                Boolean b = SchedulerUtil.jobIsExist(job_name,job_group);
                //存在则激活，不存在则添加
                if (b) {
                    SchedulerUtil.jobResume(job_name, job_group);  // 存在的话 立刻激活 resume 一下
                } else {
                    // 不存在的话，则进行添加
                    Map jobDataMap = new HashMap();
                    jobDataMap.put("transfer_id", map.get("transfer_id"));
                    SchedulerUtil.addJob(Constant.TASK_CLASS.DEFAULT_TASK_CLASS_PATH,job_name, job_group, map.get("job_cron").toString(), jobDataMap);
                }
                this.jobService.updateJobById(map);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
                logger.error("执行任务异常：" + e.getMessage());
                return ErrorMsg("3000", "执行任务异常" + e.getMessage());
            }
        }else {
            return ErrorMsg("3000","任务ID为" + id + "的任务已经在运行!");
        }

        logger.info("执行成功");
        return SuccessMsg("1000","执行成功");
    }

    /*暂停一个QuartzJob*/
    @RequestMapping(value = "/pauseJob", produces = "text/plain;charset=UTF-8")
    public String pauseJob(@RequestBody String  paramJson) {

        logger.info("立即停止一个job");
        JSONObject jsonObject = JSON.parseObject(paramJson);
        int id = jsonObject.getIntValue("id");

        // 1. 非空校验
        if(StringUtils.isBlank(String.valueOf(id))){
            return ErrorMsg("3000","ID不能为空");
        }

        // 2. 数据库存在性校验
        Map map  = this.jobService.getJobById(id);
        if(null == map){
            return ErrorMsg("3000","任务ID为" + id + "的任务不存在!");
        }

        // 3. 对此数据的状态进行判断 ，如果为0 则变为1  启用状态
        if(String.valueOf(Constant.JOB_STATE.YES).equals(map.get("job_status").toString())){
            try{
                map.put("job_status",Constant.JOB_STATE.NO);
                String job_name = map.get("job_name").toString();
                String job_group = map.get("job_group").toString();
                SchedulerFactory sf = new StdSchedulerFactory();
                Scheduler scheduler = sf.getScheduler();
                JobKey jobKey = new JobKey(job_name,job_group);
                if(scheduler.checkExists(jobKey)){
                    scheduler.pauseJob(jobKey);
                }
                this.jobService.updateJobById(map);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
                logger.error("终止任务异常：" + e.getMessage());
                return ErrorMsg("3000", "终止任务异常" + e.getMessage());
            }
        }else {
            return ErrorMsg("3000","任务ID为" + id + "的任务不处于运行状态,终止无效!");
        }

        logger.info("终止执行成功");
        return SuccessMsg("1000","终止执行成功");
    }

    /*还原一个QuartzJob*/
    @RequestMapping(value = "/resumeJob", produces = "text/plain;charset=UTF-8")
    public String resumeJob(@RequestBody String  paramJson) {

        logger.info("重启任务中......");
        JSONObject jsonObject = JSON.parseObject(paramJson);
        int id = jsonObject.getIntValue("id");
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            // 1. 存在性校验
            Map map = this.jobService.getJobById(id);
            if(null == map){
                return ErrorMsg("3000","不存在此任务,无法重启!");
            }

            String job_name = map.get("job_name").toString();
            String job_group = map.get("job_group").toString();
            Scheduler scheduler = sf.getScheduler();
            JobKey jobKey = new JobKey(job_name,job_group);
            if(scheduler.checkExists(jobKey)){
                scheduler.pauseJob(jobKey);
                scheduler.resumeJob(jobKey);
            }else {
                return ErrorMsg("3000","任务不在执行状态,无法从内存中获取从而重启该任务");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ErrorMsg("3000","获取定时任务scheduler失败");
        }

        logger.info("重启任务完毕.");
        return SuccessMsg("1000","重启任务成功");
    }
    /*查询一个Job*/
    @RequestMapping(value = "/getJobById", produces = "text/plain;charset=UTF-8")
    public String getJobById(@RequestBody String  paramJson) {

        JSONObject jsonObject = JSON.parseObject(paramJson);
        int id = jsonObject.getIntValue("id");

        // 1. 非空校验
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ErrorMsg("3000", "ID不能为空");
        }

        // 2. 数据库存在性校验
        Map map = this.jobService.getJobById(id);
        if (null == map) {
            return ErrorMsg("3000", "任务ID为" + id + "的任务不存在!");
        }
        return SuccessMsg("1000",map);
    }

}
