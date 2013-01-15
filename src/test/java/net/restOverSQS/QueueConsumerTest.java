package net.restOverSQS;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class QueueConsumerTest {
    private String queueUrl;
    private RestOverSQSClient sqsClient;

    @Before
    public void setUp() throws IOException {
        sqsClient = new RestOverSQSClient();
        queueUrl = sqsClient.queueUrlFor("rest-over-sqs-test");

        sqsClient.sendMessage(queueUrl, String.format("message from test - %s", new Date()));
    }

    @Test
    public void testGetMessage() throws IOException {
        QueueConsumer consumer = new QueueConsumer(sqsClient, queueUrl);
        consumer.receive(queueUrl);
    }

    @After
    public void tearDown() {
        int failedDeletionsCount = sqsClient.clearQueue(queueUrl);
        Assert.assertEquals(0, failedDeletionsCount);
    }
}
