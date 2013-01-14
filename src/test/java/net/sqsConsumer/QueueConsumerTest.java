package net.sqsConsumer;

import org.junit.Ignore;
import org.junit.Test;

public class QueueConsumerTest {
    @Test
    @Ignore
    public void testGetMessage() {
        QueueConsumer consumer = new QueueConsumer("https://sqs.us-east-1.amazonaws.com/302497724868/puneet-test");
        consumer.receive();
    }
}
