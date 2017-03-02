package com.uumai.kafka.example;

import com.uumai.kafka.KafkaProperties;
import com.uumai.kafka.Producer;

/**
 * Created by rock on 9/6/15.
 */
public class SimpleProducerDemo {

    public static void main(String[] args) throws Exception {
        Producer producer2 = new Producer(KafkaProperties.topic2);
        producer2.start();
        Producer producer3 = new Producer(KafkaProperties.topic3);
        producer3.start();
    }
}
