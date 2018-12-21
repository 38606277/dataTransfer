package root;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableTransactionManagement  // 允许开始事务管理
// @MapperScan("root.quartz.mapper")
public class DataTransferApp {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(DataTransferApp.class);
    }
}
