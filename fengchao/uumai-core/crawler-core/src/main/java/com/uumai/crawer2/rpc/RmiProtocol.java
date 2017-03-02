package com.uumai.crawer2.rpc;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

/**
 * Created by rock on 5/2/16.
 */
public interface RmiProtocol extends VersionedProtocol {
    public static final long versionID = 1L;

    void clearalltaskers() throws IOException;
    void restart() throws IOException;


}
