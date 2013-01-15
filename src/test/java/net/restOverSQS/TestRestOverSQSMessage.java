package net.restOverSQS;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.restOverSQS.domain.RestOverSQSMessage;

public class TestRestOverSQSMessage extends RestOverSQSMessage {
    public TestRestOverSQSMessage() throws JSONException {
        setVerb("get");
        setUri("http://www.google.com/");

        JSONObject params = new JSONObject();
        params.put("q", "amazon simple queue service");
        setParams(params);
    }
}
