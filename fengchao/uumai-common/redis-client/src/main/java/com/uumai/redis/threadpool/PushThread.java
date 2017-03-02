package com.uumai.redis.threadpool;

import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.redis.RedisConstant;
import com.uumai.redis.RedisDao;
import com.uumai.redis.RedisHelper;
import com.uumai.redis.Testpojo;

import redis.clients.jedis.Jedis;

/**
 * Created by kanxg on 14-11-25.
 */
public class PushThread implements Runnable{
    private Testpojo pojo;
    private  RedisDao dao;
    public PushThread(Testpojo pojo){
        this.pojo=pojo;
        dao=new RedisDao();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+pojo);
        //for (int i = 0; i < 10; i++) {
            processCommand();
        //}
        System.out.println(Thread.currentThread().getName()+" End.");
        dao.destroy();
    }

    private void processCommand() {
        try {
            Jedis redis=dao.getRedis();
            redis.lpush(RedisConstant.key, SerializeUtil.serialize(pojo));
            dao.returnResource(redis);
            System.out.println(" push "+pojo);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


 
}
