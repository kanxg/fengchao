package com.uumai.crawer2.local;

import com.google.gson.JsonObject;
import com.uumai.crawer2.CrawlerTasker;

import java.util.List;

/**
 * Created by rock on 12/9/15.
 */
public class LocalAppMaster extends Thread{
    public CrawlerTasker crawlerTasker;
    public LocalCrawlerWorker localCrawlerWorker;

    public LocalAppMaster init() {
        return this;
    }

    @Override
    public void run() {

        try {
            dobusiness();
        } catch (Exception e) {
             e.printStackTrace();
        }

    }
    public void dobusiness()  throws Exception {

    }

    protected void putDistributeTask(String host,CrawlerTasker crawlerTasker)  throws Exception {
        this.putDistributeTask(crawlerTasker);
    }
    public void putDistributeTask(CrawlerTasker crawlerTasker)  throws Exception {
        this.crawlerTasker=crawlerTasker;
        localCrawlerWorker=    new LocalCrawlerWorker(crawlerTasker);
        localCrawlerWorker.download();
        localCrawlerWorker.pipeline();
    }
    public List<JsonObject> getPipelineResult(){
        return this.localCrawlerWorker.pipelineresult;
    }
    protected void waittaskfinished()  throws Exception {

    }




}
