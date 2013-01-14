package net.sqsConsumer;

import org.junit.Test;

public class QueueConsumerTest {
    @Test
    public void testGetMessage() {
        QueueConsumer consumer = new QueueConsumer("https://sqs.us-east-1.amazonaws.com/302497724868/puneet-test");
        consumer.receive();
    }
}
