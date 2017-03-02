package com.uumai.crawler3;

import com.uumai.resourcepool.ResourcePool;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/21/15
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkerThread
        implements Runnable {

    private String command;
    private ResourcePool resourcePool;

    public WorkerThread(String s,ResourcePool resourcePool){
        this.command=s;
        this.resourcePool=resourcePool;
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);

//        if(new Integer(command)%2==0){
//            //no need resource
//            processCommand();
//            System.out.println(Thread.currentThread().getName()+" end task "+ command );
//            return;
//        }

        int resouceid;
        while (true){
            resouceid=resourcePool.getResource();
            if(resouceid!=-1) break;
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
            //System.out.println(Thread.currentThread().getName()+" didn't get resource");
        }
        System.out.println(Thread.currentThread().getName()+" get resouce : "+resouceid);
        processCommand();
        resourcePool.releaseResource(resouceid);
        System.out.println(Thread.currentThread().getName()+" release resouce : "+resouceid);
        //System.out.println(Thread.currentThread().getName()+" end task "+ command );

    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}
