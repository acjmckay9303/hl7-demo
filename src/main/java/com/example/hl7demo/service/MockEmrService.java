package com.example.hl7demo.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.parser.PipeParser;
import org.springframework.stereotype.Service;

@Service
public class MockEmrService {

    public String processHL7Message(String hl7Message) {
        PipeParser parser = new PipeParser();
        StringBuilder response = new StringBuilder();

        try {
            // Parse incoming HL7 message
            Message message = parser.parse(hl7Message);

            // Extract some data from the message
            MSH mshSegment = (MSH) message.get("MSH");
            String messageControlId = mshSegment.getMessageControlID().getValue();

            // Build a mock response using extracted data
            response.append("MSA|AA|");
            response.append(messageControlId);

        } catch (HL7Exception e) {
            e.printStackTrace();
            response.append("MSA|AE|Error: Unable to process HL7 message");
        }

        return response.toString();
    }
}
