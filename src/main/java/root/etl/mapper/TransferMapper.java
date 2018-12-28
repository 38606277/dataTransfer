package root.etl.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lxf
 * @Date: 2018/12/11 15:12
 * @Description:
 */
@Mapper
public interface TransferMapper {

    // 返回操作结果记录集
    int createTransfer(String transfer_content);

    // updateTransfer
    int updateTransfer(Map map);

    int batchDeleteTransfer(List list);

    int deleteTransfer(int id);

    List<Map> getAllTransfer();

    Map getTransferById(int id);

}
