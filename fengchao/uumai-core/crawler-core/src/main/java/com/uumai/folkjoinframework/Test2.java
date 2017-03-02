package com.uumai.folkjoinframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by rock on 9/27/15.
 */
public class Test2 extends RecursiveAction {
    private int i=0;

    public  Test2(int i){
        this.i=i;
    }
    @Override
    protected void compute() {
//        System.out.println(i);
        System.out.println("Thread "+ Thread.currentThread().getName() +" do task "+i);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if(i==1) return;
//        new Test2(i-1).compute();
//        invokeAll(new Test2(i-1));
    }
    public static void  main(String[] a){
        ForkJoinPool pool = new ForkJoinPool();

        for(int i=0;i<10; i++){
            Test2 test2=new Test2(i);
            pool.execute(test2);
        }

        while(pool.getActiveThreadCount()!=0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
