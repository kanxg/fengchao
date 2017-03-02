package com.uumai.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Created by rock on 9/6/15.
 */
public class UumaiPartitioner implements Partitioner {
    public UumaiPartitioner (VerifiableProperties props) {
        System.out.println("partition init ");
    }

    @Override
    public int partition(Object o, int i) {
        System.out.println("num.partitions:"+i);
        int partitionNum = (Integer)o%2;
        System.out.println("partitionNum:"+partitionNum);


        return partitionNum;
    }
}
