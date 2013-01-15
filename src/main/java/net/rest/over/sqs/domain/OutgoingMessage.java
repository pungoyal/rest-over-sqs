package net.rest.over.sqs.domain;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.rest.over.sqs.aws.clients.SNSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class OutgoingMessage {
    private String topicName;
    private String incomingMessageId;
    private String responseString;

    private final static Logger logger = LoggerFactory.getLogger(OutgoingMessage.class);

    public OutgoingMessage parseFrom(IncomingMessage incomingMessage) {
        this.topicName = incomingMessage.getResponseTopicName();
        this.incomingMessageId = incomingMessage.getMessageId();
        this.responseString = incomingMessage.getResponseString();
        return this;
    }

    public String publish() {
        return SNSClient.getInstance().publish(getTopicName(), toJson());
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("incoming-message-id", getIncomingMessageId());
            jsonObject.put("responseString", getResponseString());
            jsonObject.put("processed-at", new Date());
        } catch (JSONException e) {
            logger.error("could not serialize: " + this);
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getTopicName() {
        return topicName;
    }

    public String getIncomingMessageId() {
        return incomingMessageId;
    }

    public String getResponseString() {
        return responseString;
    }
}
