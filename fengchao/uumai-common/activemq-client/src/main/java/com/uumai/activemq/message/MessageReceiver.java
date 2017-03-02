package com.uumai.activemq.message;

/**
 * Created by rock on 9/2/15.
 */
import javax.jms.*;

import com.uumai.activemq.ActiveMqDao;
import com.uumai.activemq.ActiveMqFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>function:</b> 消息接收者
 * @version 1.0
 */
public class MessageReceiver extends ActiveMqDao{

    private MessageConsumer consumer;
    public  MessageReceiver(String DESTINATION){
        this.DESTINATION=DESTINATION;
    }

    public void init() throws Exception{
        super.init();
        this.session = ActiveMqFactory.getSession(Boolean.FALSE,Session.CLIENT_ACKNOWLEDGE,connection);
//        this.session = ActiveMqFactory.getSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE,connection);

        Destination destination = session.createQueue(DESTINATION);

        // 创建消息制作者
        consumer = session.createConsumer(destination);

    }
    public void close() {
        try {
            consumer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        super.close();
    }

    public Message receiveMessage() throws Exception {
        Message message = consumer.receiveNoWait();
        return message;
//            message.acknowledge();
//        if(message==null) return  null;
//
//        if(message instanceof  TextMessage){
//            current_message=message;
//
//            TextMessage text = (TextMessage) message;
//            return text.getText();
//        }
////        else if(message instanceof  ObjectMessage){
////            ObjectMessage text = (ObjectMessage) message;
////            return  text.getObject();
////        }
//        return null;
    }


    public static void main(String[] args) throws Exception {
        MessageReceiver messageReceiver=new MessageReceiver("uumai.quartz3");
        messageReceiver.init();
        while(true){
            Message msg=messageReceiver.receiveMessage();
            if(msg!=null){
                System.out.println(msg);
                msg.acknowledge();
                //messageReceiver.acknowledge();
            }else{

                    try{
                        //System.out.println("sleep");
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


            }
        }
    }
}
