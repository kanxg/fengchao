package com.uumai.activemq.message;

/**
 * Created by rock on 9/2/15.
 */
import javax.jms.*;

import com.uumai.activemq.ActiveMqDao;
import com.uumai.activemq.ActiveMqFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>function:</b> 消息发送者
 * @version 1.0
 */
public class MessageSender extends ActiveMqDao{

    private  MessageProducer producer;

    public  MessageSender(String DESTINATION){
        this.DESTINATION=DESTINATION;
    }

    public void init() throws Exception{
        super.init();
        this.session = ActiveMqFactory.getSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE,connection);
        Destination destination = session.createQueue(DESTINATION);
        // 创建消息制作者
        producer = session.createProducer(destination);
        // 设置持久化模式
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    public void close() {
        try {
            this.producer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        super.close();
    }

    public  void clean(){

    }

        public void sendMessage(String message) throws Exception {
        TextMessage text = session.createTextMessage(message);
        session.createObjectMessage();

        producer.send(text);
        //session.commit();

    }

//    public void sendMessage(Serializable message) throws Exception {
//
//        ObjectMessage obj= session.createObjectMessage(message);
//
//        producer.send(obj);
//        session.commit();
//
//    }


    public static void main(String[] args) throws Exception {
        MessageSender messageSender=new MessageSender("mock_test");
        messageSender.init();
        int i=0;
        while (true) {
            messageSender.sendMessage("test"+i);
            i=i+1;
            //   messageSender.sendMessage("test2");
        }
//        messageSender.sendMessage(new MessageObject());
//        messageSender.sendMessage(new MessageObject());
        //messageSender.close();
    }
}
