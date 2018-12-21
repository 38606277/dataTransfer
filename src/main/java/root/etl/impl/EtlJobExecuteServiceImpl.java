package root.etl.impl;

import org.apache.ibatis.annotations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.entity.EtlJobExecute;
import root.etl.mapper.EtlJobExecuteMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/12/13 11:31
 * @Description:
 */
@Service(value = "etlJobExecuteServiceImpl")
@Transactional
public class EtlJobExecuteServiceImpl implements IEtlJobExecuteService {

    private final BigDecimal finalProcess = new BigDecimal("100.00");  // 制定 完成之后的任务进度为100.00

    @Autowired
    EtlJobExecuteMapper etlJobExecuteMapper;

    /**
     *
     * 功能描述:
     *     增加 任务执行情况 记录
     * @param:
     *      job_id  -  来自etl_job 表的 id
     * @return:
     *      执行情况记录
     * @auther: pccw
     * @date: 2018/12/13 11:35
     */
    @Override
    public int addEtlJobExecuteForBegin(int job_id) {

        EtlJobExecute etlJobExecute = new EtlJobExecute();
        etlJobExecute.setJob_id(job_id);
        etlJobExecute.setJob_begin_date(new Date());
        etlJobExecute.setJob_process_number(new BigDecimal(0.00));   // 默认值 0.00

        return etlJobExecuteMapper.addEtlJobExecute(etlJobExecute);
    }

    /**
     *
     * 功能描述: 
     *      map : map中存放 job_id 跟 job_process
     *      注意： 在累加 job_process 而不是 每次更新
     * @param:
     * @return: 
     * @auther: pccw
     * @date: 2018/12/13 15:55
     */
    @Override
    public synchronized  int updateEtlJobExecuteForProcess(int job_id,BigDecimal process) {

        int result = 0;
        // 1. 得到原始数据的
        EtlJobExecute etlJobExecute = this.etlJobExecuteMapper.getEtlJobExecuteById(job_id);
        if(etlJobExecute!=null  && etlJobExecute.getJob_process_number()!=null){
            //2. 累加 bigDecimal
            etlJobExecute.setJob_process_number(etlJobExecute.getJob_process_number().add(process));
            result =  this.etlJobExecuteMapper.updateEtlJobExecute(etlJobExecute);
        }
        return result;
    }

    /**
     *
     * 功能描述:
     *      将指定的这条 job_exeute执行情况的记录的 process 变为100.00 ,并且填充上 end_date
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/12/14 13:19
     */
    @Override
    public int updateEtlJobExecuteToEnd(int job_id) {
        EtlJobExecute etlJobExecute = this.etlJobExecuteMapper.getEtlJobExecuteById(job_id);
       //  etlJobExecute.setJob_id(job_id);
        etlJobExecute.setJob_process_number(finalProcess);
        etlJobExecute.setJob_end_date(new Date());
        return this.etlJobExecuteMapper.updateEtlJobExecute(etlJobExecute);
    }

    @Override
    public EtlJobExecute getEtlJobExecuteById(int id) {
        return this.etlJobExecuteMapper.getEtlJobExecuteById(id);
    }
}
