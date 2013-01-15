package net.rest.over.sqs.domain.stubs;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.rest.over.sqs.domain.IncomingMessage;

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
