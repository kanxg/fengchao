package com.uumai.crawer2.queues;

import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.crawer2.CrawlerTasker;

import java.util.List;
import java.util.Set;

/**
 * Created by rock on 9/2/15.
 */
public class RedisQueuePoolTest {

    public static  void main(String[] args) throws  Exception{
        RedisQueuePool redisQueuePool=new RedisQueuePool("mock_test");
        redisQueuePool.connect();


        CrawlerTasker crawlerTasker=new CrawlerTasker();
        crawlerTasker.setTaskerName("mock_test");
        crawlerTasker.setTaskerOwner("mock_test");
        crawlerTasker.setTaskerSeries("mock_test");
        crawlerTasker.setUrl("fdasfsdafsadfasfd");
        redisQueuePool.putDistributeTask(crawlerTasker);


        Thread.sleep(5000);


        while (true){
            List<CrawlerTasker> lists=redisQueuePool.getDistributeTask();
            for(CrawlerTasker crawlerTasker1:lists){
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redisQueuePool.removeRunningList(crawlerTasker1);
            }
        }

//        redisQueuePool.close();
    }
}
