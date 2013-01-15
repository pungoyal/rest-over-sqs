package net.restOverSQS;

import com.amazonaws.services.sqs.model.Message;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QueueConsumer {
    private final String queueName;
    private final RestOverSQSClient sqsClient;
    private int pollCounter;

    public QueueConsumer(RestOverSQSClient sqsClient, String queueName) throws IOException {
        this.sqsClient = sqsClient;
        this.queueName = queueName;
        this.pollCounter = 0;
    }

    public void receive(String queueUrl) {
        List<Message> messages = sqsClient.receiveMessages(queueUrl);

        for (Message message : messages) {
            processAndDelete(message, queueUrl);
        }
    }

    public void startListening(int pollingInterval) {
        String queueUrl = sqsClient.queueUrlFor(queueName);
        System.out.println(String.format("starting to listen to: %s, polling interval: %s", queueUrl, pollingInterval));

        while (true) {
            System.out.println(String.format("poll counter: %s...", pollCounter));
            receive(queueUrl);
            pollCounter += 1;
            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processAndDelete(Message message, String queueUrl) {
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

        sqsClient.deleteMessage(queueUrl, receiptHandle);
    }
}
