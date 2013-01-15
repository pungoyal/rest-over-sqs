package net.rest.over.sqs.domain;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.rest.over.sqs.aws.clients.SQSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncomingMessage {
    private String verb;
    private String uri;
    private JSONObject params;

    private String messageId;
    private String receiptHandle;
    private String rawBody;

    private final static Logger logger = LoggerFactory.getLogger(IncomingMessage.class);
    private String responseTopicName;
    private String responseString;

    public IncomingMessage parseFrom(Message message) {
        this.messageId = message.getMessageId();
        this.receiptHandle = message.getReceiptHandle();
        this.rawBody = message.getBody();

        String jsonMessageBody = message.getBody();

        try {
            JSONObject jsonObject = new JSONObject(jsonMessageBody);
            logger.debug(String.format("will parse json: %s", jsonObject));

            this.verb = (String) jsonObject.get("verb");
            this.uri = (String) jsonObject.get("uri");
            this.responseTopicName = (String) jsonObject.get("response-queue-name");

            this.params = (JSONObject) jsonObject.get("params");
        } catch (JSONException e) {
            logger.debug(String.format("could not parse: %s", jsonMessageBody));
        }
        return this;
    }

    public void processAndDelete(SQSClient sqsClient, String queueUrl) {
        logger.debug("  Processing message");
        logger.debug("    MessageId:       " + getMessageId());
        logger.debug("    Body:            " + getRawBody());
        logger.debug("    Verb:            " + getVerb());
        logger.debug("    URI:             " + getUri());
        logger.debug("    Response Topic:  " + getResponseTopicName());
        logger.debug("    params:          " + getParams());

        setResponseString("successfully done!");

        new OutgoingMessage().parseFrom(this).publish();
        sqsClient.deleteMessage(queueUrl, getReceiptHandle());
    }

    public String toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("verb", getVerb());
        jsonObject.put("uri", getUri());
        jsonObject.put("params", getParams());
        jsonObject.put("response-queue-name", getResponseTopicName());

        return jsonObject.toString();
    }

    public String getVerb() {
        return verb;
    }

    public String getUri() {
        return uri;
    }

    public JSONObject getParams() {
        return params;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getRawBody() {
        return rawBody;
    }

    public String getResponseTopicName() {
        return responseTopicName;
    }

    protected void setVerb(String verb) {
        this.verb = verb;
    }

    protected void setUri(String uri) {
        this.uri = uri;
    }

    protected void setParams(JSONObject params) {
        this.params = params;
    }

    public void setResponseTopicName(String responseTopicName) {
        this.responseTopicName = responseTopicName;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
