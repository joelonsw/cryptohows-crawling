package com.example.crawler.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlackService {

    @Value("${slack.url}")
    private String slackUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSlackDeployMessage(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("text", message);
        restTemplate.postForObject(slackUrl, body, String.class);
    }
}
