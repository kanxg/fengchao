package com.uumai.folkjoinframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/25/15
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test extends RecursiveTask<Long> {
    private static final long serialVersionUID = 7875142223684511653L;
    private final long n;

    Test(long n) {
        this.n = n;
    }

    protected Long compute() {
        System.out.println("Thread "+ Thread.currentThread().getName() +" do task "+ n);

        if (n <= 1) {
            return n;
        }
        Test f1 = new Test(n - 1);
            f1.fork();
        Test f2 = new Test(n - 2);
        return f2.compute() + f1.join();
    }

    public static void main(String[] args) {
        Test task = new Test(10);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);
        System.out.println(task.getRawResult());
    }

}