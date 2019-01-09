package root.etl.Util;

/**
 * @Auther: pccw
 * @Date: 2018/11/27 11:22
 * @Description:
 */
public class Constant {

    /**
     * 定时任务状态
     */
    public static class JOB_STATE {
        /**
         * 启用; etl_job_execute 当中表示 成功
         */
        public static int YES = 1;
        /**
         * 停用; etl_job_execute 当中表示 成功失败
         */
        public static int NO = 0;
        /**
         * 任务执行中 （正在执行状态：etl_job_execute 用到）
         */
        public static int STARTING = 2;
    }

    /**
     * 系统响应编码
     */
    public static class SYSCODE {
        /**
         * 成功
         */
        public static String SUCCESS = "200";
        /**
         * 失败
         */
        public static String FAIL = "500";
        /**
         * 校验不通过
         */
        public static String CHECKFIAL = "204";
    }

    /**
     * 响应结果
     */
    public static class RETURN_STATE {
        /**
         * 成功
         */
        public static String SUCC = "1";
        /**
         * 失败
         */
        public static String FAIL = "0";
    }

    public static class TASK_CLASS {

        // 解决移动历史数据版本的 导库任务
        public static String TRANSFER_VALUE = "1";

        public static String TRANSFER_TASK_CLASS_PATH = "root.job.task.TransferTask";

        // 默认的 导库任务
        public static String DEFAULT_VALUE = "2";

        public static String DEFAULT_TASK_CLASS_PATH = "root.job.task.DefaultTask";
    }
}
