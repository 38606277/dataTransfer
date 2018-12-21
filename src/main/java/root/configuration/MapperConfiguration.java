package root.configuration;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Auther: lxf
 * @Date: 2018/12/11 14:20
 * @Description:
 *      myabtis的配置管理类
 */
@Configuration
public class MapperConfiguration {

    @Autowired
    @Qualifier("localDataSource")
    private DataSource localDataSource;    // 拿到我们自己配置的数据源

    //配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper(){
        System.out.println("开始配置数据分页插件");
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("pageSizeZero","true");  // 我任然想查询出所有参数
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        //配置mysql数据库的方言
        properties.setProperty("dialect","mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }



    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(localDataSource);  // 指定 数据源

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4jImpl.class);    // 指定为log4j 日志
        configuration.setCallSettersOnNulls(true);
        // 注册 typeHandle 组件，对 Date类型进行处理
       //  configuration.getTypeHandlerRegistry().register(DateTypeHandle.class);

        factoryBean.setConfiguration(configuration);

        // 扫描 mapper.xml 文件
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().
                        getResources("classpath:mapper/*.xml"));

        // 指定 分页插件 plugin
        // factoryBean.setPlugins(getMybatisPlugins());

        return factoryBean.getObject();
    }

    private static Interceptor[] getMybatisPlugins() {
        Interceptor[] ins = new Interceptor[1];
        PageInterceptor pageInceptor = new PageInterceptor();
        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
     /*   properties.setProperty("helperDialect", "mysql");     // mysql 类型
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");*/
        properties.setProperty("pageSizeZero","true");  // 我任然想查询出所有参数
        properties.setProperty("rowBoundsWithCount","true");
        // properties.setProperty("params", "count=countSql");   // ???
        pageHelper.setProperties(properties);
        ins[0] = pageInceptor;
        return ins;
    }

}
