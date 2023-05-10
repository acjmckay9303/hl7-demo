package com.example.hl7demo.converter;

import com.example.hl7demo.model.HL7Message;
import ca.uhn.hl7v2.model.v25.message.OMI_O23;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JsonToHL7ConverterTest {

    @InjectMocks
    private JsonToHL7Converter jsonToHL7Converter;

    private HL7Message hl7Message;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        hl7Message = new HL7Message();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date dateOfBirth = dateFormat.parse("1985-01-01");
        Date studyDateTime = dateTimeFormat.parse("2023-05-10T10:00:00");
        Date orderDateTime = dateTimeFormat.parse("2023-05-09T15:30:00");
        Date observationRequestDateTime = dateTimeFormat.parse("2023-05-09T16:00:00");

        // Initialize HL7Message with provided JSON data, using Date objects instead of strings
        hl7Message.setPatient(new HL7Message.Patient("P123456", "John", "Doe", dateOfBirth, "M", new HL7Message.Address("123 Main St", "Springfield", "IL", "12345", "USA"), "555-555-1234", "johndoe@example.com"));
        hl7Message.setReferringVet(new HL7Message.Vet("Jane", "Smith", "1234567890"));
        hl7Message.setPerformingVet(new HL7Message.Vet("Michael", "Johnson", "0987654321"));
        hl7Message.setStudy(new HL7Message.Study("123", studyDateTime, "X-ray of the chest"));
        hl7Message.setOrder(new HL7Message.Order("O123", orderDateTime));
        hl7Message.setObservationRequest(new HL7Message.ObservationRequest("123", observationRequestDateTime, "X-ray of the chest"));
    }

    @Test
    void convert() {
        OMI_O23 message = jsonToHL7Converter.convert(hl7Message);
    }

    @Test
    void getHL7StrMessage() {
        OMI_O23 message = jsonToHL7Converter.convert(hl7Message);
        String hl7StrMessage = jsonToHL7Converter.getHL7StrMessage(message);

        assertNotNull(hl7StrMessage);
        assertTrue(hl7StrMessage.startsWith("MSH|^~\\&|"));
    }

}
