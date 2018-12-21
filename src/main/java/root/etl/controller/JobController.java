package root.etl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import root.configuration.RO;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.etl.Util.Constant;
import root.etl.Util.SchedulerUtil;
import root.etl.entity.EtlJob;
import root.etl.entity.EtlJobExecute;
import root.etl.entity.LayuiData;
import root.etl.exception.BizException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 15:10
 * @Description: quartz 的 job 动态管理controller类
 */
// 不能定义成 restController  因为我要实现页面跳转
@Controller
@RequestMapping("/job")
@Transactional(rollbackFor=Exception.class)
public class JobController extends RO {

    private static Logger logger = Logger.getLogger(JobController.class);

    // 注入 service 接口
    @Autowired
    private IEtlJobService etlJobService;

    @Autowired
    private IEtlJobExecuteService etlJobExecuteService;

    // 列表页
    @RequestMapping(value = "/jobList", produces = "text/plain;charset=UTF-8")
    public String jobList(HttpServletRequest request, HttpServletResponse response) {
       /* String credentials = request.getHeader("credentials");
        JSONObject obj = null;
        if (credentials == null) {
            return "index.html";
        } else {
            obj = JSON.parseObject(credentials);
        }
        String userCode = obj.getString("UserCode");
        String pwd = obj.getString("Pwd");*/
        return "jobListPage";    //返回 jobListPage ，让前端自己跳转到 对应列表页
    }

    //  打开详情页
    @RequestMapping(value = "/toDetail", produces = "text/plain;charset=UTF-8")
    public String toDetail(Integer id, Model model) {
        EtlJob job = etlJobService.selectByPrimaryKey(id);
        model.addAttribute("job", job);
        return "jobDetail";
    }

    // 根据 id  返回 detail信息
    @GetMapping(value = "/getDetail/{id}", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getDetail(@PathVariable("id") String idStr) throws Exception {
        if (StringUtils.isBlank(idStr)) {
            throw new BizException("任务ID不能为空");
        }
        int id = Integer.parseInt(idStr);
        // 1. 校验
        EtlJob queryBean = new EtlJob();
        queryBean.setId(id);
        EtlJob result = this.etlJobService.selectByBean(queryBean);
        if (result != null) {
            return SuccessMsg("1000", JSON.toJSONString(result));
        } else {
            return ErrorMsg("3000", "无对应数据");
        }
    }

    // 根据分页参数 返回所有的 job 信息列表
    @RequestMapping(value = "/getAllJobInfo", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getAllJobInfo(@RequestBody String pJson) throws Exception {

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
        // bounds = new PageRowBounds(startIndex, perPage);
        PageHelper.startPage(startIndex,perPage);

        // 3. 查询分页记录集
        List<EtlJob> etlJobList = this.etlJobService.querySysJobList(new HashMap<>());
        if (etlJobList != null && etlJobList.size() > 0) {
            return SuccessMsg("1000", JSON.toJSONString(etlJobList));
        } else {
            return ErrorMsg("3000", "无数据");
        }
    }

    // 打开修改页面
    @RequestMapping(value = "/toUpdate", produces = "text/plain;charset=UTF-8")
    public String toUpdate(Integer id, Model model) {
        EtlJob job = etlJobService.selectByPrimaryKey(id);
        model.addAttribute("job", job);
        return "jobUpdate";
    }

    // 打开新增界面
    @RequestMapping("/toJob")
    public String toJob() {
        return "jobAdd";
    }

    //查询任务列表
    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    @ResponseBody
    public LayuiData queryJobList(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        // 1. 解析参数
        String idStr = request.getParameter("id");
        String jobName = request.getParameter("jobName");
        String jobGroup = request.getParameter("jobGroup");
        String jobCron = request.getParameter("jobCron");
        String jobClassPath = request.getParameter("jobClassPath");
        String jobDescribe = request.getParameter("jobDescribe");

        // 2. 组装到map上
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(idStr)) {
            map.put("id", idStr);
        }
        if (StringUtils.isNotBlank(jobName)) {
            map.put("jobName", jobName);
        }
        if (StringUtils.isNotBlank(jobGroup)) {
            map.put("jobGroup", jobGroup);
        }
        if (StringUtils.isNotBlank(jobCron)) {
            map.put("jobCron", jobCron);
        }
        if (StringUtils.isNotBlank(jobClassPath)) {
            map.put("jobClassPath", jobClassPath);
        }
        if (StringUtils.isNotBlank(jobDescribe)) {
            map.put("jobDescribe", jobDescribe);
        }
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        if (page >= 1) {
            page = (page - 1) * limit;
        }
        LayuiData layuiData = new LayuiData();

        // 3. 从数据库查询数据并组装
        try {
            List<EtlJob> jobList = this.etlJobService.querySysJobList(map);
            int count = this.etlJobService.getJobCount();
            layuiData.setCode(0);
            layuiData.setCount(count);
            layuiData.setMsg("数据请求成功");
            layuiData.setData(jobList);
            return layuiData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("查询任务列表异常：" + e.getMessage());
        }
    }

    /**
     * // 添加定时任务
     *
     * @PostMapping(value = "/addJob",produces = "text/plain;charset=UTF-8")
     * @ResponseBody public int addjob(HttpServletRequest request, HttpServletResponse response) throws Exception {
     * <p>
     * // 1. 解析参数
     * logger.info("添加任务开始... ...");
     * int num =0;
     * String jobName = request.getParameter("jobName");
     * String jobClassPath= request.getParameter("jobClassPath");
     * String jobGroup= request.getParameter ("jobGroup");
     * String jobCron= request.getParameter("jobCron");
     * String jobDescribe= request.getParameter("jobDescribe");
     * String jobDataMap= request.getParameter("jobDataMap");  // jobDataMap至关重要，凭借其信息关联 etl_transfer 当中的 脚本内容
     * <p>
     * // 2. 校验参数不能为空 否则任务执行会出问题的
     * if (StringUtils.isBlank(jobName)) {
     * throw new BizException("任务名称不能为空");
     * }
     * if (StringUtils.isBlank(jobGroup)) {
     * throw new BizException("任务组别不能为空");
     * }
     * if (StringUtils.isBlank(jobCron)) {
     * throw new BizException("Cron表达式不能为空");
     * }
     * if (StringUtils.isBlank(jobClassPath)) {
     * throw new BizException("任务类路径不能为空");
     * }
     * <p>
     * // 3. 参数不为空时校验格式
     * if(StringUtils.isNotBlank(jobDataMap)) {
     * try {
     * JSONObject.parseObject(jobDataMap);
     * } catch (Exception e) {
     * throw new BizException("参数JSON格式错误");
     * }
     * }
     * <p>
     * // 4. job 存在性校验
     * SqlSession sqlSession = DbFactory.Open(DbFactory.FORM);
     * EtlJob queryBean = new EtlJob();
     * queryBean.setJobName(jobName);
     * EtlJob result = this.etlJobService.selectByBean(sqlSession,queryBean);
     * if (null != result) {
     * throw new BizException("任务名为" + jobName + "的任务已存在！");
     * }
     * <p>
     * // 5. 校验通过 则进行数据库插入工作
     * EtlJob bean = new EtlJob();
     * bean.setJobName(jobName);
     * bean.setJobClassPath(jobClassPath);
     * bean.setJobGroup(jobGroup);
     * bean.setJobCron(jobCron);
     * bean.setJobDescribe(jobDescribe);
     * bean.setJobDataMap(jobDataMap);
     * bean.setJobStatus(Constant.JOB_STATE.YES);
     * try {
     * sqlSession.getConnection().setAutoCommit(false);
     * num = this.etlJobService.insertSelective(sqlSession,bean);
     * sqlSession.getConnection().commit();
     * } catch (Exception e) {
     * sqlSession.getConnection().rollback();
     * throw new BizException("新增定时任务失败");
     * }
     * SchedulerUtil.addJob(jobClassPath,jobName, jobGroup, jobCron,jobDataMap);   // 在当前的 scheduler 管理当中加上这个任务
     * return num;
     * }
     */

    // 添加定时任务 VERSION :  Rest
    @PostMapping(value = "/addJob", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String addjob(@RequestBody JSONObject paramJson) throws Exception {

        // 不要使用 @RequestBody  否则会出问题的 @RequestBody的作用其实是将json格式的数据转为java对象。
        logger.info("添加任务开始... ...");
        int num = 0;
        // 1. 解析  pJson 参数，注意到  jobDataMap 一定不能为空，jobDataMap 存放 transfer_id 的map
        String pJson = paramJson.toJSONString();
        JSONObject jsonObject = JSON.parseObject(pJson);
        String jobName = jsonObject.getString("jobName");
        String jobClassPath = jsonObject.getString("jobClassPath");
        String jobGroup = jsonObject.getString("jobGroup");
        String jobCron = jsonObject.getString("jobCron");
        String jobDescribe = jsonObject.getString("jobDescribe");
        JSONObject jobDataJson = JSON.parseObject(jsonObject.getString("jobDataMap"));
        // jobDataMap至关重要，凭借其信息关联 etl_transfer 当中的 脚本内容
        Map<String, Object> jobDataMap = null;

        // 2. 校验参数不能为空 否则任务执行会出问题的
        if (StringUtils.isBlank(jobName)) {
            throw new BizException("任务名称不能为空");
        }
        if (StringUtils.isBlank(jobGroup)) {
            throw new BizException("任务组别不能为空");
        }
        if (StringUtils.isBlank(jobCron)) {
            throw new BizException("Cron表达式不能为空");
        }
        if (StringUtils.isBlank(jobClassPath)) {
            throw new BizException("任务类路径不能为空");
        }
        // 3. 参数不为空时校验格式
        if (jobDataJson != null) {
            try {
                jobDataMap = JSONObject.toJavaObject(jobDataJson, Map.class);
            } catch (Exception e) {
                logger.error("参数JSON格式错误");
                return ErrorMsg("3000", "参数JSON格式错误");
            }
        }

        // 4. job 存在性校验
        EtlJob queryBean = new EtlJob();
        queryBean.setJobName(jobName);
        EtlJob result = this.etlJobService.selectByBean(queryBean);
        if (null != result) {
            throw new BizException("任务名为" + jobName + "的任务已存在！");
        }

        // 5. 校验通过 则进行数据库插入工作
        EtlJob bean = new EtlJob();
        bean.setJobName(jobName);
        bean.setJobClassPath(jobClassPath);
        bean.setJobGroup(jobGroup);
        bean.setJobCron(jobCron);
        bean.setJobDescribe(jobDescribe);
        bean.setJobDataMap(jobDataJson.toString());   // 注意，存放的是JSONOBject的string才行
        bean.setJobStatus(Constant.JOB_STATE.YES);
        try {
            num = this.etlJobService.insertSelective(bean);
            if(num!=0){
                EtlJob etlJob = this.etlJobService.getEtlJobByNameAndGroup(jobName,jobGroup);
                this.etlJobExecuteService.addEtlJobExecuteForBegin(etlJob.getId());
            }
            // 6. 把job添加到 scheduler
            SchedulerUtil.addJob(jobClassPath, jobName, jobGroup, jobCron, jobDataMap);   // 在当前的 scheduler 管理当中加上这个任务
        } catch (Exception e) {
            logger.error("新增定时任务失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();   // 手动回滚事务,理应AOP切面扫到controller层异常
            // throw new RuntimeException(e.getMessage());
            return ErrorMsg("3000", "新增定时任务失败");
        }
        return SuccessMsg("1000", "新增定时任务成功");
    }

    // 变更定时任务状态  (从启动到 停用 ，从停用到启用) 这也对应前端的 [停用] [启用]  按钮
    @PostMapping(value = "/changeState")
    @ResponseBody
    public String changeState(@RequestBody JSONObject param) throws Exception {

        // 因为只有2个状态，所以不需要传递状态只需要传递一个 id 即可
        String idStr = param.toJSONString();
        logger.info("变更定时任务状态开始... ...");
        if (StringUtils.isBlank(idStr)) {
            throw new BizException("任务ID不能为空");
        }
        int id = param.getIntValue("id");

        // 1. 校验
        EtlJob queryBean = new EtlJob();
        queryBean.setId(id);
        EtlJob result = this.etlJobService.selectByBean(queryBean);
        if (null == result) {
            throw new BizException("任务ID为" + id + "的任务不存在！");
        }

        // 2. 从schduler 当中修改状态
        EtlJob updateBean = new EtlJob();
        updateBean.setId(id);

        // 2.1 如果是现在是启用，则停用
        if (Constant.JOB_STATE.YES == result.getJobStatus()) {
            updateBean.setJobStatus(Constant.JOB_STATE.NO);
            // SchedulerUtil.jobPause(result.getJobName(), result.getJobGroup());   // 暂停 job的使用
            Boolean b = SchedulerUtil.jobIsExist(result.getJobName(), result.getJobGroup());
            if (b) {
                SchedulerUtil.deleteJob(result.getJobName(), result.getJobGroup());  // 直接删除掉
            }
        }
        // 2.2 如果现在是停用，则启用
        if (Constant.JOB_STATE.NO == result.getJobStatus()) {
            updateBean.setJobStatus(Constant.JOB_STATE.YES);
            //SchedulerUtil.jobresume(result.getJobName(), result.getJobGroup());
            Boolean b = SchedulerUtil.jobIsExist(result.getJobName(), result.getJobGroup());
            //存在则激活，不存在则添加
            if (b) {
                SchedulerUtil.jobResume(result.getJobName(), result.getJobGroup());  // 存在的话 立刻激活 resume 一下
            } else {
                // 不存在的话，则进行添加
                Map jobDataMap = JSONObject.toJavaObject(JSON.parseObject(result.getJobDataMap()), Map.class);
                SchedulerUtil.addJob(result.getJobClassPath(), result.getJobName(), result.getJobGroup(), result.getJobCron(), jobDataMap);
            }
        }

        // 3. 数据库修改状态
        try {

            this.etlJobService.updateByPrimaryKeySelective(updateBean);
        } catch (Exception e) {
            logger.error("更新数据库的定时任务信息异常！");
            return ErrorMsg("3000", "更新数据库的定时任务信息异常！");
        }

        return SuccessMsg("1000", "修改状态成功");  // 1 表示成功
    }

    // 删除一个任务
    @PostMapping(value = "/deleteJob/{id}")
    @ResponseBody
    public String deletejob(@PathVariable("id") String idStr) throws Exception {

        logger.info("删除定时任务状态开始... ...");
        if (StringUtils.isBlank(idStr)) {
            throw new BizException("任务ID不能为空");
        }
        int id = Integer.parseInt(idStr);

        // 1. 存在性校验
        EtlJob queryBean = new EtlJob();
        queryBean.setId(id);
        EtlJob result = this.etlJobService.selectByBean(queryBean);
        if (null == result) {
            throw new BizException("任务ID为" + idStr + "的任务不存在！");
        }

        // 2. 更改数据库状态
        try {
            this.etlJobService.deleteByPrimaryKey(id);
            // 3. 更改schduler 的job
            SchedulerUtil.deleteJob(result.getJobName(), result.getJobGroup());
        } catch (Exception e) {
            logger.error("从数据库删除定时任务时发生异常！");
            return ErrorMsg("3000", "从数据库删除定时任务时发生异常！");
        }

        return SuccessMsg("1000", "删除成功");
    }

    // 修改定时表达式

    /**
     * @RequestMapping("/reSchedulejob")
     * @ResponseBody public int updateByBean(HttpServletRequest request, HttpServletResponse response) throws Exception {
     * <p>
     * logger.info("修改定时任务信息开始... ...");
     * int num =0;
     * String jobCron = request.getParameter("jobCron");
     * String jobDescribe = request.getParameter("jobDescribe");
     * String idStr = request.getParameter("id");
     * int id = Integer.parseInt(idStr);
     * // 数据非空校验
     * if (!StringUtils.isNotBlank(idStr)) {
     * throw new BizException("任务ID不能为空");
     * }
     * <p>
     * SqlSession sqlSession = DbFactory.Open(DbFactory.FORM);
     * // 1. 验证数据库是否存在
     * EtlJob pccwJob = this.etlJobService.selectByPrimaryKey(sqlSession,id);
     * // 数据不存在
     * if (null == pccwJob) {
     * throw new BizException("根据任务ID[" + id + "]未查到相应的任务记录");
     * }
     * <p>
     * <p>
     * // 2. 修改数据库状态
     * EtlJob bean = new EtlJob();
     * bean.setId(id);
     * bean.setJobCron(jobCron);
     * bean.setJobDescribe(jobDescribe);
     * try {
     * sqlSession.getConnection().setAutoCommit(false);
     * num = this.etlJobService.updateByPrimaryKeySelective(sqlSession,bean);
     * <p>
     * // 3. 从schduler 当中修改掉状态
     * //只有任务状态为启用，才开始运行
     * // 如果先启动再手工插入数据，此处会报空指针异常
     * if( pccwJob.getJobStatus() ==Constant.JOB_STATE.YES ){
     * SchedulerUtil.updateJobCronExpression(pccwJob.getJobName(), pccwJob.getJobGroup(),jobCron);
     * }
     * <p>
     * sqlSession.getConnection().commit();
     * } catch (Exception e) {
     * sqlSession.getConnection().rollback();
     * throw new BizException("变更任务表达式异常：" + e.getMessage());
     * }
     * <p>
     * return num;
     * }
     */

    // 修改定时任务信息
    @PostMapping(value = "/updateJob", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String updateByBean(@RequestBody JSONObject paramJson) throws Exception {

        logger.info("修改定时任务信息开始... ...");
        String pJson = paramJson.toJSONString();
        JSONObject jsonObject = JSON.parseObject(pJson);

        // 1. 从json当中解析数据
        String jobCron = jsonObject.getString("jobCron"); // request.getParameter("jobCron");
        String jobDescribe = jsonObject.getString("jobDescribe"); // request.getParameter("jobDescribe");
        String idStr = jsonObject.getString("id");  // request.getParameter("id");
        JSONObject jobDataJson = JSON.parseObject(jsonObject.getString("jobDataMap"));
        int id = Integer.parseInt(idStr);
        // 数据非空校验
        if (!StringUtils.isNotBlank(idStr)) {
            throw new BizException("任务ID不能为空");
        }

        // 1. 验证数据库是否存在
        EtlJob pccwJob = this.etlJobService.selectByPrimaryKey(id);
        // 数据不存在
        if (null == pccwJob) {
            throw new BizException("根据任务ID[" + id + "]未查到相应的任务记录");
        }


        // 2. 修改数据库状态
        EtlJob bean = new EtlJob();
        bean.setId(id);
        bean.setJobCron(jobCron);
        bean.setJobDescribe(jobDescribe);
        bean.setJobDataMap(jobDataJson.toString());
        try {
            this.etlJobService.updateByPrimaryKeySelective(bean);

            // 3. 修改掉 jobDataMap 当中的值
            Map<String, Object> itemMap = JSONObject.toJavaObject(jobDataJson, Map.class);
            SchedulerUtil.updateJsonDataMap(pccwJob.getJobName(), pccwJob.getJobGroup(), itemMap);

            // 4. 从schduler 当中修改掉状态  (运行态必须重置 cronTrigger)
            //只有任务状态为启用，才开始运行
            // 如果先启动再手工插入数据，此处会报空指针异常
            if (pccwJob.getJobStatus() == Constant.JOB_STATE.YES) {
                SchedulerUtil.updateJobCronExpression(pccwJob.getJobName(), pccwJob.getJobGroup(), jobCron);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("变更任务异常：" + e.getMessage());
            return ErrorMsg("3000", "变更任务异常" + e.getMessage());
        }

        return SuccessMsg("1000", "修改任务信息成功");
    }

    //  展示任务调度管理页
    @RequestMapping(value = "/jobPage", method = RequestMethod.GET)
    public String getJobPage(HttpServletRequest request, HttpServletResponse rep) {
        return "job/job_info";
    }


}
