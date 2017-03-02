package com.uumai.resourcepool.apachecommon2impl;

import com.uumai.resourcepool.ResourcePool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by rock on 2/29/16.
 */
public class UumaiBasePool  {

    GenericObjectPool<UumaiResourceObject> pool;

    public UumaiBasePool(){
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(3);
        conf.setMaxIdle(10);
        pool = new GenericObjectPool<UumaiResourceObject>(new UumaiBasePooledObjectFactory(), conf);
    }

    public UumaiResourceObject getResource(){
        UumaiResourceObject resource = null;
        try {
            resource = pool.borrowObject();
            System.out.println("get a new UumaiResourceObject");
            return resource ;
         }catch(Exception e) {
            e.printStackTrace();
         }
        return null;
    }
    public void releaseResource(UumaiResourceObject resource){
        if(null != resource) {
            pool.returnObject(resource);
            System.out.println("release a new UumaiResourceObject");
        }
    }

    public static void main(String[] a){
        UumaiBasePool uumaiBasePool=new UumaiBasePool();
        UumaiResourceObject[] resourcePools=new UumaiResourceObject[10];
        for(int i=0;i<10;i++) {
            resourcePools[i]= uumaiBasePool.getResource();
        }
        for(int i=0;i<10;i++) {
            uumaiBasePool.releaseResource(resourcePools[i]);
        }
    }
}
