package root.etl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.etl.Service.ITransferService;
import root.etl.mapper.TransferMapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/12/11 15:21
 * @Description:
 */
@Service
@Transactional
public class TransferServiceImpl implements ITransferService {

    @Autowired
    TransferMapper transferMapper;

    @Override
    public int createTransfer(String id) {
        return transferMapper.createTransfer(id);
    }

    @Override
    public int updateTransfer(Map map) {
        return transferMapper.updateTransfer(map);
    }

    @Override
    public int batchDeleteTransfer(List list) {
        return transferMapper.batchDeleteTransfer(list);
    }

    @Override
    public int deleteTransfer(int id) {
        return transferMapper.deleteTransfer(id);
    }

    @Override
    public List<Map> getAllTransfer() {
        return transferMapper.getAllTransfer();
    }

    @Override
    public Map getTransferById(int id) {
        return transferMapper.getTransferById(id);
    }
}
