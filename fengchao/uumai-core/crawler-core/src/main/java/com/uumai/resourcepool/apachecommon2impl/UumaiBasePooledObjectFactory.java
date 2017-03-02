package com.uumai.resourcepool.apachecommon2impl;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by rock on 2/29/16.
 */
public class UumaiBasePooledObjectFactory extends BasePooledObjectFactory<UumaiResourceObject> {


    @Override
    public UumaiResourceObject create() throws Exception {
        System.out.println("create a new UumaiResourceObject");
        return new UumaiResourceObject();
    }

    @Override
    public PooledObject<UumaiResourceObject> wrap(UumaiResourceObject obj) {
        System.out.println("wrap a new UumaiResourceObject");
        return new DefaultPooledObject<UumaiResourceObject>(obj);
    }

}
