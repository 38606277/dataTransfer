package root.job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import root.job.service.FndVarService;
import root.transfer.util.DbHelper;
import root.transfer.util.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: pccw
 * @Date: 2019/1/10 14:32
 * @Description:
 *      提取系统变量
 */
@Component
public class GlodbalVar {

    private  final String TYPE_ONE = "sql";

    private final String TYPE_TWO = "java";

    private final String PREFIX = "${";   // 前缀

    private final String SUFFIX = "}";  // 后缀

    @Autowired
    FndVarService fndVarService;

    // 得到指定的系统变量的值
    public Object getSpecifiedValue(String name){

        Map map = new HashMap();
        map.put("var_name",name);
        Map resultMap = fndVarService.getFndVarByName(map);
        if(resultMap != null){
            String script = resultMap.get("var_define").toString();
            String type = resultMap.get("var_type").toString();
            if(StringUtils.isNotBlank(script) && StringUtils.isNotBlank(type)){
                if(this.TYPE_ONE.equals(type)){
                    // 数据库脚本
                    Connection conn = null;
                    PreparedStatement ps = null;
                    try{
                        conn = DbHelper.getConnection("form");
                        ps = conn.prepareStatement(script);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            return rs.getString(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    } finally {
                        try {
                            if (ps != null) ps.close();
                            if (conn != null) conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(this.TYPE_TWO.equals(type)){
                    // JAVA 脚本 TODO:反射调用方法
                }
            }
        }

        return null;
    }


    /**
     *
     * 功能描述:
     *      执行参数当中的脚本，组装key-value 返回出去
     * @param:
     * @return:
     * @auther: pccw
     * @date: 2019/1/10 16:22
     */
    private Map<String,Object> getSpecifiedValueByList(List<Map> mapParam){
        Map<String,Object> resultMap = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = DbHelper.getConnection("form");
            resultMap = new HashMap<>(mapParam.size());
            for(Map tempMap : mapParam) {
                String script = tempMap.get("var_define").toString();
                String type = tempMap.get("var_type").toString();
                if (StringUtils.isNotBlank(script) && StringUtils.isNotBlank(type)) {
                    if (this.TYPE_ONE.equals(type)) {
                        // 执行 数据库脚本函数
                        ps = conn.prepareStatement(script);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            resultMap.put(tempMap.get("var_name").toString(),rs.getString(1));
                        }
                    } else {
                        // JAVA 脚本 TODO:反射调用方法
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     *
     * 功能描述:
     *     传递一个数组,返回组装好的 要执行的 全局变量
     * @auther: pccw
     * @date: 2019/1/10 15:10
     */
    public Map<String,Object> findTransferVars(List<String> listParam){

        if(listParam == null || listParam.size()<1){
            return null;
        }

        Map<String,Object> resultMap = null;
        List<Map> transferMap = new ArrayList<>();

        // 1. 数据库查找出数组对应的值，  放到2个MAP 当中 一个是 数据库脚本 一个是 JAVA方法
       List<Map> mapList = this.fndVarService.getAllFndVar();
       for(String str : listParam){
           for(Map varMap : mapList){
               if(str.equals(varMap.get("var_name"))){
                   transferMap.add(varMap);
                   break;  // 找到了就跳出去
               }
           }
       }

       // 2. 调用组装方法
        resultMap = getSpecifiedValueByList(transferMap);
        return resultMap;
    }

    /**
     *
     * 功能描述:
     *      对SQL 进行 全局变量替换
     *      【规定】 ： 我们规定 在参数SQL 当中 需要替换的变量是【 ${变量名}  】 形式 (忽略大小写) ,
     *      【注意】 ： 我们不对参数进行类型判断，我们只替换
     * @param:
     * @return:
     * @auther: lxf
     * @date: 2019/1/10 16:22
     */
    public String replaceGlobalVar(Map<String,Object> map,String sql){

        if(StringUtils.isNotBlank(sql)){
            return null;
        }

        String tempParam = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
           //  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            tempParam = this.PREFIX+entry.getKey()+this.SUFFIX;
            sql = sql.replace(tempParam,String.valueOf(entry.getValue()));
        }
        return sql;
    }

    public String replaceGlobalVarForParam(List<String> params,String sql){

        Map<String,Object> map = findTransferVars(params);

        String tempParam = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            tempParam = this.PREFIX+entry.getKey()+this.SUFFIX;
            sql = sql.replace(tempParam,String.valueOf(entry.getValue()));
        }
        return sql;
    }

}
