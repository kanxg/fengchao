package com.uumai.crawer2.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.pool.DefaultFixThreadPool;
import com.uumai.crawer2.pool.WorkerPool;
import com.uumai.crawer2.queues.ActiveMQPool;
import com.uumai.crawer2.queues.QueuePool;
import com.uumai.crawer2.queues.RedisQueuePool;
import com.uumai.logs.UumaiLogUtil;
import redis.clients.jedis.Jedis;

import com.uumai.crawer.util.UumaiProperties;
import com.uumai.redis.RedisDao;
import com.uumai.redis.pubsub.Listener;
import com.uumai.redis.pubsub.Receiver;

/**
 * Created with IntelliJ IDEA. User: rock Date: 3/16/15 Time: 2:34 PM To change
 * this template use File | Settings | File Templates.
 */
public class AbstractAppMaster extends Thread {

    public QueuePool queuePool;
    public String redisKey;

    public AbstractAppMaster(){

        String packagename=this.getClass().getPackage().toString().replace("package com.uumai.crawer.", "");
        if(packagename.indexOf(".")>0){
            packagename=packagename.substring(0,packagename.indexOf("."));
        }
        this.redisKey="uumai."+packagename;
        System.out.println("redisKey:"+redisKey);
    }


    public AbstractAppMaster init(){

        String queueimple= UumaiProperties.readconfig("uumai.core.queue.class",null);
        if("activemq".equalsIgnoreCase(queueimple)){
            this.queuePool=new ActiveMQPool(redisKey);
        }else{
            this.queuePool=new RedisQueuePool(redisKey);

        }
        return this;
    }

    protected void cleanprevioustasker(){
        this.getQueuePool().cleantasks();
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }


    public QueuePool getQueuePool() {
        return queuePool;
    }

    public void setQueuePool(QueuePool queuePool) {
        this.queuePool = queuePool;
    }


    private String host;
    private String currentTaskerOwner;
    private String currentTaskerName;
    private String currentTaskerSerie;
    private int currentTaskercount=0;

    UumaiLogUtil uumaiLogUtil=new UumaiLogUtil();

	public void run() {

         try {
             this.queuePool.connect();
             uumaiLogUtil.connect();

             dobusiness();
		} catch (Exception e) {
 			e.printStackTrace();
		}finally {
             queuePool.close();
             uumaiLogUtil.close();
        }

        System.out.println("stopped master!");

	}
    public void customerCommand(String command,String[] paramsargs){

    }

    protected void putDistributeTask(String host,CrawlerTasker crawlerTasker)  throws Exception {
        if(this.currentTaskerSerie!=crawlerTasker.getTaskerSeries()){
            //new series,clean
            this.cleanpreviouslog();
        }
        this.host=host;
        this.currentTaskerOwner=crawlerTasker.getTaskerOwner();
        this.currentTaskerName=crawlerTasker.getTaskerName();
        this.currentTaskerSerie=crawlerTasker.getTaskerSeries();
        this.currentTaskercount=this.currentTaskercount+1;

        // put >1000 , start to check pool size
        if(this.currentTaskercount>1000){
            //per 100, check pool once
            if(this.currentTaskercount%100==0)
                waitingfortaskerfull();
        }
        if(host==null) {
//            super.putDistributeTask(crawlerTasker);
            this.getQueuePool().putDistributeTask( crawlerTasker);

        }else{
//            super.putDistributeTask(host,crawlerTasker);
            this.getQueuePool().putDistributeTask(host, crawlerTasker);

        }
    }
    protected void putDistributeTask(CrawlerTasker crawlerTasker)  throws Exception {
        this.putDistributeTask(null, crawlerTasker);
    }

    private String getCurrentLey(){
        if(this.host!=null){
            return this.host+"-"+this.getQueuePool().getRediskey() +"_" +this.currentTaskerOwner+ "_" + this.currentTaskerName +"_"+ this.currentTaskerSerie;
        }
        return this.getQueuePool().getRediskey() +"_" +this.currentTaskerOwner+ "_" + this.currentTaskerName +"_"+ this.currentTaskerSerie;
    }
    private void waitingfortaskerfull(){
        try {
            String key=getCurrentLey();
            while(true){
                long currentQueueSize=this.getQueuePool().getQueueSize(key);
                if(currentQueueSize>500){
                    System.out.println("running tasker queue>500! waiting....");
                    System.out.println("current tasker queue size:"+currentQueueSize +", total count:"+ this.currentTaskercount);
                    Thread.sleep(1000*10);
                }else{
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


        public void dobusiness()  throws Exception {

	}

    protected void waittaskfinished()  throws Exception {
        this.waittaskfinished(false);
    }
        protected void waittaskfinished(boolean failedThrowException)  throws Exception{
        if(this.currentTaskerName==null||this.currentTaskerOwner==null||this.currentTaskerSerie==null||this.currentTaskercount==0){
            System.out.println("didn't detect current tasker.");
            return;
        }
            //check redis pool
         String key=getCurrentLey();
        while(true){
            long currentQueueSize=this.getQueuePool().getQueueSize(key);
            System.out.println("Tasker Owner:"+this.currentTaskerOwner+",Name:"+this.currentTaskerName+",Series:"+this.currentTaskerSerie+",totalcount:"+this.currentTaskercount+", still have "+currentQueueSize + " not start...");

            if(currentQueueSize>0){
                Thread.sleep(1000*10);
            }else{
                break;
            }
        }

        //check uumai_logs

            int logscount=0;
        for(int i=0;i<=100;i++){
            int new_logscount=getFinishedCount();
            if(logscount!=new_logscount){
                i=0;
                logscount=new_logscount;
            }
            System.out.println("Tasker Owner:"+this.currentTaskerOwner+",Name:"+this.currentTaskerName+",Series:"+this.currentTaskerSerie+",totalcount:"+this.currentTaskercount + ", have finished " +logscount);

            if(logscount>=this.currentTaskercount){
                System.out.println("all done!");
                break;
            }else{
                Thread.sleep(1000*10);
            }
            if(i==60){   //wait for 5 mins
                System.out.println("wating too long, stop!");
            }
        }
            System.out.println("summy totalcount:"+this.currentTaskercount);
            System.out.println("summy logs total count:"+logscount);
            int logfailedscount=getFailedCount();
            System.out.println("summy logs failed count:"+logfailedscount);

            if(logscount!=this.currentTaskercount){
                System.out.println("WARNNING: HAVE MISSED TASKERS!");
            }

    }
    public void cleanpreviouslog(){
        //reset tasker indicator
        this.host=null;
        this.currentTaskerOwner=null;
        this.currentTaskerName=null;
        this.currentTaskerSerie=null;
        this.currentTaskercount=0;
    }
    public int getFinishedCount(){
        int new_logscount=uumaiLogUtil.getLogs(this.currentTaskerOwner,this.currentTaskerName,this.currentTaskerSerie);
        return new_logscount;
    }

    public int getFailedCount(){
        int new_logscount=uumaiLogUtil.getLogs(this.currentTaskerOwner, this.currentTaskerName, this.currentTaskerSerie,"false");
        return new_logscount;
    }

    public int getSuccessdCount(){
        int new_logscount=uumaiLogUtil.getLogs(this.currentTaskerOwner, this.currentTaskerName, this.currentTaskerSerie,"true");
        return new_logscount;
    }

    public float getFailedRate(){
        float rate=getFailedCount()/getFinishedCount();
        return rate;
    }
}