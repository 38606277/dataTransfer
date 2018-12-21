package root.etl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.etl.Service.IEtlJobService;
import root.etl.entity.EtlJob;
import root.etl.mapper.EtlJobMapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 14:59
 * @Description: quartz service 实现类
 */
@Service
@Transactional
public class EtlJobServiceImpl implements IEtlJobService {

    @Autowired
    private EtlJobMapper etlJobMapper;

    @Override
    public int getJobCount() {
        return etlJobMapper.getJobCount();
    }

    @Override
    public List<EtlJob> querySysJobList(HashMap<String, String> map) {
        return etlJobMapper.querySysJobList(map);
    }

    @Override
    public int insertSelective(EtlJob record) {
        return etlJobMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return etlJobMapper.deleteByPrimaryKey(id);
    }

    @Override
    public EtlJob selectByPrimaryKey(Integer id) {
        return etlJobMapper.selectByPrimaryKey(id);
    }

    @Override
    public EtlJob selectByBean(EtlJob bean) {
        return etlJobMapper.selectByBean(bean);
    }

    @Override
    public int updateByPrimaryKeySelective(EtlJob bean) {
        return etlJobMapper.updateByPrimaryKeySelective(bean);
    }

    /**
     *
     * 功能描述:
     *   根据组名跟名称查询一条 job 信息
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/12/14 12:10
     */
    @Override
    public EtlJob getEtlJobByNameAndGroup(String job_name,String job_group_name){
        Map<String,String> map = new HashMap<>();
        map.put("job_name",job_name);
        map.put("job_group",job_group_name);
        EtlJob etlJob =  this.etlJobMapper.getEtlJobByNameAndGroup(map);
        return etlJob;
    }
}
