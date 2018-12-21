package root.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.web.bind.annotation.RequestBody;
import root.transfer.util.XmlUtil;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.FileInputStream;

/**
 * @Auther: pccw
 * @Date: 2018/12/11 12:00
 * @Description:
 */
public class DbManager {

    private static final Logger logger = Logger.getLogger(DbManager.class);

    // 导库依赖的 配置文件
    private static final String DB_CONFIG_PATH = System.getProperty("user.dir") + "/config/DBConfig.xml";
    public static final String DB_NAME_SOURCE = "source";   // 指定源数据库名称为 source
    public static final String DB_NAME_TARGET = "target";   // 指定目标数据源名称为 target
    public static final String DB_NAME_LOCAL = "form";   // 指定自己的系统数据库 form
    public static final String DB_TYPE_MYSQL = "Mysql";
    public static final String DB_TYPE_ORACLE = "Oracle";


    public static String getDBConnectionByName(String name) {
        JSONObject obj = new JSONObject(true);
        Document dom = null;
        try {
            dom = XmlUtil.parseXmlToDom(new FileInputStream(DB_CONFIG_PATH));
        } catch (Exception e) {
            logger.error("解析DBConfig.xml异常!");
            e.printStackTrace();
        }
        Node node = dom.selectSingleNode("/DBConnection/DB[name='" + name + "']");
        if (node != null) {
            obj.put("name", node.selectSingleNode("name").getText());
            obj.put("driver", node.selectSingleNode("driver").getText());
            obj.put("dbtype", node.selectSingleNode("dbtype").getText());
            obj.put("url", node.selectSingleNode("url").getText());
            obj.put("username", node.selectSingleNode("username").getText());
            obj.put("password", node.selectSingleNode("password").getText());
            obj.put("maxPoolSize", node.selectSingleNode("maxPoolSize").getText());
            obj.put("minPoolSize", node.selectSingleNode("minPoolSize").getText());
        }

        return obj.toJSONString();
    }

    public static DataSource bulidDataSource(JSONObject dbJson) throws Exception {
        ErpUtil erpUtil = new ErpUtil();
        String dbtype = dbJson.getString("dbtype");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dbJson.getString("username"));
        dataSource.setPassword(erpUtil.decode(dbJson.getString("password")));
        dataSource.setDriverClassName(dbJson.getString("driver"));
        if ("Mysql".equals(dbtype)) {
            dataSource.setUrl(dbJson.getString("url") + "?serverTimezone=UTC&useSSL=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true");
        } else {
            dataSource.setUrl(dbJson.getString("url"));
            if("DB2".equals(dbtype)){
                dataSource.setQueryTimeout(180);   // 如果是远程的DB2 数据库，查询的时候设置180s 也就是3分钟的查询超时时间
            }
        }
        dataSource.setMaxWait(10000);//设置连接超时时间10秒
        dataSource.setMaxActive(Integer.valueOf(dbJson.getString("maxPoolSize")));
        dataSource.setInitialSize(Integer.valueOf(dbJson.getString("minPoolSize")));
        dataSource.setTimeBetweenEvictionRunsMillis(60000);//检测数据源空连接间隔时间
        dataSource.setMinEvictableIdleTimeMillis(300000);//连接空闲时间
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }
}
