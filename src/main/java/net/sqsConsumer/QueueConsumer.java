package net.sqsConsumer;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.Map;

public class QueueConsumer {
    private final String queueUrl;

    public QueueConsumer(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public void receive() {
        AmazonSQSClient sqsClient = new AmazonSQSClient(new AnonymousAWSCredentials());
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(this.queueUrl);
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            String receiptHandle = message.getReceiptHandle();

            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + receiptHandle);
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }


            sqsClient.deleteMessage(new DeleteMessageRequest().
                    withQueueUrl(this.queueUrl).
                    withReceiptHandle(receiptHandle));
        }

    }
}
