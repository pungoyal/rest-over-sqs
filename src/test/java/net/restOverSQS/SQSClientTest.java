package net.restOverSQS;

import junit.framework.Assert;
import net.restOverSQS.domain.IncomingMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SQSClientTest {

    private SQSClient SQSClient;
    private String queueUrl;

    @Before
    public void setUp() throws Exception {
        SQSClient = new SQSClient();
        queueUrl = SQSClient.queueUrlFor("rest-over-sqs-test");
        SQSClient.clearQueue(queueUrl);
    }

    @Test
    public void testReceiveMessages() throws Exception {
        String messageId = SQSClient.sendMessage(queueUrl, new TestIncomingMessage());
        Assert.assertNotNull(messageId);

        List<IncomingMessage> messages = SQSClient.receiveMessages(queueUrl);
        Assert.assertEquals(1, messages.size());
        IncomingMessage message = messages.get(0);
        Assert.assertNotNull(message.getRawBody());
    }

    @After
    public void tearDown() throws Exception {
        SQSClient.clearQueue(queueUrl);
    }
}
