package root.transfer.main;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.etl.Util.Constant;
import root.job.service.JobExecuteService;
import root.transfer.pojo.CallBackInfo;
import root.transfer.pojo.Item;
import root.transfer.pojo.PreInfo;
import root.transfer.pojo.PreItem;
import root.transfer.util.DbHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

public class BaseTranser {
    private static final Logger log = Logger.getLogger(BaseTranser.class);

    // 导库前执行的sql
    public void executePreInfo(PreInfo preInfo){
        if(preInfo == null && preInfo.getItem()==null) return;
        log.info("开始执行导库前sql...");
        try {
            for(PreItem preItem : preInfo.getItem()){
                this.executeWithPreItem(preItem);
            }
        }catch (Exception e) {
            log.warn("当前操作无需删除此表");
        }
    }


    // 回调执行一些sql
    public void executeCallBack(CallBackInfo call,int job_id,IEtlJobExecuteService etlJobExecuteService,String year) throws Exception {
        if (call == null || call.getItem() == null) return;
        log.info("开始执行回调sql...");
        for (Item item : call.getItem()) {
            this.executeWithItem(item,year);
        }

        // 回调执行完毕之后 , 更改掉 指定  JOB  的 process 为 100
        etlJobExecuteService.updateEtlJobExecuteToEnd(job_id);
        log.info("回调SQL执行完毕");

    }

    // 回调执行一些sql
    public void executeCallBack(CallBackInfo call,JobExecuteService jobExecuteService,String year,String month,int job_execute_id) throws Exception {
        if (call == null || call.getItem() == null) return;
        log.info("开始执行回调sql...");

        // 对 year 跟month 判断
        if(StringUtils.isNotBlank(year)  &&  StringUtils.isNotBlank(month)){
            for (Item item : call.getItem()) {
                this.executeWithItem(item,year,month);
            }
        }else {
            for (Item item : call.getItem()) {
                this.executeWithItem(item,null);
            }
        }

        // 回调执行完毕之后 , 更改掉 指定  JOB  的 process 为 100
        Map map = jobExecuteService.getJobExecuteById(job_execute_id);
        map.put("job_process",new BigDecimal("100.00"));
        map.put("end_time",new Date());
        map.put("job_status",Constant.JOB_STATE.YES);
        jobExecuteService.updateEtlJobExecute(map);

        log.info("回调SQL执行完毕");

    }

    private void executeWithItem(Item item,String year) throws Exception {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DbHelper.getConnection(item.getDbName());
            st = conn.createStatement();

            if(StringUtils.isNotBlank(year)){
                for (String sql : item.getSql()) {
                    if(sql.contains("${budget_year}")){
                        sql = sql.replace("${budget_year}",year);
                        log.info("正在删除"+year+"年份的数据...");
                    }
                    log.info("回调SQL为:"+sql);
                    st.execute(sql);
                }
            }else {
                for (String sql : item.getSql()) {
                    log.info("回调SQL为:"+sql);
                    st.execute(sql);
                }
            }


        } finally {
            if (st != null)
                st.close();
            if (conn != null)
                conn.close();
        }
    }

    private void executeWithItem(Item item,String year,String month) throws Exception {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DbHelper.getConnection(item.getDbName());
            st = conn.createStatement();
            for (String sql : item.getSql()) {
                if(sql.contains("${budget_year}")){
                    sql = sql.replace("${budget_year}",year);
                    sql = sql.replace("${budget_month}",month);
                    log.info("正在删除"+year+"年份的数据...");
                }
                log.info("回调SQL为:"+sql);
                st.execute(sql);
            }
        } finally {
            if (st != null)
                st.close();
            if (conn != null)
                conn.close();
        }
    }

    private void executeWithPreItem(PreItem preItem) throws Exception {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DbHelper.getConnection(preItem.getDbName());
            st = conn.createStatement();

            for (String sql : preItem.getSql()) {
                if(StringUtils.isNotBlank(sql)){
                    log.info("执行SQL为:"+sql);
                    st.execute(sql);
                }
            }
        } finally {
            log.info("导库前SQL执行完毕");
            if (st != null)
                st.close();
            if (conn != null)
                conn.close();
        }
    }
}
