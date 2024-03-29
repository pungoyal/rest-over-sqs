package net.rest.over.sqs;

import net.rest.over.sqs.clients.aws.SQSClient;

import java.io.IOException;

public class SQSConsumer {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            printUsage();
            System.exit(0);
        }

        String queueName = args[0];
        SQSClient sqsClient = SQSClient.getInstance();
        QueueConsumer consumer = new QueueConsumer(sqsClient, queueName);

        int pollingInterval = 60;
        if (args.length == 2) {
            pollingInterval = Integer.parseInt(args[1]);
        }

        if (pollingInterval == -1) {
            consumer.receive(sqsClient.queueUrlFor(queueName));
        } else {
            consumer.startListening(pollingInterval);
        }
    }

    private static void printUsage() {
        System.out.println("********************************************************************************");
        System.out.println("USAGE:");
        System.out.println("\t java -jar <jar file> <queue name> <polling interval in seconds>");
        System.out.println("eg:");
        System.out.println("\t java -jar rest-over-sqs.jar test-sqs-queue 10");
        System.out.println("********************************************************************************");
    }
}
