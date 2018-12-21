package root.etl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.configuration.RO;
import root.etl.Service.ITransferService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2018/12/3 15:50
 * @Description: 基于数据库动态更改
 */
@RestController
@RequestMapping("/dataTransfer/transfer")
public class TransferControl extends RO {

    @Autowired
    ITransferService transferService;

    @RequestMapping(value = "/createTransfer", produces = "text/plain;charset=UTF-8")
    public String createATransfer(@RequestBody String pJson) throws SQLException {

        JSONObject jsonObject = JSON.parseObject(pJson);
        String transfer_content = jsonObject.getString("transfer_content");
        try {
            // 新增一条记录
            transferService.createTransfer(transfer_content);
        } catch (Exception e) {
            return ErrorMsg("3000", e.getMessage());
        }
        return SuccessMsg("1000", "新增成功");
    }

    @RequestMapping(value = "/updateTransfer", produces = "text/plain;charset=UTF-8")
    public String updateTransfer(@RequestBody String pJson) throws SQLException {
        JSONObject jsonObject = JSON.parseObject(pJson);
        int transfer_id = jsonObject.getIntValue("transfer_id");
        String transfer_content = jsonObject.getString("transfer_content");
        Map<String, Object> map = new HashMap<>();
        map.put("transfer_id", transfer_id);
        map.put("transfer_content", transfer_content);

        try {
            // 修改一条记录
            transferService.updateTransfer(map);
        } catch (Exception e) {
            return ErrorMsg("3000", e.getMessage());
        }

        return SuccessMsg("1000", "修改成功");
    }


    @RequestMapping(value = "/deleteTransfer", produces = "text/plain;charset=UTF-8")
    public String deleteTransfer(@RequestBody String pJson) throws SQLException {
        JSONArray jsonArray = JSON.parseArray(pJson);
        try {
            // 删除多条记录
            List list = new ArrayList<>();
            jsonArray.forEach(e ->
                    this.transferService.deleteTransfer(Integer.parseInt(String.valueOf(e)))
            );
        } catch (Exception e) {
            return ErrorMsg("3000", e.getMessage());
        }
        return SuccessMsg("1000", "删除成功");
    }

    @RequestMapping(value = "/getAllTransfer", produces = "text/plain;charset=UTF-8")
    public String getAllTransfer() {
        List<Map> mapList = this.transferService.getAllTransfer();
        return SuccessMsg("1000", mapList);
    }

}
