package root.job.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.common.RO;
import root.etl.Util.Constant;
import root.job.service.JobExecuteService;

import java.util.HashMap;
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
    public String getJobExecuteByJobId(@RequestBody String pJson) {

        JSONObject obj = JSON.parseObject(pJson);
        int startIndex = obj.getIntValue("pageNumd");
        int perPage = obj.getIntValue("perPaged");
        // TODO ：  分页参数
        PageHelper.startPage(startIndex,perPage,true);   // 分页 紧贴着的下一个对象
        int job_id = obj.getIntValue("job_id");
        List<Map> mapList = this.jobExecuteService.getJobExecuteByJobId(job_id);
        Map<String,Object> resultMap = new HashMap<>();
        PageInfo<Map> pageInfo = new PageInfo<>(mapList);
        //获得总条数
        long total = pageInfo.getTotal();
        resultMap.put("resultTotal",total);
        resultMap.put("resultRows",mapList);

        return SuccessMsg("", resultMap);
    }

    /**
     * 从内存当中取出指定 job_execute_id 的执行进度条数
     */
    @RequestMapping(value = "/getJobExecutePorcess", produces = "text/plain;charset=UTF-8")
    public String getJobExecutePorcess(@RequestBody String pJson) {

        JSONObject obj = JSON.parseObject(pJson);
        int job_execute_id = obj.getIntValue("job_execute_id");  // 此ID 为 job_execute_id

        Map<String,Integer> map = new HashMap<>();
        map.put("count",Constant.PROCESSMAP.get(job_execute_id+Constant.TOTAL));
        map.put("current",Constant.PROCESSMAP.get(job_execute_id+Constant.DONE));

        return SuccessMsg("1000",map);

    }

}
