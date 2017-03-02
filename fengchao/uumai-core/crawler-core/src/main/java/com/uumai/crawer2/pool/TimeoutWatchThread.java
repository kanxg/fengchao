package com.uumai.crawer2.pool;

import com.uumai.crawer2.MultiCrawlerWorker;
import com.uumai.logs.UumaiLog;
import com.uumai.logs.UumaiLogUtil;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by rock on 5/29/16.
 */
public class TimeoutWatchThread implements  Runnable{
    private static int waitingtime=60*5;  //seconds
    private Future<?> future;
    private MultiCrawlerWorker worker;

    @Override
    public void run(){
        try {
            if(future!=null){
                try {
                    future.get(waitingtime, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    System.out.println("crawlerworker timeout , kill it,url:"+worker.tasker.getUrl());

                    future.cancel(true);
                    worker.sendBack2Pool();
//                    saveUUmaiLog(e);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendbacktopool(){

    }

    private void saveUUmaiLog(TimeoutException e){
        try {
            UumaiLogUtil uumaiLogUtil=new UumaiLogUtil();
            UumaiLog uumaiLog=uumaiLogUtil.createLogInstanceFromTasker(worker.tasker);
            uumaiLogUtil.createLogInstanceFromException(uumaiLog,worker.tasker,e);
            uumaiLogUtil.createLog(uumaiLog);
        } catch (Exception ex) {
            ex.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }

    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public MultiCrawlerWorker getWorker() {
        return worker;
    }

    public void setWorker(MultiCrawlerWorker worker) {
        this.worker = worker;
    }
}
