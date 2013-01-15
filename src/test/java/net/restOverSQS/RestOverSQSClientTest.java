package net.restOverSQS;

import com.amazonaws.services.sqs.model.Message;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class RestOverSQSClientTest {

    private RestOverSQSClient restOverSQSClient;
    private String queueUrl;

    @Before
    public void setUp() throws Exception {
        restOverSQSClient = new RestOverSQSClient();
        queueUrl = restOverSQSClient.queueUrlFor("rest-over-sqs-test");
        restOverSQSClient.clearQueue(queueUrl);
    }

    @Test
    public void testReceiveMessages() throws Exception {
        String messageBody = String.format("test message, sent on: %s", new Date());

        String messageId = restOverSQSClient.sendMessage(queueUrl, messageBody);
        Assert.assertNotNull(messageId);

        List<Message> messages = restOverSQSClient.receiveMessages(queueUrl);
        Assert.assertEquals(1, messages.size());
    }

    @After
    public void tearDown() throws Exception {
        restOverSQSClient.clearQueue(queueUrl);
    }
}
