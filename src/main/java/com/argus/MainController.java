package com.argus;

import com.argus.config.ServerProperties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(value = "/api")
public class MainController {

    private final ServerProperties serverProperties;

    @Autowired
    public MainController(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    private static String SAVED_RESOURCE = "";

    @GetMapping("/alive")
    boolean isAlive() {
        return true;
    }

    @GetMapping("/resource")
    String getResource() {
        return SAVED_RESOURCE;
    }

    @PostMapping(path = "/resource", consumes = MediaType.APPLICATION_JSON_VALUE)
    String postResource(@RequestBody String json, @RequestHeader HttpHeaders headers) {
        //I tried to use Eureka but I got some troubles when I run it, so I decided to send a simple GET request for other service
        if (headers.containsKey("stop-forwarding")) {
            SAVED_RESOURCE = json;
            return "Success!";
        }

        try {
            forwardPostRequest(json);
        } catch (IOException ex) {
            return ex.toString();
        }
        return "Resource forwarded";
    }

    private HttpEntity forwardPostRequest(String body) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://" +
                serverProperties.getPairHost() + ":" +
                serverProperties.getPairPort() +
                "/api/resource");

        httppost.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httppost.addHeader("stop-forwarding", Boolean.toString(true));
        httppost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

        HttpResponse response = httpclient.execute(httppost);
        return response.getEntity();
    }
}
