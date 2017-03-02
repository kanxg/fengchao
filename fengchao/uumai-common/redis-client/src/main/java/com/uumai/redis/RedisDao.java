package com.uumai.redis;

import com.uumai.crawer.util.UumaiProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by kanxg on 14-11-24.
 */
public class RedisDao {
    static JedisPool pool;

    public RedisDao(){
        init();
    }

    private void init(){
        if(pool==null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(100);
            config.setMaxIdle(8);
            config.setMinIdle(0);
            config.setMaxWaitMillis(15000);
            config.setMinEvictableIdleTimeMillis(300000);
            config.setSoftMinEvictableIdleTimeMillis(-1);
            config.setNumTestsPerEvictionRun(3);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestWhileIdle(false);
            config.setTimeBetweenEvictionRunsMillis(60000);//一分钟
            pool = new JedisPool(config, UumaiProperties.readconfig("uumai.redis.serverip", RedisConstant.default_redis_server_ip));
        }
    }

    public Jedis  getRedis() {
//        init();
        return pool.getResource();
    }

    public void returnResource(Jedis jedis){
//        pool.returnResource(jedis);
        jedis.close();
    }

    public void destroy(){
        if(pool!=null){
            pool.destroy();
            pool=null;
        }
    }
}
