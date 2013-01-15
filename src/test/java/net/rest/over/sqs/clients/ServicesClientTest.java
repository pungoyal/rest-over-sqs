package net.rest.over.sqs.clients;

import com.amazonaws.util.json.JSONObject;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ServicesClientTest {

    private ServicesClient servicesClient;

    @Before
    public void setUp() throws Exception {
        servicesClient = new ServicesClient();
    }

    @Test
    @Ignore
    public void testGet() throws Exception {
        ClientResponse<String> response = servicesClient.get("http://localhost:3000/objects.json", new JSONObject());
        Assert.assertNotNull(response.getEntity());
    }

    @Test
    @Ignore
    public void testPost() throws Exception {
        JSONObject params = new JSONObject();
        params.put("param one", "value one");
        params.put("param two", "value two");
        params.put("param three", "value three");

        ClientResponse<String> response = servicesClient.post("http://localhost:3000/objects", params);
        Assert.assertNotNull(response.getEntity());
    }
}
