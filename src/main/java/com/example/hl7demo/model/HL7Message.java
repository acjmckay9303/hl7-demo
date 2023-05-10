package com.example.hl7demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HL7Message {

    // Patient information
    private Patient patient;

    // Referring veterinarian information
    private Vet referringVet;

    // Performing veterinarian information
    private Vet performingVet;

    // Study information
    private Study study;

    // Order information
    private Order order;

    // Observation request information
    private ObservationRequest observationRequest;

    @Data
    @AllArgsConstructor
    public static class Patient {
        private String patientId;
        private String firstName;
        private String lastName;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date dateOfBirth;
        private String gender;
        private Address address;
        private String phoneNumber;
        private String email;

    }
    @Data
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;

    }
    @Data
    @AllArgsConstructor
    public static class Vet {
        private String firstName;
        private String lastName;
        private String npi;

    }
    @Data
    @AllArgsConstructor
    public static class Study {
        private String studyId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date studyDateTime;
        private String studyDescription;

    }
    @Data
    @AllArgsConstructor
    public static class Order {
        private String orderId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date orderDateTime;

    }
    @Data
    @AllArgsConstructor
    public static class ObservationRequest {
        private String observationRequestId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date observationRequestDateTime;
        private String observationRequestDescription;

    }
}
