package com.uumai.crawer2.pool;

import com.uumai.crawer2.MultiCrawlerWorker;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kanxg on 14-12-18.
 */
public class DefaultFixThreadPool extends  WorkerPool{
    public ExecutorService executor;
    //public ArrayBlockingQueue<CrawlerTasker> queue;

   // ConcurrentHashMap<String,String>  urllist;
    //private Set<String> urllist;
//    public ResourcePool resourcePool;
    //boolean ismasterapp;
    private int queuesize=1;

    private class CustomThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = DefaultFixThreadPool.class.getSimpleName() + count.addAndGet(1);
            //System.out.println(threadName);
            t.setName(threadName);
            return t;
        }
    }


    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 记录异常
            // 报警处理等
            System.out.println("uumai pool full, put new tasker will be sendback to pool.");
            try {
                MultiCrawlerWorker crawlerWorker=(MultiCrawlerWorker)r;
                crawlerWorker.sendBack2Pool();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public DefaultFixThreadPool(int poolsize){
        this.poolsize=poolsize;
        this.queuesize=poolsize/2;
        if(this.queuesize==0)
            this.queuesize=1;
        //this.ismasterapp=ismasterapp;
    }

    public void init(){
        System.out.println(this.getClass().getCanonicalName()+" start ThreadPool with size: "+ poolsize + ",queue size:"+this.queuesize);

         //executor = Executors.newFixedThreadPool(poolsize);
        executor = new ThreadPoolExecutor(1, poolsize,
                10L, TimeUnit.MINUTES,
                //  new LinkedBlockingQueue<Runnable>(),
                new ArrayBlockingQueue<Runnable>(queuesize),
                //new CustomThreadFactory(),
                Executors.defaultThreadFactory(),
                new CustomRejectedExecutionHandler());

//        executor = new ThreadPoolExecutor(poolsize, poolsize,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>());
////                new CustomThreadFactory(),
////                new CustomRejectedExecutionHandler());


        //queue = new  ArrayBlockingQueue<CrawlerTasker>(queuesize,true);
        //urllist =new ConcurrentHashMap<String,String>();
        // urllist= Collections.synchronizedSet(new HashSet<String>()); //new HashSet<String>();

//        if(resourcepoolsize>0){
//            resourcePool=new DefaultResoucePool(resourcepoolsize) ;
//        }


    }





//    public CrawlerTasker pollTask(){
//        return queue.poll();
//    }

    //一直阻塞

    /**
     *

    public void putTask(CrawlerWorker worker){
        try {
//            if(urllist.get(tasker.getUrl())==null){
//                queue.put(tasker);
//                urllist.put(tasker.getUrl(),"");
//            }
            if(ismasterapp){
                //master app, need check duplicate url
                if(!urllist.contains(worker.tasker.getUUID())){
                    //queue.put(tasker);
                    urllist.add(worker.tasker.getUUID());
                    executor.execute(worker);
                }
            }else{
                //slave , don't need to check duplicate url
                executor.execute(worker);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */

    public void doWorker(MultiCrawlerWorker worker){
//        executor.execute(worker);
        Future<?> future =executor.submit(worker);
        TimeoutWatchThread timeoutWatchThread=new TimeoutWatchThread();
        timeoutWatchThread.setWorker(worker);
        timeoutWatchThread.setFuture(future);
        timeoutWatchThread.run();
    }


    public int getQueuesize() {
        return queuesize;
    }

    public void setQueuesize(int queuesize) {
        this.queuesize = queuesize;
    }
    
}
