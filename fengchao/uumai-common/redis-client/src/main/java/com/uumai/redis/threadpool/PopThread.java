package com.uumai.redis.threadpool;

import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.redis.RedisConstant;
import com.uumai.redis.RedisDao;
import com.uumai.redis.RedisHelper;
import com.uumai.redis.Testpojo;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by kanxg on 14-11-25.
 */
public class PopThread implements Runnable{
    private String command;
    private RedisDao dao;
    public PopThread(String s){
        this.command=s;
        dao=new RedisDao();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
        dao.destroy();
    }

    private void processCommand() {
        Jedis redis=dao.getRedis();
        // block invoke
        while (true){
            List<String> msgs = redis.brpop(1, RedisConstant.key);
            if (msgs != null) {
                String jobMsg = msgs.get(1);
                Testpojo pojo=(Testpojo) SerializeUtil.unserialize(jobMsg);
                //processMsg(jobMsg);
                System.out.println(command +" processing "+pojo);
                try {
                    dobusiness(jobMsg);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    System.out.println(command +" no job,sleep! ");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //dao.returnResource(redis);
    }

    public void dobusiness(String msg){

    }

    @Override
    public String toString(){
        return this.command;
    }
}
