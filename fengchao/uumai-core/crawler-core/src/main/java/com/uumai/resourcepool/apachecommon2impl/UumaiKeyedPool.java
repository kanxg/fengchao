package com.uumai.resourcepool.apachecommon2impl;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by rock on 2/29/16.
 */
public class UumaiKeyedPool {
    GenericKeyedObjectPool<Integer,UumaiResourceObject> pool;
    private int maxtotal;

    public UumaiKeyedPool(int maxtotal){
        this.maxtotal=maxtotal;
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(maxtotal); //整个池最大值
        config.setMaxTotalPerKey(maxtotal); //每个key的最大
        config.setBlockWhenExhausted(true);
        config.setMinIdlePerKey(0);
        config.setMaxWaitMillis(-1); //获取不到永远等待
        config.setNumTestsPerEvictionRun(Integer.MAX_VALUE); // always test all idle objects
        config.setTestOnBorrow(true);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);
        config.setTimeBetweenEvictionRunsMillis(1 * 60000L); //-1不启动。默认1min一次
        config.setMinEvictableIdleTimeMillis(10 * 60000L); //可发呆的时间,10mins
        config.setTestWhileIdle(false) ; //发呆过长移除的时候是否test一下先

         pool = new GenericKeyedObjectPool<Integer,UumaiResourceObject>(new UumaiKeyedPooledObjectFactory(), config);
    }

    public UumaiResourceObject getResource(){
        UumaiResourceObject resource = null;
        try {
            for(int i=0;i<maxtotal;i++){
                resource = pool.borrowObject(i);
                if(resource!=null) {
                    resource.setResourceid(i);
                    System.out.println("get a new UumaiResourceObject");
                    return resource;
                }

            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void releaseResource(UumaiResourceObject resource){
        if(null != resource) {
            pool.returnObject(resource.getResourceid(),resource);
            System.out.println("release a new UumaiResourceObject");
        }
    }

    public static void main(String[] a){
        UumaiKeyedPool uumaiKeyedPool=new UumaiKeyedPool(10);
        UumaiResourceObject[] resourcePools=new UumaiResourceObject[10];
        for(int i=0;i<10;i++) {
            resourcePools[i]= uumaiKeyedPool.getResource();
        }
        for(int i=0;i<10;i++) {
            uumaiKeyedPool.releaseResource(resourcePools[i]);
        }
    }
}
