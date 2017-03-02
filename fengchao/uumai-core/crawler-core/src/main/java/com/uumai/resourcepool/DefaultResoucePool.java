package com.uumai.resourcepool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/30/15
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 * Rock
 * this will only work in one JVM, like run in Storm.
 * if there are more JVM in one pi, like run in YARN, we need to use ZooKeeperResoucePool
 */
public class DefaultResoucePool extends  ResourcePool{

    Lock lock = new ReentrantLock();// 锁
    Condition condition= lock.newCondition();
    private Map<Integer,Integer> resouces=new HashMap<Integer,Integer>() ;

    public  DefaultResoucePool(String name,int poolsize,int waittime){
        super(name,poolsize,waittime);
        for(int i=0;i<poolsize;i++){
            resouces.put(i, new Integer("0"));
        }

    }
//    public synchronized Integer getResource(){
//            for(int i=0;i<poolsize;i++){
//                Integer s= resouces.get(i);
//                if(s==0){
//                    resouces.put(i, new Integer("1"));
//                    return i;
//                }
//            }
//
//        return -1;
//    }
//    public synchronized void releaseResource(int i){
//            resouces.put(i,new Integer("0"));
//    }
    @Override
    public void resize(int poolsize, int waittime){
        System.out.println("start to resize pool:" + this.name + ",from " + this.poolsize + " to " + poolsize);
        lock.lock();
        try{
            while(true){
                boolean using=false;
                for(int i=0;i<poolsize;i++){
                    Integer s= resouces.get(i);
                    if(s==1){
                        using=true;
                        break;
                    }
                }
                if(using){
                    condition.await();
                }else{
                    this.setPoolsize(poolsize);
                    this.setWaittime(waittime);
                    return;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
        System.out.println("finish resize pool:"+ this.name );
    }

     @Override
    public Integer getResource(){
        System.out.println("start to get new resource from pool:" + this.name);
         lock.lock();
        try{
            while(true){
                for(int i=0;i<poolsize;i++){
                    Integer s= resouces.get(i);
                    if(s==0){
                        resouces.put(i, new Integer("1"));
                        System.out.println("get pool resource :"+ i +"from pool:" + this.name  );
                        return  i;
                    }
                }
                condition.await();
            }

        } catch(Exception e){
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
         System.out.println("no resource in pool " +this.name );
        return -1;
    }
    @Override
    public void releaseResource(int i){
        System.out.println("start to release pool "+ this.name+ " resource "+ i );
        lock.lock();
        try{
            resouces.put(i,new Integer("0"));
            if(this.waittime>0){
                Thread.sleep(this.waittime);
            }
            condition.signalAll();
        } catch(Exception e){
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
        System.out.println("start to release pool "+ this.name+ " resource "+ i );
    }
}
