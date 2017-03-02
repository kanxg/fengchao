package com.uumai.activemq;

import com.uumai.crawer.util.UumaiProperties;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

/**
 * Created by rock on 9/6/15.
 */
public class ActiveMqHelper {
    JMXServiceURL url;
    JMXConnector jmxc;
    MBeanServerConnection conn;

    public  ActiveMqHelper(){
        try {
            url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+ UumaiProperties.readconfig("uumai.activemq.serverip", "localhost")+":1099/jmxrmi");
            jmxc = JMXConnectorFactory.connect(url);
            conn = jmxc.getMBeanServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  void deletequeue(String DESTINATION){
        try {
            String operationName="removeQueue"; //operation like addQueue or removeQueue
            String parameter=DESTINATION;   // Queue name
            ObjectName activeMQ = new ObjectName("org.apache.activemq:brokerName=localhost,type=Broker");
            if(parameter != null) {
                Object[] params = {parameter};
                String[] sig = {"java.lang.String"};
                conn.invoke(activeMQ, operationName, params, sig);
            } else {
                conn.invoke(activeMQ, operationName,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a){
        ActiveMqHelper activeMqHelper=new ActiveMqHelper();
        activeMqHelper.deletequeue("mock_test1");
    }

}
