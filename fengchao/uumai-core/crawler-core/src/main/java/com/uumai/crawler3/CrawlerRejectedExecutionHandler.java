package com.uumai.crawler3;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 1/21/15
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrawlerRejectedExecutionHandler implements RejectedExecutionHandler {
    public CrawlerRejectedExecutionHandler() {
        super();
    }

    /**
     * Does nothing, which has the effect of discarding task r.
     * @param r the runnable task requested to be executed
     * @param e the executor attempting to execute this task
     */
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        System.out.println(r.toString() + " is rejected");
    }

}
