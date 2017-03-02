package com.uumai.crawler3;

import com.uumai.resourcepool.ResourcePool;
import com.uumai.resourcepool.semaphore.SemaphoreResourcePool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/21/15
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleThreadPool {

    public static void main(String[] args) {

//        ThreadPoolExecutor(int corePoolSize,
//        int maximumPoolSize,
//        long keepAliveTime,
//        TimeUnit unit,
//        BlockingQueue<Runnable> workQueue,
//        ThreadFactory threadFactory,
//        RejectedExecutionHandler handler) {
//       ResourcePool resourcePool=new ResourcePool(3);
        ResourcePool  resourcePool=new SemaphoreResourcePool("test",3,-1);

        int corePoolSize=5;
        int maximumPoolSize=5      ;
        ExecutorService executor
         = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new CrawlerThreadFactory() ,new CrawlerRejectedExecutionHandler());


        //ExecutorService executor = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i,resourcePool);
            executor.execute(worker);
        }
        executor.shutdown(); // This will make the executor accept no new threads and finish all existing threads in the queue
        while (!executor.isTerminated()) { // Wait until all threads are finish,and also you can use "executor.awaitTermination();" to wait
        }
        System.out.println("Finished all threads");
    }
}
