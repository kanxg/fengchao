package com.uumai.redis.threadpool;

/**
 * Created by kanxg on 14-11-25.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.uumai.redis.Testpojo;

public class SimplePushThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
        	Testpojo pojo=new Testpojo();
        	pojo.setName("pojoname"+i);
            Runnable worker = new PushThread(pojo);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all Push threads");
    }

}
