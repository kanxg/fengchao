package com.uumai.resourcepool.apachecommon2impl;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by rock on 2/29/16.
 */
public class UumaiKeyedPooledObjectFactory extends BaseKeyedPooledObjectFactory<Integer,UumaiResourceObject> {

    @Override
    public UumaiResourceObject create(Integer integer) throws Exception {
        return new UumaiResourceObject();
    }

    @Override
    public PooledObject<UumaiResourceObject> wrap(UumaiResourceObject resourceObject) {
        return new DefaultPooledObject<UumaiResourceObject>(resourceObject);

    }
}
