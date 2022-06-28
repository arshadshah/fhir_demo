package com.example.demo.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.dto.PatientDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Patient;
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
    public PatientDto convertDto(@RequestBody PatientDto patient) throws JsonProcessingException {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Patient pt= parser.parseResource(Patient.class, objectMapper.writeValueAsString(patient));
        parser.setPrettyPrint(true);
        System.out.println("data"+parser.encodeResourceToString(pt));
        return objectMapper.readValue(parser.encodeResourceToString(pt),PatientDto.class);
    }


}
