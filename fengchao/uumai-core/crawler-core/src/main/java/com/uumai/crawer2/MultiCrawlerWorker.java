package com.uumai.crawer2;


import com.google.gson.Gson;
import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer.util.UumaiTime;
import com.uumai.crawer.util.filesystem.UumaiFileUtil;
import com.uumai.crawer2.download.CrawlerProxy;
import com.uumai.crawer2.download.Download;
import com.uumai.crawer2.download.DownloadFactory;
import com.uumai.crawer2.queues.QueuePool;
import com.uumai.dao.helper.Json2DBHelper;
import com.uumai.logs.UumaiLog;
import com.uumai.logs.UumaiLogUtil;
import com.uumai.resourcepool.ResourcePool;
import com.uumai.resourcepool.ResourcePoolFactory;

import java.net.URL;

/**
 * Created by kanxg on 14-12-18.
 */
public class MultiCrawlerWorker extends CrawlerWorker {
    private String orignTaskerMsg;

    private QueuePool queuePool;


    private UumaiLog uumaiLog;
    private UumaiLogUtil uumaiLogUtil=new UumaiLogUtil();

//    public CrawlerWorker(CrawlerTasker tasker,DefaultFixThreadPool pool) {
//         this(tasker, pool,true);
//    }

    public MultiCrawlerWorker(CrawlerTasker tasker) {
        super(tasker);
    }


//    public CrawlerWorker(CrawlerTasker tasker,WorkerPool workerPool,QueuePool queuePool) {
//        this.tasker=tasker;
//        this.workerPool=workerPool;
//        this.queuePool=queuePool;
////        this.resourcePool=resourcePool;
////        this.queue=pool.queue;
//    }

    public void run(){
//        while (true){
//            CrawlerTasker tasker=this.pool.pollTask(); //queue.poll();
            if(tasker==null){
//                try {
                    System.out.println("tasker is null!");
                    //Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }else{
                System.out.println("Thread "+ Thread.currentThread().getName() +" do task "+tasker.getUrl());
//                System.out.println("Thread "+ Thread.currentThread().getName() +" start do tasker:" + new UumaiTime().getNowString());
                 try {
                    tasker.init();
                     if(tasker.getProxy()==null) {
                         String proxyip = UumaiProperties.readconfig("uumai.crawler.CrawlerProxy.ip", null);
                         String proxyport = UumaiProperties.readconfig("uumai.crawler.CrawlerProxy.port", null);
                         if (proxyip != null && proxyport != null) {
                             tasker.setProxy(new CrawlerProxy(proxyip, new Integer(proxyport)));
                         }
                     }
                     uumaiLog=uumaiLogUtil.createLogInstanceFromTasker(tasker);

                    dobusiness();
//                    System.out.println("Thread "+ Thread.currentThread().getName() +" finish do tasker:" + new UumaiTime().getNowString());
                } catch (Exception e) {
                    try {
                        uumaiLogUtil.createLogInstanceFromException(uumaiLog,tasker,e);
                        e.printStackTrace();
                        failHander();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

//                System.out.println("Thread "+ Thread.currentThread().getName() +" start save log:" + new UumaiTime().getNowString());

                //record tasker result into logs.
                //record into mongodb
                try {
                      uumaiLogUtil.createLog(uumaiLog);
//                    }
                    //remove from back redis pool list
                    this.getQueuePool().removeRunningList(tasker);
                } catch (Exception e) {
                    e.printStackTrace(); // To change body of catch statement use
                    // File | Settings | File Templates.
                }
//                System.out.println("Thread "+ Thread.currentThread().getName() +" finish save log:" + new UumaiTime().getNowString());

//                System.out.println("finish tasker:" + tasker.url);

            }


//        }

    }

    protected void download() throws  Exception{
        //try get from cache
        if(tasker.getSavefilename()!=null&&tasker.isUseingcache()){
            UumaiFileUtil uumaiFileUtil=new UumaiFileUtil();
            String text=uumaiFileUtil.readfromcache(tasker.getSavefilename());
            if(text!=null) {
                this.result=new CrawlerResult();
                result.setRawText(text);
                result.setUrl(tasker.getUrl());
                System.out.println("read cache from file:" + tasker.getSavefilename());
                return;
            }
        }

        //get connection resource
        Integer resouceid=null;

        //not get from cache, send http request
            try {
                if(Download.DownloadType.chrome_download==tasker.getDownloadType()
                        ||Download.DownloadType.firefox_download==tasker.getDownloadType()
                        ||Download.DownloadType.phantomjs_download==tasker.getDownloadType()
                        ||Download.DownloadType.jbrowser_download==tasker.getDownloadType()
                        ||Download.DownloadType.java_download==tasker.getDownloadType()
                        ||Download.DownloadType.httpclient_download==tasker.getDownloadType()
                        ||Download.DownloadType.openscript_download==tasker.getDownloadType()
                        ) {
                    resouceid = getResource();
                }
                Download download=getDownloadInstantce();
                this.result=download.download(tasker);
                this.result.setUrl(tasker.getUrl());
                } finally {
                    if(resouceid!=null&&resouceid==1)
                        releaseResouce(resouceid);
                }

        if(tasker.getSavefilename()!=null&&tasker.isUseingcache()){
            UumaiFileUtil uumaiFileUtil=new UumaiFileUtil();
            uumaiFileUtil.save2file(tasker.getSavefilename(),result.getRawText());
            System.out.println("save html to cache file:" + tasker.getSavefilename());
        }

    }

    protected Download getDownloadInstantce() throws  Exception {
        return DownloadFactory.getnewDownload(tasker.getDownloadType());
    }
        protected Integer getResource() throws  Exception{
        URL url = new URL(this.tasker.getUrl());
        ResourcePool resourcePool= ResourcePoolFactory.getNewResourcePool(url.getHost());
         if(resourcePool==null) return 1;
        while (true){
            Integer resouceid=resourcePool.getResource();
            if(resouceid!=-1) return resouceid;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void releaseResouce(Integer resouceid) throws  Exception{
        URL url = new URL(this.tasker.getUrl());
        ResourcePool resourcePool= ResourcePoolFactory.getNewResourcePool(url.getHost());

        if(resourcePool==null) return;

        resourcePool.releaseResource(resouceid);
    }


    /**
     * hander when worker get errors,put tasker back to pool.
     */
    protected void failHander(){
//    	if(this.tasker.getMaxRetryTimes_distributed()<=0){
//    		System.out.println("distributed retry times less than 0, not retry. ignore tasker.");
//    	}else{
//    		if(this.tasker.getRetryTimes_distributed()<this.tasker.getMaxRetryTimes_distributed()){
//                this.tasker.setRetryTimes_distributed(this.tasker.getRetryTimes_distributed()+1);
////            	this.workerPool.putDistributeFailedTask(this.tasker);
//    		}else{
//        		System.out.println("meet max distributed retry times, not retry.ignore tasker");
//    		}
//
//    	}
        String[] faillogic=this.tasker.getFailProcessLogic();
        if(faillogic==null||faillogic.length==1) return;

        if("sendbacktoredis".equalsIgnoreCase(faillogic[0])){
            String host=faillogic[1];
            if(host==null||"".equals(host)){
                this.getQueuePool().putDistributeTask(this.tasker);
            }else{
                this.getQueuePool().putDistributeTask(host,this.tasker);
            }
        }else if("save2db".equalsIgnoreCase(faillogic[0])){
            if(faillogic.length==3){
                saveTasker2DB(faillogic[1],faillogic[2]);
            }else{
                saveTasker2DB(faillogic[1],null);
            }

        }else if("saveFailed2db".equalsIgnoreCase(faillogic[0])){
            saveFailed2DB(faillogic[1]);
        }else if("retry".equalsIgnoreCase(faillogic[0])){

        }

    }

    protected void saveTasker2DB(String tablename,String json) {
        try {
//                    if(tasker.isSavingLogs()||!uumaiLog.isResult()){
            Json2DBHelper helper = new Json2DBHelper();
            if(json==null){
                helper.store(new Gson().toJson(this.tasker),tablename);
            }else{
                helper.store(json,tablename);
            }

//                    }
            //remove from back redis pool list
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
    }

    protected void saveFailed2DB(String tablename) {
        try {
//                    if(tasker.isSavingLogs()||!uumaiLog.isResult()){
            Json2DBHelper helper = new Json2DBHelper();
            helper.store(new Gson().toJson(this.uumaiLog),tablename);
//                    }
            //remove from back redis pool list
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
    }

    public void sendBack2Pool(){
            this.getQueuePool().putDistributeTask(this.tasker);
        }

    public QueuePool getQueuePool() {
        return queuePool;
    }

    public void setQueuePool(QueuePool queuePool) {
        this.queuePool = queuePool;
    }
}
