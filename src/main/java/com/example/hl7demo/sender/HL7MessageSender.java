package com.example.hl7demo.sender;

import com.example.hl7demo.service.MockEmrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HL7MessageSender {

    private final MockEmrService mockEmrService;

    @Autowired
    public HL7MessageSender(MockEmrService mockEmrService) {
        this.mockEmrService = mockEmrService;
    }

    public String sendMessage(String hl7Message) {
        // Simulate sending the HL7 message to the mock EMR system and receiving a response
        String hl7Response = mockEmrService.processHL7Message(hl7Message);
        return hl7Response;
    }
}
