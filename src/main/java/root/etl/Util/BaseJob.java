package root.etl.Util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 10:46
 * @Description: 编写 BaseJob 接口
 */
public interface BaseJob extends Job {

    //当Scheduler调用一个Job，就会将JobExecutionContext传递给Job的execute()方法；
    //Job能通过JobExecutionContext对象访问到Quartz运行时候的环境以及Job本身的明细数据
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException;

}
