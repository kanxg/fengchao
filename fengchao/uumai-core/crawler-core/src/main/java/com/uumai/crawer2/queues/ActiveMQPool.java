package com.uumai.crawer2.queues;

import com.uumai.activemq.ActiveMqHelper;
import com.uumai.activemq.message.MessageReceiver;
import com.uumai.activemq.message.MessageSender;
import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.redis.RedisDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jms.*;

/**
 * Created by rock on 9/2/15.
 */
public class ActiveMQPool extends  QueuePool{

    private Map<String,MessageReceiver> messageReceiverMap=new HashMap<String,MessageReceiver>();
    private Map<String,MessageSender> messageSenderMap=new HashMap<String,MessageSender>();

    private Map<String,Message> runningMessageList=new HashMap<String,Message>();

    ActiveMqHelper activeMqHelper=new ActiveMqHelper();


    public  ActiveMQPool(String rediskey){
        this.rediskey=rediskey;
     }
    @Override
    public void connect(){
        startSenderGroup();
    }

    public void startSenderGroup(){
            MessageSender messageSender=new MessageSender(this.rediskey);
            try {
                messageSender.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageSenderMap.put(this.rediskey,messageSender);

    }

    public void startConsumerGroup(){
             MessageReceiver messageReceiver=new MessageReceiver(this.rediskey);
            try {
                messageReceiver.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageReceiverMap.put(this.rediskey,messageReceiver);
    }

    @Override
    public void close(){
            MessageReceiver messageReceiver = messageReceiverMap.get(this.rediskey );
            if(messageReceiver!=null) messageReceiver.close();
            MessageSender messageSender = messageSenderMap.get(this.rediskey );
            if(messageSender!=null) messageSender.close();


    }

    @Override
    public  void putDistributeTask(CrawlerTasker tasker) {
        try {
            MessageSender messageSender = messageSenderMap.get(rediskey );
            messageSender.sendMessage(SerializeUtil.serialize(tasker));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<CrawlerTasker> getDistributeTask(){
            Message message=null;
            try {
                MessageReceiver messageReceiver = messageReceiverMap.get(this.rediskey );
                message=messageReceiver.receiveMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(message!=null){
                if(message instanceof TextMessage) {
                    try {
                        TextMessage text = (TextMessage) message;
                        String textmsg=text.getText();
                        CrawlerTasker tasker = (CrawlerTasker) SerializeUtil.unserialize(textmsg);
                        tasker.setJMSMessageID(message.getJMSMessageID());
                        runningMessageList.put(message.getJMSMessageID(),message);

                        runningTakserlist.add(tasker);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }

//        System.out.println("no messages get from pool.");
        //no tasker , check missing
        return this.runningTakserlist;
    }
    @Override
    public synchronized  void removeRunningList(CrawlerTasker tasker) {
         Message message= runningMessageList.get(tasker.getJMSMessageID());
        if(message!=null) {
//            System.out.println("mark the message from activeMQ with key: " + tasker.getRunningMessageId());
            try {
                message.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        runningMessageList.remove(tasker.getJMSMessageID());
    }
    @Override
    public void cleantasks() {
            this.cleantasks(rediskey);

    }
    private void cleantasks(String key) {
        activeMqHelper.deletequeue(key);
    }
}
