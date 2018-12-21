package root.etl.mapper;

import org.apache.ibatis.annotations.Mapper;
import root.etl.entity.EtlJob;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 14:52
 * @Description:
 */
@Mapper
public interface EtlJobMapper {

    /**
     * 获取任务数量
     *
     * @param
     * @return
     */
    int getJobCount();

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
     * @param pccwJob
     * @return
     */
    int insertSelective(EtlJob etlJob);

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
     *  根据名称跟组名 查询出 etl_job 信息
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2018/12/14 12:07
     */
    EtlJob getEtlJobByNameAndGroup(Map map);
}
