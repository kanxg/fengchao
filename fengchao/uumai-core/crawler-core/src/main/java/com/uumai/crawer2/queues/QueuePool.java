package com.uumai.crawer2.queues;
import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer.util.UumaiTime;
import com.uumai.crawer2.CrawlerTasker;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rock on 8/29/15.
 */
public class QueuePool {
    protected String rediskey;

    private DateTime lastreloadtime;

    private UumaiTime uumaiTime=new UumaiTime();

    private int reloadkeyinterval= 5*60;

    protected List<CrawlerTasker> runningTakserlist=new ArrayList<CrawlerTasker>();


    //for every node, have one running list, when workpool is empty , check running list
    //protected String __runingKey="runningKey" + UumaiProperties.getHostName();

    public  QueuePool(){
        try{
            this.reloadkeyinterval= new Integer(UumaiProperties.readconfig("uumai.crawler.queuepool.reloadkeyinterval",""+5*60));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void checkreloadkeys(){
        if(lastreloadtime==null){
            reloadKeys();
            lastreloadtime= uumaiTime.getNow();
        }else{
            DateTime now= uumaiTime.getNow();
            if(now.isAfter(lastreloadtime.plusSeconds(reloadkeyinterval))){
                reloadKeys();
                lastreloadtime=now;
            }
        }
    }
    protected void reloadKeys() {

    }

    public void connect() {

    }
//
//    public List<String> blpop() {
//        return null;
//    }
//
//    public List<String> brpop() {
//        return null;
//    }

//    public Long llen() {
//        return 1l;
//    }

    //    public  void putDistributeTask(CrawlerTasker tasker, boolean putfirst) {
//    }
//    public Long llen(int level) {
//        return -1l;
//    }
    public List<CrawlerTasker> getDistributeTask() {
        return null;
    }
    public void putDistributeTask(CrawlerTasker tasker) {

    }

    public void putDistributeTask(String host,CrawlerTasker tasker) {

    }

    public void removeRunningList(CrawlerTasker tasker) {
    }

    public void close() {

    }

    public void cleantasks() {

    }

    public void checkRunningPool() {
    }

    public Long  getQueueSize(String key) {
        return 0l;
    }

    public String getRediskey() {
        return rediskey;
    }

    public void setRediskey(String rediskey) {
        this.rediskey = rediskey;
    }

    public int getReloadkeyinterval() {
        return reloadkeyinterval;
    }

    public void setReloadkeyinterval(int reloadkeyinterval) {
        this.reloadkeyinterval = reloadkeyinterval;
    }
}