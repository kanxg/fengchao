package com.uumai.resourcepool.semaphore;

import com.uumai.resourcepool.ResourcePool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/29/15
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SemaphoreResourcePool extends ResourcePool {

    private AdjustableSemaphore semaphore;

    public SemaphoreResourcePool(String name,int poolsize,int waittime){
        super(name,poolsize,waittime);
        semaphore=new AdjustableSemaphore();
        semaphore.setMaxPermits(poolsize);
    }

    @Override
    public void resize(int poolsize, int waittime){
        semaphore=new AdjustableSemaphore();
        semaphore.setMaxPermits(poolsize);
        System.out.println("resize resource size to:" +poolsize);
    }

    @Override
    public Integer getResource(){
        try {
            semaphore.acquire();
            System.out.println("get resource");
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return -1;
    }

    @Override
    public void releaseResource(int i){
        try {
            if(this.waittime>0){
                Thread.sleep(this.waittime);
            }
            semaphore.release();
            System.out.println("relese resource");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] a){
        ExecutorService executorService = Executors.newCachedThreadPool();
        final SemaphoreResourcePool uumaiSemaphoreResourcePool=new SemaphoreResourcePool("test",3,100);
        for(int i=0;i<10;i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int i = uumaiSemaphoreResourcePool.getResource();
                    if (i == -1) {
                        System.out.println("didn't get resource");
                    } else {
                        System.out.println("get resource");
                    }
                    try {
                        Thread.sleep(Math.round(Math.random()*10)*1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    uumaiSemaphoreResourcePool.releaseResource(1);
                    System.out.println("release resource");
                }
            };
            executorService.execute(runnable);
        }

    }
}
