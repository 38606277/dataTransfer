package root.etl.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/12/11 15:20
 * @Description:
 */
public interface ITransferService {

    // 返回操作结果记录集
    int createTransfer(String id);

    // updateTransfer
    int updateTransfer(Map map);

    int batchDeleteTransfer(List list);

    int deleteTransfer(int id);

    List<Map> getAllTransfer();

    Map getTransferById(int id);
}
