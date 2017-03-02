package com.uumai.redis.pubsub;

import redis.clients.jedis.Jedis;

import com.uumai.crawer.util.UumaiProperties;
import com.uumai.redis.RedisDao;
 
public class Sender {
	private RedisDao dao;
	private String channel;
	private String slavename;
	public Sender(String channel){
		this.channel=channel;
		this.slavename=UumaiProperties.getIpaddress();
		dao=new RedisDao();
	}
	public void sendmsg(String message){
		try {
            Jedis redis=dao.getRedis();
            redis.publish(channel, slavename+" : "+ message);
            dao.returnResource(redis);
            //System.out.println(" publish message: "+ message +" to channel:"+ channel);
         } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public void quitemsg(){
		sendmsg("exit");
	}
	public void destory(){
		dao.destroy();
	}
	public static void main(String[] args) {
		Sender sender=new Sender("amazon");
		sender.sendmsg( "start");
		sender.quitemsg();
		
		sender.destory();
    }
}
