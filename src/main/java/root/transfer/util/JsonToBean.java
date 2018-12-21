package root.transfer.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import root.transfer.pojo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: pccw
 * @Date: 2018/12/3 10:34
 * @Description: 解析 JSON 字符串 绑定到对象上
 */
public class JsonToBean {

    /**
     * 功能描述:
     * 把 json 对象转换成 root 当中的各个属性值,
     * 其中 root 是单个， root当中的 transferInfo 是多个 ，  root当中的 callBack 是一个 ，
     * transferInfo 里面有一个 srcInfo 跟一个 targetInfo
     */
    public static Root transJsonToBean(JSONObject jsonObject) throws Exception {

        JSONArray jsonArray = jsonObject.getJSONArray("trans_info");
        Root root = null;
        try {
            List<TransferInfo> transferInfos = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                TransferInfo transferInfo = new TransferInfo();
                JSONObject json = jsonArray.getJSONObject(i);

                // 1. 组装 src对象
                SrcInfo srcInfo = new SrcInfo();
                srcInfo.setDbName(json.getString("src_db"));
                srcInfo.setSql(json.getString("src_sql"));
                transferInfo.setSrcInfo(srcInfo);

                // 2. 组装 target 对象
                TargetInfo targetInfo = new TargetInfo();
                targetInfo.setDbName(json.getString("target_db"));
                targetInfo.setTable(json.getString("target_table"));
                List list = new ArrayList();
                JSONArray sqlArray = json.getJSONArray("target_sql");
                List<String> strList = new ArrayList<>();
                sqlArray.forEach(e -> strList.add(String.valueOf(e)));
                targetInfo.setSql(strList);
                transferInfo.setTargetInfo(targetInfo);

                // 3. 组装到 集合中去
                transferInfos.add(transferInfo);

            }

            // 3. 组装 callback 对象
            CallBackInfo callBackInfo = new CallBackInfo();
            List<Item> itemList = new ArrayList<>();
            JSONArray itemJsonArray = jsonObject.getJSONArray("call_item");
            for (int j = 0; j < itemJsonArray.size(); j++) {
                Item item = new Item();
                JSONObject jsonObject1 = itemJsonArray.getJSONObject(j);
                item.setDbName(jsonObject1.getString("call_db"));

                JSONArray sqlArrayCall = jsonObject1.getJSONArray("call_sql");
                List<String> strListCall = new ArrayList<>();
                sqlArrayCall.forEach(e -> strListCall.add(String.valueOf(e)));
                item.setSql(strListCall);
                itemList.add(item);
            }
            callBackInfo.setItem(itemList);

            // 4. 组装成大对象
            root = new Root();
            root.setCallBackInfo(callBackInfo);
            root.setTransferInfo(transferInfos);
        } catch (Exception e) {
            throw new Exception("解析JSON数据异常");
        }
        return root;

    }

}
