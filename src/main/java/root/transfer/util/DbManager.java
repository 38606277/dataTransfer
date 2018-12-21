package root.transfer.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbManager {
    @SuppressWarnings("unchecked")
    public static Map<String, String> getDBConnectionByName(String dbName) {
        Document dom = null;
        try {
            dom = XmlUtil.parseXmlToDom(new FileInputStream("config/DBConfig.xml"));
        } catch (Exception e) {
            throw new RuntimeException("解析DBConfig发生异常", e);
        }
        List<Element> propList = dom.selectNodes("/DBConnection/DB[name='" + dbName + "']/*");
        Map<String, String> retMap = new HashMap<String, String>();
        if (propList != null) {
            for (Element element : propList) {
                retMap.put(element.getName(), element.getStringValue());
            }
        }
        return retMap;
    }
}
