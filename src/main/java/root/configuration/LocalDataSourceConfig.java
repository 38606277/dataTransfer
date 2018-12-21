package root.configuration;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Auther: lxf
 * @Date: 2018/12/11 14:13
 * @Description:
 *      指定数据源 -》 从指定的文件当中读取 数据库信息 ：注意加密解密算法
 */
@Configuration
public class LocalDataSourceConfig {

    @Bean(name = "localDataSource")
    public DataSource localDataSource() throws Exception {
        // 手动构建  source 的数据源
        JSONObject dbJson = JSONObject.parseObject(DbManager.getDBConnectionByName(DbManager.DB_NAME_LOCAL));
        if (dbJson.size() == 0) {
            throw  new Exception("源数据库解析异常");
        }
        return DbManager.bulidDataSource(dbJson);   // 不需要 init 了，springboot帮我们初始化到IOC容器中了
    }

    @Bean(name = "localTransactionManager")
    public DataSourceTransactionManager localTransactionManager(@Qualifier("localDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
