package net.rest.over.sqs.clients.aws;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;

import java.util.List;

public class SNSClient extends AmazonSNSClient {
    private static SNSClient instance = null;

    public static SNSClient getInstance() {
        if (instance == null)
            instance = new SNSClient();

        return instance;
    }

    private SNSClient() {
        super(new ClasspathPropertiesFileCredentialsProvider().getCredentials());
    }

    public String publish(String topicName, String messageBody) {
        String topicARN = getTopicARN(topicName);

        PublishRequest publishRequest = new PublishRequest().withTopicArn(topicARN).withMessage(messageBody);
        return publish(publishRequest).getMessageId();
    }

    public String getTopicARN(String topicDisplayName) {
        List<Topic> topics = listTopics().getTopics();

        for (Topic topic : topics) {
            String topicArn = topic.getTopicArn();
            String[] tokens = topicArn.split(":");
            String displayName = tokens[tokens.length - 1];
            if (displayName.equals(topicDisplayName)) {
                return topicArn;
            }
        }

        return null;
    }
}
