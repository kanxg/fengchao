package com.uumai.redis;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by kanxg on 14-11-24.
 */
public class RedisHelper {
    private RedisDao dao=new RedisDao();

    public void push(String key,String msg){
        Jedis redis=dao.getRedis();
        redis.lpush(key, msg);
        dao.returnResource(redis);
        dao.destroy();
    }

    public String pop(String key){
        String jobMsg=null;
        Jedis redis=dao.getRedis();
                 // block invoke
                 List<String> msgs = redis.brpop(1, key);
                 if (msgs != null) {
                      jobMsg = msgs.get(1);
                     //processMsg(jobMsg);
                     //System.out.println(jobMsg);
                 }
        dao.returnResource(redis);
        dao.destroy();
        return jobMsg;
    }


    public static void main( String[] args )
    {
        RedisHelper helper=new RedisHelper();
        //helper.push("uumai.amazon.asin","1");
        helper.pop("uumai.amazon.asin");

        System.out.println( "done!" );
    }
}
