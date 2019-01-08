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
public interface TransferService {

    int addTransferSQL(Map map);

    int updateTransfer(Map map);

    int deleteTransfer(int transfer_id);

    List<Map> getAllTransfer();

}
