package net.rest.over.sqs.domain;

import com.amazonaws.util.json.JSONObject;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class IncomingMessageTest {
    private IncomingMessage incomingMessage;

    @Before
    public void setUp() throws Exception {
        incomingMessage = new IncomingMessage();
    }

    @Test
    public void testToJson() throws Exception {
        incomingMessage.setVerb("post");
        incomingMessage.setUri("http://www.google.com");
        incomingMessage.setResponseTopicName("rest-over-sqs-response");
        JSONObject params = new JSONObject();
        params.put("q", "amazon simple queue service");
        incomingMessage.setParams(params);

        String jsonString = incomingMessage.toJson();

        JSONObject json = new JSONObject(jsonString);
        Assert.assertEquals("post", json.get("verb"));
        Assert.assertEquals("http://www.google.com", json.get("uri"));
        Assert.assertEquals("rest-over-sqs-response", json.get("response-queue-name"));

        params = (JSONObject) json.get("params");
        Assert.assertEquals("amazon simple queue service", params.get("q"));
    }
}
