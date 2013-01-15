package net.restOverSQS;

import junit.framework.Assert;
import net.restOverSQS.domain.RestOverSQSMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        String messageId = restOverSQSClient.sendMessage(queueUrl, new TestRestOverSQSMessage());
        Assert.assertNotNull(messageId);

        List<RestOverSQSMessage> messages = restOverSQSClient.receiveMessages(queueUrl);
        Assert.assertEquals(1, messages.size());
        RestOverSQSMessage message = messages.get(0);
        Assert.assertNotNull(message.getRawBody());
    }

    @After
    public void tearDown() throws Exception {
        restOverSQSClient.clearQueue(queueUrl);
    }
}
