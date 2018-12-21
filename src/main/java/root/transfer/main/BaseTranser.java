package root.transfer.main;


import org.apache.log4j.Logger;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.transfer.pojo.CallBackInfo;
import root.transfer.pojo.Item;
import root.transfer.pojo.PreInfo;
import root.transfer.pojo.PreItem;
import root.transfer.util.DbHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;

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

    }

    private void executeWithItem(Item item,String year) throws Exception {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DbHelper.getConnection(item.getDbName());
            st = conn.createStatement();
            for (String sql : item.getSql()) {
                if(sql.contains("${budget_year}")){
                    sql = sql.replace("${budget_year}",year);
                    log.info("正在删除"+year+"年份的数据...");
                }
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
                st.execute(sql);
            }
        } finally {
            if (st != null)
                st.close();
            if (conn != null)
                conn.close();
        }
    }
}
