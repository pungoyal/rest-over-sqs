package net.rest.over.sqs;

import com.amazonaws.util.json.JSONException;
import junit.framework.Assert;
import net.rest.over.sqs.aws.clients.SQSClient;
import net.rest.over.sqs.domain.stubs.TestIncomingMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class QueueConsumerTest {
    private String queueUrl;
    private SQSClient sqsClient;

    @Before
    public void setUp() throws IOException, JSONException {
        sqsClient = SQSClient.getInstance();
        queueUrl = sqsClient.queueUrlFor("rest-over-sqs-test");

        sqsClient.sendMessage(queueUrl, new TestIncomingMessage());
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
