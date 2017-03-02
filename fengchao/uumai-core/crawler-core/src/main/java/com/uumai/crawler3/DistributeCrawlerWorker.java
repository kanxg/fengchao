package com.uumai.crawler3;

import com.uumai.crawler3.pool.DefaultFixDistributeThreadPool;
import com.uumai.redis.RedisDao;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/13/15
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistributeCrawlerWorker {

    private RedisDao dao;

   private String redisKey;

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public DistributeCrawlerWorker(String workername,DefaultFixDistributeThreadPool pool, boolean usingHttpdownload) {
        dao=new RedisDao();
    }
    public void run(){
        Jedis redis=dao.getRedis();
        while (true){
            List<String> msgs = redis.brpop(1, redisKey);
            if (msgs != null) {
                String jobMsg = msgs.get(1);
                //processMsg(jobMsg);
                //System.out.println(command +" processing "+jobMsg);
                try {
                    //dobusiness(jobMsg);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    System.out.println("no task,Thread  sleep 5 seconds");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}