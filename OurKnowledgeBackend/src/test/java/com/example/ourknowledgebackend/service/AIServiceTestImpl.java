package com.example.ourknowledgebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AIServiceTestImpl {

    @Autowired
    private AIService AIService;

    // Test only manually
    //@Test
    //void llamada() {
    //    AIService.llamada();
    //}
}