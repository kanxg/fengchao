package com.uumai.crawer2.queues;

import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.crawer2.CrawlerTasker;

/**
 * Created by rock on 9/5/15.
 */
public class ActiveMQPoolSenderTest {
    public static  void main(String[] args){
        ActiveMQPool activeMQPool=new ActiveMQPool("mock_test_object");
        activeMQPool.connect();

        for(int i=0;i<10;i++){
            CrawlerTasker crawlerTasker=new CrawlerTasker();
            crawlerTasker.setTaskerName("mock_test");
            crawlerTasker.setTaskerOwner("mock_test");
            crawlerTasker.setTaskerSeries("mock_test");
            crawlerTasker.setUrl("xxx"+i);

            activeMQPool.putDistributeTask(crawlerTasker);
        }


        activeMQPool.close();
    }
}
