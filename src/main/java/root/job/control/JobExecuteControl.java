package root.job.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.common.RO;
import root.job.service.JobExecuteService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2019/1/9 11:48
 * @Description:
 */
@RestController
@RequestMapping("transfer/jobExecute")
public class JobExecuteControl extends RO {

    @Autowired
    JobExecuteService jobExecuteService;

    /*返回把有的QuartzJob*/
    @RequestMapping(value = "/getJobExecuteByJobId", produces = "text/plain;charset=UTF-8")
    public String getAllJob(@RequestBody String pJson) {

        JSONObject obj = JSON.parseObject(pJson);
        int job_id = obj.getIntValue("job_id");
        List<Map> mapList = this.jobExecuteService.getJobExecuteByJobId(job_id);

        return SuccessMsg("1000",mapList);
    }
}
