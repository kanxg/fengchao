package com.uumai.crawer2.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * Created by rock on 5/2/16.
 */
public class RmiServer extends  Thread{
    private static final String ADDRESS = "0.0.0.0";
    //private static final int PORT = 9999;
    private int PORT;

    private static Configuration conf;

    private  RmiImpl rmiImpl=null;

    private Server server;

    @Override
    public  void run()  {

        try {
            conf = new Configuration();
            server = new RPC.Builder(conf).setProtocol(RmiProtocol.class)
                    .setInstance(rmiImpl).setBindAddress(ADDRESS).setPort(PORT).build();

            server.start();

            server.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            server.stop();

        }
    }

    public  void stopserver() {
        try{
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            server.stop();

        }
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public RmiImpl getRmiImpl() {
        return rmiImpl;
    }

    public void setRmiImpl(RmiImpl rmiImpl) {
        this.rmiImpl = rmiImpl;
    }
}
