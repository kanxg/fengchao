package com.uumai.activemq;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

/**
 * Created by rock on 9/2/15.
 */
public class ActiveMqDao {
    protected Connection connection = null;
    protected Session session = null;
    protected String DESTINATION;

    public void init() throws Exception {
        this.connection = ActiveMqFactory.getConnection();
    }

    public void close() {
        // 关闭释放资源
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
//                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
//                e.printStackTrace();
            }
        }
    }
}
