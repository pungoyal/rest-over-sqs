package net.rest.over.sqs.domain;

import junit.framework.Assert;
import net.rest.over.sqs.clients.aws.SQSClient;
import net.rest.over.sqs.domain.stubs.TestIncomingMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OutgoingMessageTest {
    private OutgoingMessage message;
    private String queueUrl;
    private SQSClient sqsClient;

    @Before
    public void setUp() throws Exception {
        message = new OutgoingMessage();

        sqsClient = SQSClient.getInstance();
        queueUrl = sqsClient.queueUrlFor("rest-over-sqs-response-test");
        sqsClient.clearQueue(queueUrl);
    }

    @After
    public void tearDown() throws Exception {
        sqsClient.clearQueue(queueUrl);
    }

    @Test
    public void testParseFrom() throws Exception {
        TestIncomingMessage incomingMessage = new TestIncomingMessage();
        incomingMessage.setResponseString("success!");

        OutgoingMessage outgoingMessage = message.parseFrom(incomingMessage);

        Assert.assertEquals(incomingMessage.getResponseTopicName(), outgoingMessage.getTopicName());
        Assert.assertEquals(incomingMessage.getMessageId(), outgoingMessage.getIncomingMessageId());
        Assert.assertEquals(incomingMessage.getResponseString(), outgoingMessage.getResponseString());
    }
}
