package com.uumai.kafka;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;

import java.util.Properties;

/**
 * Created by rock on 9/7/15.
 */
public class AdminTools {
    public void createtopic(String myTopic) {
        ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
        AdminUtils.createTopic(zkClient, myTopic, 10, 1, new Properties());
    }
    public  boolean checkExistTopic(String myTopic){
        ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
        return AdminUtils.topicExists(zkClient, myTopic);
    }
    public  void deleteTopic(String myTopic){
        ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
        AdminUtils.deleteTopic(zkClient, myTopic);
    }

    public static void main(String[] args){
        AdminTools adminTools=new AdminTools();
        adminTools.deleteTopic("test2");
        adminTools.createtopic("test2");

    }
}
