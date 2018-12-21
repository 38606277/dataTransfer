package root.transfer.main;

import org.apache.log4j.Logger;
import root.etl.Service.IEtlJobExecuteService;
import root.transfer.util.DbHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InsertTask implements Runnable {

    private static final Logger log = Logger.getLogger(InsertTask.class);  //

    public static final int queueSize = 5;
    public static final int maxConnectionSize = 20;
    public static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(maxConnectionSize);
    private List<List<Object>> list;
    private Connection conn;
    private String sql;

    private BigDecimal oneProcess;    // 一次执行的进度
    private IEtlJobExecuteService etlJobExecuteService;  // 业务类
    private int job_id;

    public InsertTask(String dbName, String sql, List<List<Object>> list, BigDecimal oneProcess,IEtlJobExecuteService etlJobExecuteService,int job_id) throws Exception {
        super();
        queue.put(0);
        this.conn = DbHelper.getConnection(dbName);
        this.conn.setAutoCommit(Boolean.FALSE);
        this.sql = sql;
        this.list = list;

        this.oneProcess = oneProcess;
        this.etlJobExecuteService = etlJobExecuteService;
        this.job_id = job_id;
    }

    @Override
    public void run() {
        try (PreparedStatement ps = conn.prepareStatement(this.sql)) {
            //从队列中取出list数据
            if (list != null) {
                for (List<Object> elements : list) {
                    for (int i = 0; i < elements.size(); i++) {
                        ps.setObject(i + 1, elements.get(i));
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                this.conn.commit();
                this.list = null;
                queue.take();

                // 对本地的 mysql 当中的 etl_job_execute 更新 process
                if(this.etlJobExecuteService != null && this.oneProcess!=null){
                    // 下面调用的这个方法是同步的
                   this.etlJobExecuteService.updateEtlJobExecuteForProcess(job_id,oneProcess);  // 累加进度
                } else{
                    log.error("无法得到etlJobExecuteService对象,");
                }
            }
        } catch (Exception e) {
            log.error("数据插入异常:", e);
            throw new RuntimeException(e);
        } finally {
            this.closeConnection();
        }
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
