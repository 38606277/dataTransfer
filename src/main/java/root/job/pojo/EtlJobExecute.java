package root.job.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: pccw
 * @Date: 2018/12/13 12:11
 * @Description:
 */
public class EtlJobExecute implements Serializable {

    private static final long serialVersionUID = 1L;    // 要把实体类定义成能够序列化的

    private  int job_id;               // Jon_id

    // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  job_begin_date;       // 任务开始执行时间
    private Date  job_end_date;          // 任务执行结束时间

    private BigDecimal job_process_number;
    private int job_process_status;
    private String job_failure_reason;

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public Date getJob_begin_date() {
        return job_begin_date;
    }

    public void setJob_begin_date(Date job_begin_date) {
        this.job_begin_date = job_begin_date;
    }

    public Date getJob_end_date() {
        return job_end_date;
    }

    public void setJob_end_date(Date job_end_date) {
        this.job_end_date = job_end_date;
    }

    public BigDecimal getJob_process_number() {
        return job_process_number;
    }

    public void setJob_process_number(BigDecimal job_process_number) {
        this.job_process_number = job_process_number;
    }

    public int getJob_process_status() {
        return job_process_status;
    }

    public void setJob_process_status(int job_process_status) {
        this.job_process_status = job_process_status;
    }

    public String getJob_failure_reason() {
        return job_failure_reason;
    }

    public void setJob_failure_reason(String job_failure_reason) {
        this.job_failure_reason = job_failure_reason;
    }
}
