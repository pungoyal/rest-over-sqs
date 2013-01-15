package net.restOverSQS.domain;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.restOverSQS.RestOverSQSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestOverSQSMessage {
    private String verb;
    private String uri;
    private JSONObject params;

    private String messageId;
    private String receiptHandle;
    private String rawBody;

    private final static Logger logger = LoggerFactory.getLogger(RestOverSQSMessage.class);

    public RestOverSQSMessage parseFrom(Message message) {
        this.messageId = message.getMessageId();
        this.receiptHandle = message.getReceiptHandle();
        this.rawBody = message.getBody();

        String jsonMessageBody = message.getBody();

        try {
            JSONObject jsonObject = new JSONObject(jsonMessageBody);
            logger.debug(String.format("will parse json: %s", jsonObject));

            this.verb = (String) jsonObject.get("verb");
            this.uri = (String) jsonObject.get("uri");
            this.params = (JSONObject) jsonObject.get("params");
        } catch (JSONException e) {
            logger.debug(String.format("could not parse: %s", jsonMessageBody));
        }
        return this;
    }

    public void processAndDelete(RestOverSQSClient sqsClient, String queueUrl) {
        logger.debug("  Processing message");
        logger.debug("    MessageId:  " + getMessageId());
        logger.debug("    Body:       " + getRawBody());
        logger.debug("    Verb:       " + getVerb());
        logger.debug("    URI:        " + getUri());
        logger.debug("    params:     " + getParams());

        sqsClient.deleteMessage(queueUrl, receiptHandle);
    }

    public String toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("verb", getVerb());
        jsonObject.put("uri", getUri());
        jsonObject.put("params", getParams());

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

    protected void setVerb(String verb) {
        this.verb = verb;
    }

    protected void setUri(String uri) {
        this.uri = uri;
    }

    protected void setParams(JSONObject params) {
        this.params = params;
    }
}
