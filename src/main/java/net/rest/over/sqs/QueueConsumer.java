package net.rest.over.sqs;

import net.rest.over.sqs.clients.aws.SQSClient;
import net.rest.over.sqs.domain.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class QueueConsumer {
    private final String queueName;
    private final SQSClient sqsClient;
    private int pollCounter;

    private final static Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

    public QueueConsumer(SQSClient sqsClient, String queueName) throws IOException {
        this.sqsClient = sqsClient;
        this.queueName = queueName;
        this.pollCounter = 0;
    }

    public void receive(String queueUrl) {
        List<IncomingMessage> messages = sqsClient.receiveMessages(queueUrl);

        for (IncomingMessage message : messages) {
            message.processAndDelete(sqsClient, queueUrl);
        }
    }

    public void startListening(int pollingIntervalInSeconds) {
        String queueUrl = sqsClient.queueUrlFor(queueName);
        logger.debug(String.format("starting to listen to: %s, polling interval: %s", queueUrl, pollingIntervalInSeconds));

        while (true) {
            logger.debug(String.format("poll counter: %s...", pollCounter));
            receive(queueUrl);
            pollCounter += 1;
            try {
                Thread.sleep(pollingIntervalInSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
