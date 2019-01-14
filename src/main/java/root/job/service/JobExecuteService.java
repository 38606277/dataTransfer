package root.job.service;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

// 直接与 mapper.xml 对应
@Mapper
public interface JobExecuteService {

    int addJobExecute(Map map);

    Map getJobExecuteById(int id);

    int updateEtlJobExecute(Map map);

    /**
     * 默认降序排列
     */
    List<Map> getJobExecuteByJobId(int job_id);

    int deleteEtlJobExecute(int id);
}
