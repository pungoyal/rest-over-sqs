package net.restOverSQS;

import com.amazonaws.util.json.JSONException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class QueueConsumerTest {
    private String queueUrl;
    private RestOverSQSClient sqsClient;

    @Before
    public void setUp() throws IOException, JSONException {
        sqsClient = new RestOverSQSClient();
        queueUrl = sqsClient.queueUrlFor("rest-over-sqs-test");

        sqsClient.sendMessage(queueUrl, new TestRestOverSQSMessage());
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
