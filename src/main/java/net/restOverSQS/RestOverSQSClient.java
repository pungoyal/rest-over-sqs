package net.restOverSQS;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestOverSQSClient extends AmazonSQSClient {
    public RestOverSQSClient() throws IOException {
        super(new PropertiesCredentials(RestOverSQSClient.class.getResourceAsStream("aws-credentials.properties")));
    }

    public String queueUrlFor(String queueName) {
        return getQueueUrl(new GetQueueUrlRequest().withQueueName(queueName)).getQueueUrl();
    }

    public String sendMessage(String queueUrl, String messageBody) {
        return sendMessage(new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(messageBody)).getMessageId();
    }

    public List<Message> receiveMessages(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl).withMaxNumberOfMessages(10);
        return receiveMessage(receiveMessageRequest).getMessages();
    }

    public int clearQueue(String queueUrl) {
        List<Message> messages = receiveMessages(queueUrl);
        int failedCount = 0;

        while (messages.size() > 0) {
            List<DeleteMessageBatchRequestEntry> entriesForDeletion = new ArrayList<DeleteMessageBatchRequestEntry>();

            for (Message message : messages) {
                entriesForDeletion.add(new DeleteMessageBatchRequestEntry().withId(message.getMessageId()).withReceiptHandle(message.getReceiptHandle()));
            }

            DeleteMessageBatchRequest emptyQueueRequest = new DeleteMessageBatchRequest().withQueueUrl(queueUrl).withEntries(entriesForDeletion);
            List<BatchResultErrorEntry> failed = deleteMessageBatch(emptyQueueRequest).getFailed();
            failedCount += failed.size();

            messages = receiveMessages(queueUrl);
        }

        return failedCount;
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(receiptHandle);
        deleteMessage(deleteMessageRequest);
    }
}
