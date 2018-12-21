package root.configuration;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Auther: lxf
 * @Date: 2018/12/11 11:52
 * @Description:
 *  指定 【source 、target 、自己业务库】 库的数据库连接 、事务
 */
// @Configuration
public class TransferDataSourceConfig {

   /* // 源头库
    @Bean(name = "sourceDataSource")
    public DataSource sourceDataSource() throws Exception {
        // 手动构建  source 的数据源
        JSONObject dbJson = JSONObject.parseObject(DbManager.getDBConnectionByName(DbManager.DB_NAME_SOURCE));
        if (dbJson.size() == 0) {
            throw  new Exception("源数据库解析异常");
        }
        return DbManager.bulidDataSource(dbJson);   // 不需要 init 了，springboot帮我们初始化到IOC容器中了
    }

    // @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
    @Primary  // primary 默认值，避免注入重复错误
    @Bean(name = "sourceTransactionManager")
    public DataSourceTransactionManager sourceTransactionManager(@Qualifier("sourceDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 目标库
    @Bean(name = "targetDataSource")
    public DataSource targetDataSource() throws Exception {
        // 手动构建  source 的数据源
        JSONObject dbJson = JSONObject.parseObject(DbManager.getDBConnectionByName(DbManager.DB_NAME_TARGET));
        if (dbJson.size() == 0) {
            throw  new Exception("源数据库解析异常");
        }
        return DbManager.bulidDataSource(dbJson);   // 不需要 init 了，springboot帮我们初始化到IOC容器中了
    }

    @Bean(name = "targetTransactionManager")
    public DataSourceTransactionManager targetTransactionManager(@Qualifier("targetDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/
}
