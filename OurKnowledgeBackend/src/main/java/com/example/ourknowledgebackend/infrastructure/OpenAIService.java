package com.example.ourknowledgebackend.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String openaiUrl;

    public OpenAIService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> askGPT(Map<String, Object> prompt) {
        String url = openaiUrl + "/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(prompt, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );
            return extractContentFromResponse(response.getBody());
        } catch (HttpStatusCodeException e) {
            System.err.println("Error during askGPT request: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error during askGPT request: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> extractContentFromResponse(Map<String, Object> response) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String content = (String) message.get("content");
        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        return result;
    }
}
