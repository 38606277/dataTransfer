package root.job.control;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.common.RO;
import root.job.service.JobService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/job")
public class JobControl extends RO {


    @Autowired
    JobService jobService;



    /*返回把有的QuartzJob*/
    @RequestMapping(value = "/GetAllJob", produces = "text/plain;charset=UTF-8")
    public String GetAllJob() {

        List<Map<String, String>> list ;
        try {
            list=jobService.GetAllJob();
            return SuccessMsg("", list);
        } catch (Exception ex){
            return ExceptionMsg(ex.getMessage());
        }
    }

    /*创建一个QuartzJob*/
    @RequestMapping(value = "/CreateJob", produces = "text/plain;charset=UTF-8")
    public String CreateJob() {
        return "";
    }
    /*修改一个QuartzJob*/
    @RequestMapping(value = "/UpdateJob", produces = "text/plain;charset=UTF-8")
    public String UpdateJob() {
        return "";
    }
    /*删除一个QuartzJob*/
    @RequestMapping(value = "/DeleteJob", produces = "text/plain;charset=UTF-8")
    public String DeleteJob() {
        return "";
    }

    /*立即执行一个QuartzJob*/
    @RequestMapping(value = "/ExecuteJob", produces = "text/plain;charset=UTF-8")
    public String ExecuteJob() {
        return "";
    }

    /*暂停一个QuartzJob*/
    @RequestMapping(value = "/PauseJob", produces = "text/plain;charset=UTF-8")
    public String PauseJob() {



        return "";
    }
    /*还原一个QuartzJob*/
    @RequestMapping(value = "/ResumeJob", produces = "text/plain;charset=UTF-8")
    public String ResumeJob() {



        return "";
    }


}
