package root.transfer.main;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Util.Constant;
import root.job.service.JobExecuteService;
import root.transfer.pojo.SrcInfo;
import root.transfer.pojo.TargetInfo;
import root.transfer.pojo.TransferInfo;
import root.transfer.util.DataConvert;
import root.transfer.util.DbHelper;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransferWithMultiThreadForMemory extends BaseTranser {

    private static final Logger log = Logger.getLogger(TransferWithMultiThreadForMemory.class);

    public void transfer(TransferInfo t, int job_execute_id, JobExecuteService jobExecuteService, int year,int month, boolean bool) {
        long time = System.currentTimeMillis();
        TargetInfo target = t.getTargetInfo();
        SrcInfo src = t.getSrcInfo();
        int count = 0;
        try {
            new DbHelper().executeQuery(t.getSrcInfo(), rs -> {
                if(bool){
                    DbHelper.createTableInDB(rs, t,year);  // 不用创建表了，已经创建了
                }
                ExecutorService service = Executors.newFixedThreadPool(InsertTask.queueSize);
                try {
                    if (rs != null) {

                        // 初始化全局变量
                        List<List<Object>> list = new ArrayList<>();
                        // IEtlJobExecuteService etlJobExecuteService = null;

                        // 1. 计算所需要的次数 ： rs 移动到最后面 ，确认总条数 ,  rs再移动回去，
                        rs.last();
                        if(year>0 && month>0){
                            log.info("特别提醒*******************"+year+"年"+month+"月"+"需要导入的数据行数是"+ rs.getRow()+"**************");
                        }

                        // 往 全局的 processMap 当中写
                        int countAll = rs.getRow();
                        Constant.PROCESSMAP.put(job_execute_id+Constant.TOTAL, countAll);   // 总数

                        BigDecimal result = new BigDecimal((double) rs.getRow()/5000).setScale(0, BigDecimal.ROUND_UP);  // 向上取整
                        int countSize = result.intValue();   // 所需要的总次数
                        log.info("此处导库总需线程调用次数为"+countSize);
                        // 向下取整 得到每一次能完成的进度
                        final BigDecimal everyProcess = new BigDecimal(Double.toString(Double.valueOf("1.00")/countSize)).setScale(2, BigDecimal.ROUND_DOWN);

                        rs.first();  // 坑处1，如果使用了firt的话，那么直接达到了第一条，则不要使用 while(rs.next())

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int cc = rsmd.getColumnCount();
                        String insertSql = DbHelper.getInserSql(target, rsmd);

                        if(rs!=null){

                            List<Object> firstElements  = new ArrayList<>(cc);
                            list.add(firstElements);
                            for (int j = 1; j <= cc; j++) {
                                firstElements.add(DataConvert.getVal(rs.getObject(j)));  // 我们需要吧第一次的数据再次添加回去
                            }
                        }

                        // 2. 执行 5000 行一次的操作
                        while (rs.next()) {
                            List<Object> elements = new ArrayList<>(cc);
                            list.add(elements);
                            for (int i = 1; i <= cc; i++) {
                                elements.add(DataConvert.getVal(rs.getObject(i)));
                            }
                            if (rs.getRow() % 5000 == 0) {
                                service.submit(new InsertTaskForMemory(target.getDbName(), insertSql, list,everyProcess,jobExecuteService,job_execute_id));
                                log.info("提交了" + rs.getRow() + "行");
                                if(Constant.PROCESSMAP.get(job_execute_id+Constant.DONE) == null){
                                    Constant.PROCESSMAP.put(job_execute_id+Constant.DONE,rs.getRow());   // 已经写入数
                                }else {
                                    // 累加
                                    int tempRow =  Constant.PROCESSMAP.get(job_execute_id+Constant.DONE);
                                    tempRow += rs.getRow();
                                    Constant.PROCESSMAP.put(job_execute_id+Constant.DONE,tempRow);
                                }

                                list = new ArrayList<>();
                            }
                        }
                        // 把 剩余的不足5000 行的继续写一次
                        service.submit(new InsertTaskForMemory(target.getDbName(), insertSql, list,everyProcess,jobExecuteService,job_execute_id));
                        Constant.PROCESSMAP.put(job_execute_id+Constant.DONE,countAll);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    service.shutdown();
                }
            });
        } catch (Exception e) {
            log.error("提取过程发生异常", e);
        }
        log.info("【extract】抽取表:" + src.getTable() != null ? src.getTable() : src.getSql() + "耗时：" + (System.currentTimeMillis() - time) + "毫秒");
    }
}
