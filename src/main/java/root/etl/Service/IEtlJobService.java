package root.etl.Service;

import root.etl.entity.EtlJob;

import java.util.HashMap;
import java.util.List;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 14:56
 * @Description:
 */
public interface IEtlJobService {

    /**
     * 获取任务数量
     *
     * @param
     * @return
     */
    public int getJobCount();

    /**
     * 查询定时任务列表
     *
     * @param map
     * @return
     */
    List<EtlJob> querySysJobList(HashMap<String, String> map);

    /**
     * 新增定时任务
     *
     * @param record
     * @return
     */
    int insertSelective(EtlJob record);

    /**
     * 删除定时任务
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据主键查询定时任务详情
     *
     * @param id
     * @return
     */
    EtlJob selectByPrimaryKey(Integer id);

    /**
     * 根据bean查询定时任务详情
     *
     * @param
     * @return
     */
    EtlJob selectByBean(EtlJob bean);

    /**
     * 更新定时任务详情
     *
     * @param
     * @return
     */
    int updateByPrimaryKeySelective(EtlJob bean);

    /**
     *
     * 功能描述:
     *   根据组名跟名称查询一条 job 信息
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/12/14 12:10
     */
    EtlJob getEtlJobByNameAndGroup(String job_name,String job_group_name);
}
