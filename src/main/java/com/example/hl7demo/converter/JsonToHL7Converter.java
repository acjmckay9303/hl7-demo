package com.example.hl7demo.converter;

import ca.uhn.hl7v2.model.v25.datatype.XAD;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.group.OMI_O23_ORDER;
import ca.uhn.hl7v2.model.v25.segment.*;
import com.example.hl7demo.model.HL7Message;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.group.OMI_O23_PATIENT;
import ca.uhn.hl7v2.model.v25.message.OMI_O23;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class JsonToHL7Converter {

    private static final Logger log = LoggerFactory.getLogger(JsonToHL7Converter.class);

    public OMI_O23 convert(HL7Message hl7Message) {
        OMI_O23 message = new OMI_O23();

        try {
            // Set the MSH segment values
            MSH msh = message.getMSH();
            setMessageHeader(msh);

            // Set the message timestamp (current time)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            msh.getDateTimeOfMessage().getTime().setValue(sdf.format(new Date()));

            // Set the HL7 version number
            msh.getVersionID().getVersionID().setValue("2.5");

            OMI_O23_PATIENT patient = message.getPATIENT();

            if (patient != null) {
                log.debug("Setting patient data");
                setPatientData(hl7Message, patient);
            }

            if (patient != null) {
                log.debug("Setting referring Vet Data");
                setReferringVetData(hl7Message, patient);
            }

            if (patient != null) {
                log.debug("Setting performing Vet data");
                setPerformingVet(hl7Message, patient);
            }

            log.debug("Setting Study data");
            setStudyData(hl7Message, message);

            log.debug("Setting ORC data");
            setORCSegment(hl7Message, message);

            log.debug("Setting owner data");
            // setOwnerData(hl7Message, patient);

            log.debug("Setting OBR data");
            setOBRSegment(hl7Message, message);

            // log.debug("setting ZDS data");
            // setZDSSegment(hl7Message,message);
        } catch (HL7Exception e) {
            log.error("Error while converting JSON to HL7 message", e);
        }

        return message;
    }

    public String getHL7StrMessage(OMI_O23 message) {

        String result = null;
        String FLD_SEP = "|";
        String ENC_CHAR = "^~\\&";

        try {
            HapiContext hapiContext = new DefaultHapiContext();
            message.getMSH().getFieldSeparator().setValue(FLD_SEP);
            message.getMSH().getEncodingCharacters().setValue(ENC_CHAR);

            log.debug("Parsing and encoding the OMI_O23 message");
            Parser parser = hapiContext.getPipeParser();

            result = parser.encode(message);
            log.debug(result);
        } catch (HL7Exception e) {
            log.error("Error while converting HL7 message to string", e);
        }
        return result;
    }

    @SneakyThrows
    private void setMessageHeader(MSH msh) {
        msh.getFieldSeparator().setValue("|");
        msh.getEncodingCharacters().setValue("^~\\&");
        msh.getSendingApplication().getNamespaceID().setValue("YourSendingApp");
        msh.getReceivingApplication().getNamespaceID().setValue("YourReceivingApp");
        msh.getDateTimeOfMessage().getTime().setValue(getCurrentTimeStamp());
        msh.getMessageType().getMessageCode().setValue("OMI");
        msh.getMessageType().getTriggerEvent().setValue("O23");
        msh.getMessageType().getMessageStructure().setValue("OMI_O23");
        msh.getMessageControlID().setValue(generateMessageControlID());
        msh.getProcessingID().getProcessingID().setValue("P");
        msh.getVersionID().getVersionID().setValue("2.5");
    }


    private void setPatientData(HL7Message hl7Message, OMI_O23_PATIENT patient) throws HL7Exception {
        PID pid = patient.getPID();
        pid.getPatientID().getIDNumber().setValue(hl7Message.getPatient().getPatientId());
        pid.getPatientName(0).getFamilyName().getSurname().setValue(hl7Message.getPatient().getLastName());
        pid.getPatientName(0).getGivenName().setValue(hl7Message.getPatient().getFirstName());
        pid.getDateTimeOfBirth().getTime().setValue(new SimpleDateFormat("yyyyMMdd").format(hl7Message.getPatient().getDateOfBirth()));
        pid.getAdministrativeSex().setValue(hl7Message.getPatient().getGender());
        // Set the address, phone number, and email
        XAD xad = pid.getPatientAddress(0);
        xad.getStreetAddress().getStreetOrMailingAddress().setValue(hl7Message.getPatient().getAddress().getStreet());
        xad.getCity().setValue(hl7Message.getPatient().getAddress().getCity());
        xad.getStateOrProvince().setValue(hl7Message.getPatient().getAddress().getState());
        xad.getZipOrPostalCode().setValue(hl7Message.getPatient().getAddress().getPostalCode());
        xad.getCountry().setValue(hl7Message.getPatient().getAddress().getCountry());
        pid.getPhoneNumberHome(0).getTelephoneNumber().setValue(hl7Message.getPatient().getPhoneNumber());
    }


    private void setReferringVetData(HL7Message hl7Message, OMI_O23_PATIENT patient) throws HL7Exception {
        PV1 pv1 = patient.getPATIENT_VISIT().getPV1();
        XCN referringVet = pv1.getReferringDoctor(0);
        referringVet.getIDNumber().setValue(hl7Message.getReferringVet().getNpi());
        referringVet.getFamilyName().getSurname().setValue(hl7Message.getReferringVet().getLastName());
        referringVet.getGivenName().setValue(hl7Message.getReferringVet().getFirstName());
    }


    private void setPerformingVet(HL7Message hl7Message, OMI_O23_PATIENT patient) throws HL7Exception {
        PV1 pv1 = patient.getPATIENT_VISIT().getPV1();
        XCN performingVet = pv1.getAttendingDoctor(0);
        performingVet.getIDNumber().setValue(hl7Message.getPerformingVet().getNpi());
        performingVet.getFamilyName().getSurname().setValue(hl7Message.getPerformingVet().getLastName());
        performingVet.getGivenName().setValue(hl7Message.getPerformingVet().getFirstName());
    }


    private void setStudyData(HL7Message hl7Message, OMI_O23 message) throws HL7Exception {
        OMI_O23_ORDER order = message.getORDER();
        OBR obr = order.getOBR();

        obr.getSetIDOBR().setValue(hl7Message.getStudy().getStudyId());
        obr.getRequestedDateTime().getTime().setValue(hl7Message.getStudy().getStudyDateTime());
        obr.getUniversalServiceIdentifier().getIdentifier().setValue(hl7Message.getStudy().getStudyDescription());
    }


    private void setORCSegment(HL7Message hl7Message, OMI_O23 message) throws HL7Exception {
        OMI_O23_ORDER order = message.getORDER();
        ORC orc = order.getORC();

        orc.getOrderControl().setValue("NW");
        orc.getPlacerOrderNumber().getEntityIdentifier().setValue(hl7Message.getOrder().getOrderId());
        orc.getOrderControlCodeReason().getIdentifier().setValue("New order");
        orc.getDateTimeOfTransaction().getTime().setValue(hl7Message.getOrder().getOrderDateTime());
    }


    private void setOBRSegment(HL7Message hl7Message, OMI_O23 message) throws HL7Exception {
        OMI_O23_ORDER order = message.getORDER();
        OBR obr = order.getOBR();

        obr.getSetIDOBR().setValue(hl7Message.getObservationRequest().getObservationRequestId());
        obr.getRequestedDateTime().getTime().setValue(hl7Message.getObservationRequest().getObservationRequestDateTime());
        obr.getUniversalServiceIdentifier().getIdentifier().setValue(hl7Message.getObservationRequest().getObservationRequestDescription());
    }


    private String generateMessageControlID() {
        return UUID.randomUUID().toString();
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }


}
