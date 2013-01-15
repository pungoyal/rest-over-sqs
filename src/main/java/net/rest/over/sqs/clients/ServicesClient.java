package net.rest.over.sqs.clients;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class ServicesClient {
    private final static Logger logger = LoggerFactory.getLogger(ServicesClient.class);

    public ClientResponse<String> get(String uri, JSONObject params) throws JSONException {
        ClientRequest request = new ClientRequest(uri);

        Iterator keys = params.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            request.queryParameter(key, params.get(key));
        }

        ClientResponse<String> response;
        try {
            response = request.get(String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClientResponse<String> post(String uri, JSONObject params) {
        ClientRequest request = new ClientRequest(uri);
        request.body("application/json", params.toString());
        logger.info("posting params : {} to {} ", request.getBody(), uri);
        try {
            return request.post(String.class);
        } catch (Exception e) {
            logger.error("Error occurred while processing request ", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
