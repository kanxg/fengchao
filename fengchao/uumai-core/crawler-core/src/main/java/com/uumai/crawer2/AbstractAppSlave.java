package com.uumai.crawer2;

import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer2.pool.DefaultFixThreadPool;
import com.uumai.crawer2.pool.WorkerPool;
import com.uumai.crawer2.queues.ActiveMQPool;
import com.uumai.crawer2.queues.QueuePool;
import com.uumai.crawer2.queues.RedisQueuePool;
import com.uumai.zookeeperclient.uitl.ZookeeperClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 2/12/15
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractAppSlave  extends Thread {

    public WorkerPool workerPool;
    public QueuePool queuePool;
    public String redisKey;
    public int poolsize=10;

    private boolean isTerminate=false;
    
    private int sleepInterval=5000;

    private ZookeeperClient zookeeperClient;
//    private int waitforTermiateTime=1000*60*10/sleepInterval;   //10 mins no tasker ,termiate
//    private int waitingTime=0;
    
//    private Sender sender=null;
//    static {
//        //
//        System.out.println("set all log4j level to OFF");
//        Logger logger = Logger.getLogger(AbstractAppSlave.class.getName());
//         logger.setLevel(Level.OFF);
//
//    }

    public void init(){
        this.poolsize=new Integer(UumaiProperties.readconfig("uumai.crawler.defaultpoolsize","10"));
        String packagename=this.getClass().getPackage().toString().replace("package com.uumai.crawer.", "");
        if(packagename.indexOf(".")>0){
            packagename=packagename.substring(0,packagename.indexOf("."));
        }
        this.redisKey="uumai."+packagename;
        System.out.println("redisKey:" + redisKey);

        this.workerPool=new DefaultFixThreadPool(this.poolsize);
        String queueimple= UumaiProperties.readconfig("uumai.core.queue.class",null);
        if("activemq".equalsIgnoreCase(queueimple)){
            this.queuePool=new ActiveMQPool(redisKey);
        }else{
            this.queuePool=new RedisQueuePool(redisKey);

        }
        startworkpool();
//        if(this.resourcepoolsize<=0){
//
//        } else{
//            this.maxqueuesize=resourcepoolsize/2;
//        }

        // MQ between Master and Slaves
//        sender=new Sender(this.getClass().getPackage().toString()+"__channel");

//        sender.sendmsg("Slave started!



    }
    protected void register2zookeeper(){
//        while(true){
            try {
                zookeeperClient= new ZookeeperClient();
                zookeeperClient.createWithSession("/uumai/session/"+ UumaiProperties.getHostName(),null);
                return ;
            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println(this.getClass().getCanonicalName()+" Register Zookeeper failed, stand by! will try 5 mins later... ");
                System.out.println(this.getClass().getCanonicalName()+" Register Zookeeper failed, may exist one,exit! ");
                try {
                    Thread.sleep(1000*60*5);
                } catch (InterruptedException e1) {
                }
            }finally {
                zookeeperClient.close();
            }
//        }
    }

    public void cleanprevioustasker(){
        queuePool.cleantasks();
    }


    private void startworkpool(){
        System.out.println(this.getClass().getCanonicalName()+" Slave worker init. ");

        if(this.getQueuePool() instanceof ActiveMQPool){
            ActiveMQPool activeMQPool=(ActiveMQPool)this.getQueuePool();
            activeMQPool.startConsumerGroup();
        }

        this.workerPool.init();
    }


    public void run(){

        register2zookeeper();

            while(true){
                if(isTerminate){
                    break;
                }
                try{
                    this.queuePool.connect();
                    connectRedis();
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.out.println(" error when connect to redis: " + ex.getMessage());

                }finally {
                    queuePool.close();
                }
                try {
                    System.out.println("sleeping 5 secs and reconnect... ");
                    Thread.sleep(sleepInterval);
//                    if(!isTerminate)
//                        this.getQueuePool().connect();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }

        }
    private void connectRedis() throws Exception{

        // block invoke
        while (true){
            if(isTerminate){
                break;
            }
//            System.out.println("start get queue:" + new UumaiTime().getNowString());
            List<CrawlerTasker> crawlerTaskerList= queuePool.getDistributeTask();
//            System.out.println("finish get queue:" + new UumaiTime().getNowString());
            if (crawlerTaskerList.size()!=0) {
                for(CrawlerTasker crawlerTasker:crawlerTaskerList){
                    try {
                        //CrawlerTasker jobtasker= (CrawlerTasker)SerializeUtil.unserialize(jobMsg);
                        //processMsg(jobMsg);
                        //System.out.println(this.getClass().getCanonicalName() + " get new tasker " + jobtasker.getUrl());
                        //sender.sendmsg(" get new tasker. ");
                        MultiCrawlerWorker crawlerWorker=createWoker(crawlerTasker);
                        if(crawlerWorker!=null){
                            crawlerWorker.setQueuePool(this.queuePool);
                            this.getWorkerPool().doWorker(crawlerWorker);
                        }
                        checkfull();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //checkfull
//                try {
//                    System.out.println("start checkfull:" + new UumaiTime().getNowString());
//                    checkfull();
//                    System.out.println("finish checkfull:" + new UumaiTime().getNowString());

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }else{
//            	if(waitingTime>waitforTermiateTime){
//            		stoppool();
//            	}

                try {
//                    System.out.println("no job,sleep 5 seconds... ");
                    //sender.sendmsg(" no job,sleep! ");
                    checkRunningPool();
                    Thread.sleep(sleepInterval);
//                    waitingTime=waitingTime+1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println (this.getClass().getCanonicalName() + " get termiate signal, stop main thread!");
//         sender.sendmsg("get termiate signal, stop main thread!");
//        queuePool.close();

    }

    protected MultiCrawlerWorker createWoker(CrawlerTasker tasker) throws Exception{
        return null;
    }

    private void checkRunningPool(){
        DefaultFixThreadPool  defaultFixThreadPool=(DefaultFixThreadPool)workerPool;
        ThreadPoolExecutor threadPoolExecutor=(ThreadPoolExecutor)defaultFixThreadPool.executor;

        if(threadPoolExecutor.getActiveCount()==0){
            //no tasker runing
//            System.out.println (this.getClass().getCanonicalName() + "no tasker, will check running tasker list");
                this.getQueuePool().checkRunningPool();
        }
    }


    public void checkfull(){
        DefaultFixThreadPool  defaultFixThreadPool=(DefaultFixThreadPool)workerPool;

        ThreadPoolExecutor threadPoolExecutor=(ThreadPoolExecutor)defaultFixThreadPool.executor;
        while(true){
            //System.out.println(" current queue size:" + threadPoolExecutor.getQueue().size());
            //use for DefaultFixThreadPool for ArrayBlockingQueue
            if (threadPoolExecutor.getQueue().size()==defaultFixThreadPool.getQueuesize()&&threadPoolExecutor.getActiveCount()==workerPool.getPoolsize()){
                //use for DefaultFixThreadPool fix pool size
//             if (threadPoolExecutor.getQueue().size() > defaultFixThreadPool.getQueuesize()){
                    try {
                    System.out.println(this.getClass().getCanonicalName() + " pool full,wait! ");
//                    sender.sendmsg(" pool full,wait! ");
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                return;
            }
        }

    }



    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println(this.getClass().getCanonicalName() + " Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            System.err.println(this.getClass().getCanonicalName() + " Pool interrupt");
        }
    }

    public void stoppool(){
        //stop main thread
        isTerminate=true;
//        while (true){
//            if(this.isTerminate){
//                break;
//            } else{
//                try {
//                    System.out.println("waiting for main thread stop!");
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//
//            }
//        }
        //stop pool
        System.out.println("stop "+ this.getClass().getCanonicalName() + " poll");
        DefaultFixThreadPool  defaultFixThreadPool=(DefaultFixThreadPool)workerPool;
        shutdownAndAwaitTermination(defaultFixThreadPool.executor)   ;
    }



    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public int getPoolsize() {
        return poolsize;
    }

    public void setPoolsize(int poolsize) {
        this.poolsize = poolsize;
    }

    public QueuePool getQueuePool() {
        return queuePool;
    }

    public void setQueuePool(QueuePool queuePool) {
        this.queuePool = queuePool;
    }

    public WorkerPool getWorkerPool() {
        return workerPool;
    }

    public void setWorkerPool(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }


}