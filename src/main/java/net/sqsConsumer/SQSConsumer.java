package net.sqsConsumer;

public class SQSConsumer {
    public static void main(String[] args) {
        if (args.length == 0) {
            QueueConsumer consumer = new QueueConsumer("https://sqs.us-east-1.amazonaws.com/302497724868/puneet-test");
            consumer.receive();
        } else if (args.length == 1) {
            QueueConsumer consumer = new QueueConsumer(args[0]);
            consumer.receive();
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("********************************************************************************");
        System.out.println("USAGE:");
        System.out.println("\t java -jar <jar file> <queue name>");
        System.out.println("eg:");
        System.out.println("\t java -jar rest-over-sqs.jar https://sqs.us-east-1.amazonaws.com/302497724868/puneet-test");
        System.out.println("********************************************************************************");
    }

}
