package root.etl.mapper;

import org.apache.ibatis.annotations.Mapper;
import root.etl.entity.EtlJob;
import root.etl.entity.EtlJobExecute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 14:52
 * @Description:
 */
@Mapper
public interface EtlJobExecuteMapper {

    // 插入记录
    int addEtlJobExecute(EtlJobExecute etlJobExecute);

    // 更新记录
    int updateEtlJobExecute(EtlJobExecute etlJobExecute);

    // 根据 id 查询一条记录
    EtlJobExecute getEtlJobExecuteById(int job_id);
}
