package root.job.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.configuration.RO;
import root.etl.Service.ITransferService;
import root.job.service.TransferService;

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
@RequestMapping("transfer/sql")
public class TransferSQLControl extends RO {

    @Autowired
    TransferService transferService;

    @RequestMapping(value = "/createTransfer", produces = "text/plain;charset=UTF-8")
    public String createATransfer(@RequestBody String pJson) throws SQLException {

        JSONObject jsonObject = JSON.parseObject(pJson);
        String transfer_content = jsonObject.getString("transfer_content");
        String transfer_name =  jsonObject.getString("transfer_name");
        try {
            Map map = new HashMap();
            map.put("transfer_name",transfer_name);
            map.put("transfer_content",transfer_content);
            this.transferService.addTransferSQL(map);
        } catch (Exception e) {
            return ErrorMsg("3000", e.getMessage());
        }
        return SuccessMsg("1000", "新增成功");
    }

    @RequestMapping(value = "/updateTransfer", produces = "text/plain;charset=UTF-8")
    public String updateTransfer(@RequestBody String pJson) throws SQLException {

        JSONObject jsonObject = JSON.parseObject(pJson);
        int transfer_id = jsonObject.getIntValue("transfer_id");
        String transfer_name =  jsonObject.getString("transfer_name");
        String transfer_content = jsonObject.getString("transfer_content");

        Map<String, Object> map = new HashMap<>();
        map.put("transfer_id", transfer_id);
        map.put("transfer_content", transfer_content);
        map.put("transfer_name",transfer_name);
        try {
            // 修改一条记录
            this.transferService.updateTransfer(map);
        } catch (Exception e) {
            return ErrorMsg("3000", e.getMessage());
        }
        return SuccessMsg("1000", "修改成功");
    }


    @RequestMapping(value = "/deleteTransfer", produces = "text/plain;charset=UTF-8")
    public String deleteTransfer(@RequestBody String pJson) throws SQLException {
        JSONArray jsonArray = JSON.parseArray(pJson);
        try {
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
