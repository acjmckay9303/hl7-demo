package com.example.hl7demo.service;

import com.example.hl7demo.converter.JsonToHL7Converter;
import com.example.hl7demo.model.HL7Message;
import com.example.hl7demo.sender.HL7MessageSender;
import com.example.hl7demo.parser.HL7ResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.uhn.hl7v2.model.v25.message.OMI_O23;

@Service
public class PatientService {

    private final JsonToHL7Converter jsonToHL7Converter;
    private final HL7MessageSender hl7MessageSender;
    private final HL7ResponseParser hl7ResponseParser;

    @Autowired
    public PatientService(JsonToHL7Converter jsonToHL7Converter, HL7MessageSender hl7MessageSender, HL7ResponseParser hl7ResponseParser) {
        this.jsonToHL7Converter = jsonToHL7Converter;
        this.hl7MessageSender = hl7MessageSender;
        this.hl7ResponseParser = hl7ResponseParser;
    }

    public String processPatientJson(HL7Message hl7Message) {
        // Convert HL7Message object to HL7 ADT message
        OMI_O23 hl7AdtMessage = jsonToHL7Converter.convert(hl7Message);

        // Convert OMI_O23 message to a string
        String hl7AdtMessageStr = jsonToHL7Converter.getHL7StrMessage(hl7AdtMessage);

        // Send HL7 message to EMR and receive response
        String hl7Response = hl7MessageSender.sendMessage(hl7AdtMessageStr);

        // Parse HL7 response and convert it to JSON format
        String jsonResponse = hl7ResponseParser.parseResponse(hl7Response);

        return jsonResponse;
    }
}
