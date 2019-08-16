package com.eugene.sumarry.sbrabbitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * 模拟开启500个线程统一调用抢单逻辑
 */
@Service
public class InitService {
    private static final Logger log = LoggerFactory.getLogger(InitService.class);

    public static final int THREAD_NUM = 500;

    private static int mobile = 0;

    @Autowired
    private ConcurrencyService concurrencyService;


    public void generateMultiThread() {
        log.info("开始生成500个线程");

        // 默认为1个线程
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < THREAD_NUM ; i++) {
            new Thread(new RunThread(countDownLatch)).start();
        }

        countDownLatch.countDown();
    }


    private class RunThread implements Runnable {

        private final CountDownLatch startLatch;

        public RunThread(CountDownLatch startLatch) {
            this.startLatch = startLatch;
        }

        @Override
        public void run() {
            try {
                //
                startLatch.await();

                mobile++;

                // 抢单逻辑
                concurrencyService.manageRobbing(String.valueOf(mobile));
            } catch (InterruptedException e) {
                log.error("线程运行发生异常: ", e);
                e.printStackTrace();
            }
        }
    }

}
