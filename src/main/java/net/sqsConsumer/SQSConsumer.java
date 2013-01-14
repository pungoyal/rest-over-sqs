package net.sqsConsumer;

public class SQSConsumer {
    public static void main(String[] args) {
        QueueConsumer queueConsumer = new QueueConsumer("https://sqs.us-east-1.amazonaws.com/302497724868/puneet-test");
        queueConsumer.receive();
    }
}
