package net.restOverSQS;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.util.json.JSONException;
import net.restOverSQS.domain.RestOverSQSMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestOverSQSClient extends AmazonSQSClient {
    public RestOverSQSClient() throws IOException {
        super(new ClasspathPropertiesFileCredentialsProvider().getCredentials());
    }

    public String queueUrlFor(String queueName) {
        return getQueueUrl(new GetQueueUrlRequest().withQueueName(queueName)).getQueueUrl();
    }

    public String sendMessage(String queueUrl, RestOverSQSMessage message) throws JSONException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(message.toJson());
        return sendMessage(sendMessageRequest).getMessageId();
    }

    public List<RestOverSQSMessage> receiveMessages(String queueUrl) {
        List<RestOverSQSMessage> result = new ArrayList<RestOverSQSMessage>();

        for (Message message : getAllMessages(queueUrl)) {
            result.add(new RestOverSQSMessage().parseFrom(message));
        }

        return result;
    }

    public int clearQueue(String queueUrl) {
        List<Message> messages = getAllMessages(queueUrl);
        int failedCount = 0;

        while (messages.size() > 0) {
            List<DeleteMessageBatchRequestEntry> entriesForDeletion = new ArrayList<DeleteMessageBatchRequestEntry>();

            for (Message message : messages) {
                entriesForDeletion.add(new DeleteMessageBatchRequestEntry().withId(message.getMessageId()).withReceiptHandle(message.getReceiptHandle()));
            }

            DeleteMessageBatchRequest emptyQueueRequest = new DeleteMessageBatchRequest().withQueueUrl(queueUrl).withEntries(entriesForDeletion);
            List<BatchResultErrorEntry> failed = deleteMessageBatch(emptyQueueRequest).getFailed();
            failedCount += failed.size();

            messages = getAllMessages(queueUrl);
        }

        return failedCount;
    }

    private List<Message> getAllMessages(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl).withMaxNumberOfMessages(10);
        return receiveMessage(receiveMessageRequest).getMessages();
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(receiptHandle);
        deleteMessage(deleteMessageRequest);
    }
}
