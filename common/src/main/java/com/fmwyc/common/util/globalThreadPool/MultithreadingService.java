package com.fmwyc.common.util.globalThreadPool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class MultithreadingService {

    @Async("new_Thread")
    public Future<String> testService01() {
        try {
            System.out.println("线程名称 " + Thread.currentThread().getName());
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("test01执行完毕！");
    }

    @Async("new_Thread")
    public Future<String> testService02() {
        try {
            System.out.println("线程名称 " + Thread.currentThread().getName());
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("test02执行完毕！");
    }

    @Async("new_Thread")
    public Future<String> testService03() {
        try {
            System.out.println("线程名称 " + Thread.currentThread().getName());
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("test03执行完毕！");
    }

}
