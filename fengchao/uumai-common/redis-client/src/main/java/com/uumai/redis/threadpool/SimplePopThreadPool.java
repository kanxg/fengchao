package com.uumai.redis.threadpool;

/**
 * Created by kanxg on 14-11-25.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePopThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            Runnable worker = new PopThread("popThread" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all Pop threads");
    }

}
