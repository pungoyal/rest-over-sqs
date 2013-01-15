package net.restOverSQS.domain;

import com.amazonaws.util.json.JSONObject;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestOverSQSMessageTest {
    private RestOverSQSMessage restOverSQSMessage;

    @Before
    public void setUp() throws Exception {
        restOverSQSMessage = new RestOverSQSMessage();
    }

    @Test
    public void testToJson() throws Exception {
        restOverSQSMessage.setVerb("post");
        restOverSQSMessage.setUri("http://www.google.com");
        restOverSQSMessage.setResponseTopicName("rest-over-sqs-response");
        JSONObject params = new JSONObject();
        params.put("q", "amazon simple queue service");
        restOverSQSMessage.setParams(params);

        String jsonString = restOverSQSMessage.toJson();

        JSONObject json = new JSONObject(jsonString);
        Assert.assertEquals("post", json.get("verb"));
        Assert.assertEquals("http://www.google.com", json.get("uri"));
        Assert.assertEquals("rest-over-sqs-response", json.get("response-queue-name"));

        params = (JSONObject) json.get("params");
        Assert.assertEquals("amazon simple queue service", params.get("q"));
    }
}
