package com.uumai.crawer2.queues;

import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.crawer2.CrawlerTasker;

import java.util.List;

/**
 * Created by rock on 9/5/15.
 */
public class ActiveMQPoolReceiverTtest {
    public static  void main(String[] args){
        ActiveMQPool activeMQPool=new ActiveMQPool("mock_test_object");
        activeMQPool.connect();


        while (true){
            List<CrawlerTasker> crawlerTaskerlist=activeMQPool.getDistributeTask();
            if(crawlerTaskerlist==null){
                try {
                    //System.out.println("sleep");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                for(CrawlerTasker crawlerTasker:crawlerTaskerlist){
                    System.out.println("message:"+crawlerTasker.getUrl());
                    activeMQPool.removeRunningList(crawlerTasker);
                }

               // break;
            }


        }
    }

}
