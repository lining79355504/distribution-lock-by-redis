package com.test;

import com.open.mort.LockServiceUtils;
import com.open.mort.LockUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Author:  lining17
 * Date :  26/04/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:conf/spring-core.xml"})
public class LockUtilsTest {


    private static final Logger logger = LoggerFactory.getLogger(LockUtilsTest.class);


    @Test
    public void lockTest(){

        ExecutorService executorService = Executors.newFixedThreadPool(4000);
        List<FutureTask<Boolean>> taskList = new ArrayList<FutureTask<Boolean>>();
        for (int i = 0; i < 1000; i++) {

            Callable<Boolean> callable = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean ret = LockUtils.getLock("174874816" , 4000,8000);
                    logger.info("LockUtils.getLock {}" , ret);
                    Thread.sleep(20);
                    if(ret) {
                        boolean retRelease = LockUtils.releaseLock("174874816");
                        logger.info("LockUtils.releaseLock {}", retRelease);
                    }
                    return ret;
                }
            };
            FutureTask<Boolean> task = new FutureTask<Boolean>(callable);
            executorService.submit(task);
            taskList.add(task);
        }

        for (FutureTask<Boolean> item :taskList) {
            try {
                Boolean ret = item.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        executorService.isTerminated();

    }




    @Test
    public void lockServiceTest(){

        ExecutorService executorService = Executors.newFixedThreadPool(4000);
        List<FutureTask<Boolean>> taskList = new ArrayList<FutureTask<Boolean>>();
        for (int i = 0; i < 30000; i++) {

            Callable<Boolean> callable = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean ret = LockServiceUtils.getLock("174874816" , 4000,8000);
                    logger.info("LockServiceUtils.getLock {}" , ret);
                    Thread.sleep(20);
                    if(ret) {
                        boolean retRelease = LockServiceUtils.releaseLock("174874816");
                        logger.info("LockServiceUtils.releaseLock {}", retRelease);
                    }
                    return ret;
                }
            };
            FutureTask<Boolean> task = new FutureTask<Boolean>(callable);
            executorService.submit(task);
            taskList.add(task);
        }

        for (FutureTask<Boolean> item :taskList) {
            try {
                Boolean ret = item.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        executorService.isTerminated();

    }
}
