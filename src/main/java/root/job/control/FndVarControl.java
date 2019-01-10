package root.job.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import root.common.RO;
import root.job.service.FndVarService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2019/1/10 10:38
 * @Description:
 */
@RestController
@RequestMapping("transfer/globalVar")
public class FndVarControl extends RO {

    @Autowired
    FndVarService fndVarService;

    @RequestMapping(value = "/createFndVar", produces = "text/plain;charset=UTF-8")
    @Transactional
    public String createFndVar(@RequestBody String pJson)  {

        // 1. 解析参数
        JSONObject jsonObject = JSON.parseObject(pJson);
        String var_name = jsonObject.getString("var_name");
        String var_define =  jsonObject.getString("var_define");
        String var_type =  jsonObject.getString("var_type");
        Map map = new HashMap<String,String>();
        map.put("var_name",var_name);
        map.put("var_define",var_define);
        map.put("var_type",var_type);

        // 2. 插入到数据库当中去
        try{
            this.fndVarService.addFndVar(map);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorMsg("3000","数据库插入异常");
        }

        return SuccessMsg("1000","");
    }


    @RequestMapping(value = "/updateFndVar", produces = "text/plain;charset=UTF-8")
    @Transactional
    public String updateFndVar(@RequestBody String pJson)  {

        // 1. 解析参数
        JSONObject jsonObject = JSON.parseObject(pJson);
        String var_name = jsonObject.getString("var_name");
        String var_define =  jsonObject.getString("var_define");
        String var_type =  jsonObject.getString("var_type");
        Map map = new HashMap<String,String>();
        map.put("var_name",var_name);
        map.put("var_define",var_define);
        map.put("var_type",var_type);

        // 2. 修改到数据库当中去
        try{
            this.fndVarService.updateFndVar(map);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorMsg("3000","数据库修改异常");
        }

        return SuccessMsg("1000","");
    }

    @RequestMapping(value = "/deleteFndVar", produces = "text/plain;charset=UTF-8")
    @Transactional
    public String deleteFndVar(@RequestBody String pJson)  {

        // 1. 解析参数
        JSONObject jsonObject = JSON.parseObject(pJson);
        String var_name = jsonObject.getString("var_name");
        Map map = new HashMap<String,String>();
        map.put("var_name",var_name);

        // 2. 从数据库当中删除
        try{
            this.fndVarService.deleteFndVar(map);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorMsg("3000","数据库删除异常");
        }

        return SuccessMsg("1000","");
    }

    @RequestMapping(value = "/getFndVarByName", produces = "text/plain;charset=UTF-8")
    public String getFndVarByName(@RequestBody String pJson)  {

        // 1. 解析参数
        JSONObject jsonObject = JSON.parseObject(pJson);
        String var_name = jsonObject.getString("var_name");
        Map map = new HashMap<String,String>();
        map.put("var_name",var_name);

        // 2. 从数据库当中删除
        Map resultMap = this.fndVarService.getFndVarByName(map);
        return SuccessMsg("1000",resultMap);
    }

    @RequestMapping(value = "/getAllFndVar", produces = "text/plain;charset=UTF-8")
    public String getAllFndVar(@RequestBody String pJson)  {

        // 1. 解析参数
        JSONObject jsonObject = JSON.parseObject(pJson);

        // TODO ：  分页参数

        // 2. 从数据库当中删除
        List<Map> mapList = this.fndVarService.getAllFndVar();
        return SuccessMsg("1000",mapList);
    }

}

