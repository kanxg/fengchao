package com.uumai.redis.pubsub;

import com.uumai.redis.RedisDao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Receiver extends Thread {
	private RedisDao dao;
	
	private String channels;
	private Listener listener;
	public Receiver(String channels,Listener listener){
		this.channels=channels;
		this.listener=listener;
		dao=new RedisDao();
	}
	
	public void run(){
		try {
            Jedis redis=dao.getRedis();
            redis.subscribe(listener, channels);
            System.out.println(" subscribe channel: "+channels);

            dao.returnResource(redis);
            System.out.println(" close...");
         } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void destory(){
		dao.destroy();
	}
	public static void main(String[] args) throws Exception{
		Receiver receiver=new Receiver("amazon",new Listener());
		receiver.start();

        Thread.sleep(5000);

        receiver.destory();
	}
	
}
