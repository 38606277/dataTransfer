package root.job.service;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2019/1/8 20:11
 * @Description:
 */
@Mapper
public interface FndVarService {

   int addFndVar(Map map);

   int updateFndVar(Map map);

   int deleteFndVar(Map map);

   List<Map> getAllFndVar();

   Map getFndVarByName(Map map);
}
