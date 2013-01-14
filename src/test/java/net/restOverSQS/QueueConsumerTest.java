package net.restOverSQS;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueueConsumerTest {
    private String queueUrl;
    private AmazonSQSClient sqsClient;

    @Before
    public void setUp() throws IOException {
        InputStream resourceAsStream = QueueConsumerTest.class.getResourceAsStream("aws-credentials.properties");
        Assert.assertNotNull(resourceAsStream);

        PropertiesCredentials credentials = new PropertiesCredentials(resourceAsStream);
        sqsClient = new AmazonSQSClient(credentials);

        GetQueueUrlRequest queueUrlRequest = new GetQueueUrlRequest().withQueueName("rest-over-sqs-test");
        queueUrl = sqsClient.getQueueUrl(queueUrlRequest).getQueueUrl();

        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(String.format("message from test - %s", new Date()));
        sqsClient.sendMessage(sendMessageRequest);
    }

    @Test
    public void testGetMessage() throws IOException {
        QueueConsumer consumer = new QueueConsumer(queueUrl);
        consumer.receive();
    }

    @After
    public void tearDown() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl);
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

        if (messages.size() > 0) {
            List<DeleteMessageBatchRequestEntry> entriesForDeletion = new ArrayList<DeleteMessageBatchRequestEntry>();

            for (Message message : messages) {
                entriesForDeletion.add(new DeleteMessageBatchRequestEntry().withId(message.getMessageId()).withReceiptHandle(message.getReceiptHandle()));
            }

            DeleteMessageBatchRequest emptyQueueRequest = new DeleteMessageBatchRequest().withQueueUrl(queueUrl).withEntries(entriesForDeletion);
            List<BatchResultErrorEntry> failed = sqsClient.deleteMessageBatch(emptyQueueRequest).getFailed();
            Assert.assertEquals(0, failed.size());
        }
    }
}
