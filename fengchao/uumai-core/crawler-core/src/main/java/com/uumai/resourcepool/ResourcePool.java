package com.uumai.resourcepool;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/22/15
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResourcePool {
    protected  String name;
    protected int poolsize;
    protected int waittime;

    public  ResourcePool(String name,int poolsize,int waittime) {
        this.name=name;
        this.poolsize=poolsize;
        this.waittime=waittime;
    }

        public Integer getResource(){
        return null;
    }
    public void releaseResource(int i){

    }

    public void resize(int poolsize, int waittime){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getPoolsize() {
        return poolsize;
    }

    public void setPoolsize(int poolsize) {
        this.poolsize = poolsize;
    }

    public int getWaittime() {
        return waittime;
    }

    public void setWaittime(int waittime) {
        this.waittime = waittime;
    }
}
