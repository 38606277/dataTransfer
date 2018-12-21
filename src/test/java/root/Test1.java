package root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.context.SpringContextUtils;
import root.configuration.ErpUtil;
import root.configuration.SpringApplicationContextUtil;
import root.etl.Service.IEtlJobExecuteService;
import root.etl.Service.IEtlJobService;
import root.etl.entity.EtlJob;
import root.etl.entity.EtlJobExecute;
import root.etl.impl.EtlJobExecuteServiceImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @Auther: pccw
 * @Date: 2018/12/13 11:48
 * @Description:
 */
@RunWith(SpringRunner.class)    // 环境依赖
@SpringBootTest
public class Test1 {

   /* @Autowired
    IEtlJobExecuteService etlJobExecuteService;*/



   /* // 测试添加
    @Test
    public void testDate(){
        System.out.println("开始添加");
        etlJobExecuteService.addEtlJobExecuteForBegin(52);
        System.out.println("添加成功");
    }

    // 测试 修改进度
    @Test
    public void testUpdate(){
        System.out.println("开始修改");
        etlJobExecuteService.updateEtlJobExecuteForProcess(51,new BigDecimal("12.33"));
        System.out.println("修改成功");
    }

    // 测试 修改进度
    @Test
    public void testGet(){
        System.out.println("开始搜索");
        EtlJobExecute etlJobExecute = etlJobExecuteService.getEtlJobExecuteById(52);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(etlJobExecute.getJob_begin_date()));
        System.out.println("搜索成功");
    }*/

    // EtlJobExecuteServiceImpl etlJobExecuteServiceImpl = SpringApplicationContextUtil.getBeanByClass(EtlJobExecuteServiceImpl.class);

    //  updateEtlJobExecuteForProcess
    // 测试并发修改
    @Test
    public void testA() throws InterruptedException {
        System.out.println("开始测试");
       //  System.out.println(etlJobExecuteServiceImpl.toString());



        FactorialThread1 factorialThread1 = new FactorialThread1();
        factorialThread1.start();

        FactorialThread3 factorialThread3 = new FactorialThread3();
        factorialThread3.start();


        FactorialThread4 factorialThread4 = new FactorialThread4();
        factorialThread4.start();

        FactorialThread2 factorialThread2 = new FactorialThread2();
        factorialThread2.start();

        // 先终止掉 2
        factorialThread2.sleep(1000);
        factorialThread2.interrupt();


        // 终止掉线程 ： 先让线程进入休眠状态，然后他就可以被判定为可以终止的了，就可以调用 interrupt了
        factorialThread1.sleep(5000);
        factorialThread1.interrupt();  // 终止掉线程
    }


    class FactorialThread1 extends Thread{
        public void run() {
            try {
                currentThread().sleep(2000);   // 让当前线程先睡眠10秒 ，看线程2能否抢先执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1开始修改数据");
           /* EtlJobExecuteServiceImpl etlJobExecuteService =
                    SpringApplicationContextUtil.getBeanByClass(EtlJobExecuteServiceImpl.class);*/
            IEtlJobExecuteService etlJobExecuteService =
                    (IEtlJobExecuteService)SpringApplicationContextUtil.getBean("etlJobExecuteServiceImpl");
            etlJobExecuteService.updateEtlJobExecuteForProcess(52,new BigDecimal("54.44"));
            System.out.println("线程1修改数据结束");
        }
    }

    class FactorialThread2 extends Thread{
        public void run() {
            System.out.println("线程2开始修改数据");
            IEtlJobExecuteService etlJobExecuteService =
                    (IEtlJobExecuteService)SpringApplicationContextUtil.getBean("etlJobExecuteServiceImpl");
            etlJobExecuteService.updateEtlJobExecuteForProcess(52,new BigDecimal("84.44"));
            System.out.println("线程2修改数据结束");
        }
    }



    class FactorialThread3 extends Thread{
        public void run() {
            System.out.println("线程3开始修改数据");
            IEtlJobExecuteService etlJobExecuteService =
                    (IEtlJobExecuteService)SpringApplicationContextUtil.getBean("etlJobExecuteServiceImpl");
            etlJobExecuteService.updateEtlJobExecuteForProcess(52,new BigDecimal("64.44"));
            System.out.println("线程3修改数据结束");
        }
    }


    class FactorialThread4 extends Thread{
        public void run() {
            System.out.println("线程4开始修改数据");
            IEtlJobExecuteService etlJobExecuteService =
                    (IEtlJobExecuteService)SpringApplicationContextUtil.getBean("etlJobExecuteServiceImpl");
            etlJobExecuteService.updateEtlJobExecuteForProcess(52,new BigDecimal("54.44"));
            System.out.println("线程4修改数据结束");
        }
    }

    // 测试 double bigDecimal
    @Test
    public void testDouble(){
        int countSize = 8;
        BigDecimal everyProcess = new BigDecimal(Double.toString(Double.valueOf("1.00")/countSize)).setScale(2, BigDecimal.ROUND_DOWN);
        System.out.println(everyProcess);
    }

    @Autowired
    IEtlJobService etlJobService;

    // getEtlJobByNameAndGroup
    @Test
    public void getEtlJobByNameAndGroup(){
        System.out.println("开始查询");
        EtlJob etlJob = this.etlJobService.getEtlJobByNameAndGroup("1101","2018-12-4");
        System.out.println(etlJob.getId());
        System.out.println(etlJob.toString());
    }

    private final BigDecimal finalProcess = new BigDecimal("100.00");  // 制定 完成之后的任务进度为100.00

    @Test
    public void testBigDecimal(){
        System.out.println("开始执行");
        System.out.println(finalProcess);
    }

    // j42N1qJTUoLGQPRVyCNkVA==
    @Test
    public void testDecode() throws Exception {
        ErpUtil erpUtil = new ErpUtil();
        String s = erpUtil.decode("j42N1qJTUoLGQPRVyCNkVA==");
        System.out.println(s);
        System.out.println("====================");
        // tbmPrd@1318
    }

    @Test
    public void testCode() throws Exception {
        ErpUtil erpUtil = new ErpUtil();
        String s = erpUtil.encode("tbmPrd@1318");
        System.out.println(s);
        System.out.println("====================");
        // tbmPrd@1318
        //h5mmVLEcqGiBbLJd/b5WgQ==
    }

}
