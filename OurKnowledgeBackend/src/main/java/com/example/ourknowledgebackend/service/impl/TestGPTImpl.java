package com.example.ourknowledgebackend.service.impl;

import com.example.ourknowledgebackend.infrastructure.OpenAIService;
import com.example.ourknowledgebackend.service.TestGPT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestGPTImpl implements TestGPT {

    private final ObjectMapper objectMapper;
    private final OpenAIService openAIService;

    private Map<String, Object> jsonRequest;

    @PostConstruct
    public void init() throws IOException {
        jsonRequest = objectMapper.readValue(new ClassPathResource("openAIPrompts/FileTechnologies.json").getInputStream(), Map.class);
    }

    public void llamada() {
        Map<String, Object> result = openAIService.askGPT(jsonRequest);

        System.out.println(result);


    }
}