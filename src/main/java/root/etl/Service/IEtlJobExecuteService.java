package root.etl.Service;

import root.etl.entity.EtlJob;
import root.etl.entity.EtlJobExecute;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 14:56
 * @Description:
 */
public interface IEtlJobExecuteService {

   int addEtlJobExecuteForBegin(int job_id);

   int updateEtlJobExecuteForProcess(int job_id,BigDecimal process);

   int updateEtlJobExecuteToEnd(int job_id);

   EtlJobExecute getEtlJobExecuteById(int id);
}
