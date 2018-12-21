package root.configuration;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Auther: pccw
 * @Date: 2018/12/13 18:23
 * @Description:
 */
@Component
public class SpringApplicationContextUtil implements ApplicationContextAware {

    private static Logger logger = Logger.getLogger(SpringApplicationContextUtil.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("系统中存在的bean");
        Arrays.asList(applicationContext.getBeanDefinitionNames()).forEach(
                e-> System.out.println(e.toString()+e.getClass())
        );
        System.out.println("系统中存在的bean==========");
        this.context = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        try {
            return (T) context.getBean(beanName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error("SpringContextUtil" + e);
        }
        return (T) context.getBean(beanName);
    }

    public static  <T> T getBeanByClass(Class<T> clazz) {
        T obj = null;
        try {
            obj = context.getBean(clazz);
            if(obj!=null) System.out.println("得到bean了");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.error("SpringContextUtil" + e);
        }
        return obj;
       //  return (T) context.getBean(clazz);
    }

    public String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

}
