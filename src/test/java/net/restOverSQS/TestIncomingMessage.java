package net.restOverSQS;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.restOverSQS.domain.IncomingMessage;

public class TestIncomingMessage extends IncomingMessage {
    public TestIncomingMessage() throws JSONException {
        setVerb("get");
        setUri("http://www.google.com/");
        setResponseTopicName("rest-over-sqs-response-test");

        JSONObject params = new JSONObject();
        params.put("q", "amazon simple queue service");
        setParams(params);
    }
}
