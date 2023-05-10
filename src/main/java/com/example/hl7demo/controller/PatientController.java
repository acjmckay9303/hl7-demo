package com.example.hl7demo.controller;

import com.example.hl7demo.model.HL7Message;
import com.example.hl7demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/send-hl7")
    public ResponseEntity<String> sendHL7Message(@RequestBody HL7Message hl7Message) {
        try {
            String jsonResponse = patientService.processPatientJson(hl7Message);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing HL7 message: " + e.getMessage());
        }
    }
}
