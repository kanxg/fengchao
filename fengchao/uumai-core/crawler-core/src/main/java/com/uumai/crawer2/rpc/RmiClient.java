package com.uumai.crawer2.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RemoteException;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by rock on 5/2/16.
 */
public class RmiClient {
    private String server;
    private int port;

    private RmiProtocol proxy = null;

    public  RmiClient(String server,int port){
        this.server=server;
        this.port=port;
    }
    public void connect() throws  Exception{
        Configuration conf = new Configuration();
        InetSocketAddress addr = new InetSocketAddress(this.server, this.port);
        proxy = RPC.getProxy(RmiProtocol.class, RmiProtocol.versionID, addr, conf);

    }

    public void close() throws  Exception{
        RPC.stopProxy(proxy);
    }
     public void clearalltaskers() throws IOException {
          proxy.clearalltaskers();
    }
     public void restart() throws IOException{
        proxy.restart();
    }
}


