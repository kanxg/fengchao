package com.uumai.activemq;

import com.uumai.crawer.util.UumaiProperties;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Properties;

/**
 * Created by rock on 9/2/15.
 */
public class ActiveMqFactory {

    public static synchronized Connection getConnection() throws Exception{
        Connection connection = null;
         try {
             String BROKER_URL="tcp://"+ UumaiProperties.readconfig("uumai.activemq.serverip","localhost")+":61616";
            // 创建链接工厂
            javax.jms.ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
             Properties props = new Properties();
             props.setProperty("prefetchPolicy.queuePrefetch", "1");
             props.setProperty("prefetchPolicy.queueBrowserPrefetch", "10");
             props.setProperty("prefetchPolicy.durableTopicPrefetch", "10");
             props.setProperty("prefetchPolicy.topicPrefetch", "10");


             // 通过工厂创建一个连接
            connection = factory.createConnection();
            // 启动连接
            connection.start();

        } catch (Exception e) {
             throw e;
        }
        return connection;
    }
    public static synchronized Session getSession(Boolean b,int ACKNOWLEDGE, Connection connection) throws Exception{
        Session session = null;
        try {
            session = connection.createSession(b, ACKNOWLEDGE);
//            conn.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);


        } catch (Exception e) {
            throw e;
        }
        return session;
    }
}
