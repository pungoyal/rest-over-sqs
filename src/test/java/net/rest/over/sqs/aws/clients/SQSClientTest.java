package net.rest.over.sqs.aws.clients;

import junit.framework.Assert;
import net.rest.over.sqs.domain.IncomingMessage;
import net.rest.over.sqs.domain.stubs.TestIncomingMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SQSClientTest {

    private SQSClient sqsClient;
    private String queueUrl;

    @Before
    public void setUp() throws Exception {
        sqsClient = SQSClient.getInstance();
        queueUrl = sqsClient.queueUrlFor("rest-over-sqs-test");
        sqsClient.clearQueue(queueUrl);
    }

    @Test
    public void testReceiveMessages() throws Exception {
        String messageId = sqsClient.sendMessage(queueUrl, new TestIncomingMessage());
        Assert.assertNotNull(messageId);

        List<IncomingMessage> messages = sqsClient.receiveMessages(queueUrl);
        Assert.assertEquals(1, messages.size());
        IncomingMessage message = messages.get(0);
        Assert.assertNotNull(message.getRawBody());
    }

    @After
    public void tearDown() throws Exception {
        sqsClient.clearQueue(queueUrl);
    }
}
