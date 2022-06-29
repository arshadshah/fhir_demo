package com.example.demo.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.dto.PatientDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class FHIRController {


    private final ObjectMapper objectMapper;

    public FHIRController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Patient convertDtoToFhir(@RequestBody PatientDto patient) throws JsonProcessingException {
        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();
        Patient pt= parser.parseResource(Patient.class, objectMapper.writeValueAsString(patient));
        parser.setPrettyPrint(true);
        return pt;
    }

    @PostMapping(path = "fhir/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto convertFhirToDto(@RequestBody JsonNode patientstr) throws JsonProcessingException {
        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println("patient ->" +patientstr.toString());
        Patient patient = ctx.newJsonParser().parseResource(Patient.class, patientstr.toPrettyString());
        System.out.println("patient ->" +patient.getName());
        return objectMapper.readValue(parser.encodeResourceToString(patient),PatientDto.class);
    }


}
