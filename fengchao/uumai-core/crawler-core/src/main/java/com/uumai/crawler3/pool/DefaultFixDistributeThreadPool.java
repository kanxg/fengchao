package com.uumai.crawler3.pool;

import com.uumai.crawer2.CrawlerTasker;
import com.uumai.redis.RedisDao;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kanxg on 15-1-8.
 */
public class DefaultFixDistributeThreadPool {
    public ExecutorService executor;


    private int poolsize;
//    private int queuesize;

    private  RedisDao dao;

    // ConcurrentHashMap<String,String>  urllist;
    private Set<String> urllist;

    public DefaultFixDistributeThreadPool(){
        this(1000);
    }

    public DefaultFixDistributeThreadPool(int poolsize){
        this.poolsize=poolsize;
        //this.queuesize=queuesize;
        executor = Executors.newFixedThreadPool(poolsize);
         //urllist =new ConcurrentHashMap<String,String>();
        urllist= Collections.synchronizedSet(new HashSet<String>()); //new HashSet<String>();
        dao=new RedisDao();
     }

    public CrawlerTasker pollTask(){
        return null;
    }

    //一直阻塞
    public   void putTask(CrawlerTasker tasker){
        try {
            if(!urllist.contains(tasker.getUUID())){
                Jedis redis=dao.getRedis();
                //redis.lpush("", SerializeUtil.serialize(tasker));
                dao.returnResource(redis);
                System.out.println(   " push ");
                //Thread.sleep(1000);
                urllist.add(tasker.getUUID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //返回特殊值
    public  void offerTask(CrawlerTasker tasker){


    }
    //抛出异常
    public   void addTask(CrawlerTasker tasker){

    }

}
