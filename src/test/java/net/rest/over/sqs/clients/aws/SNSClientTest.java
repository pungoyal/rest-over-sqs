package net.rest.over.sqs.clients.aws;

import net.rest.over.sqs.clients.aws.SNSClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SNSClientTest {
    private SNSClient snsClient;
    private String topicDisplayName;

    @Before
    public void setUp() throws Exception {
        snsClient = SNSClient.getInstance();
        topicDisplayName = "rest-over-sqs-response-test";
    }

    @Test
    public void testGetTopicARNForAnExistingTopic() {
        String arn = snsClient.getTopicARN(topicDisplayName);
        Assert.assertNotNull(arn);
    }

    @Test
    public void testGetTopicARNForANonExistingTopic() {
        String arn = snsClient.getTopicARN("foo-bar-topic");
        Assert.assertNull(arn);
    }

    @Test
    public void testPublish() {
        String messageId = snsClient.publish(topicDisplayName, "this is a test response");
        Assert.assertNotNull(messageId);
    }
}
