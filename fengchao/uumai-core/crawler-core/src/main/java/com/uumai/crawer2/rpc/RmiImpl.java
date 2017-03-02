package com.uumai.crawer2.rpc;

import com.uumai.crawer2.AbstractAppSlave;
import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

/**
 * Created by rock on 5/2/16.
 */
public class RmiImpl  implements RmiProtocol   {

    private AbstractAppSlave abstractAppSlave;

    public AbstractAppSlave getAbstractAppSlave() {
        return abstractAppSlave;
    }

    public void setAbstractAppSlave(AbstractAppSlave abstractAppSlave) {
        this.abstractAppSlave = abstractAppSlave;
    }

    //implemt for rmi call

    @Override
    public long getProtocolVersion(String protocol, long clientVersion) {
        return RmiProtocol.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String protocol, long clientVersion,
                                                  int hashcode) {
        return new ProtocolSignature(RmiProtocol.versionID, null);
    }

    @Override
    public void clearalltaskers() throws IOException {
        System.out.println("clearalltaksers");
    }
    @Override
    public void restart() throws IOException{
        System.out.println("restart");
    }
}
