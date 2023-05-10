package com.example.hl7demo.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class HL7ResponseParser {

    private final ObjectMapper objectMapper;

    public HL7ResponseParser() {
        objectMapper = new ObjectMapper();
    }

    public String parseResponse(String hl7Response) {
        // Parse the HL7 response and convert it to JSON format
        // In this example, we just create a JSON object with a single field
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("response", hl7Response);

        return jsonResponse.toString();
    }
}
