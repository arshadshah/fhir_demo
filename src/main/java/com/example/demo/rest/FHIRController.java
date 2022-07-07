package com.example.demo.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.dto.PatientDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Questionnaire;
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
    public JsonNode convertDtoToFhir(@RequestBody PatientDto patient) throws JsonProcessingException {
        FhirContext ctx = FhirContext.forDstu3();
        IParser parser = ctx.newJsonParser().setPrettyPrint(true).setSuppressNarratives(true);
        Patient pt = parser.parseResource(Patient.class, objectMapper.writeValueAsString(patient));
        return objectMapper.readValue(parser.encodeResourceToString(pt),JsonNode.class);
    }

    @PostMapping(path = "fhir/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto convertFhirToDto(@RequestBody JsonNode patientstr) throws JsonProcessingException {
        FhirContext ctx = FhirContext.forDstu3();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println("patient ->" +patientstr.toString());
        Patient patient = parser.parseResource(Patient.class, patientstr.toPrettyString());
        System.out.println("patient ->" +patient.getName().get(0).getFamily());
        Questionnaire q = new Questionnaire();
        q.setStatus(Enumerations.PublicationStatus.ACTIVE);
        q.setId("q1");
        q.setTitle("title");
        q.setName("name");
        q.setDescription("descdesc");
        JsonNode json = JsonNodeFactory.instance.pojoNode(parser.encodeResourceToString(q));
        System.out.println("json is here -> "+json.toString());
        return objectMapper.readValue(parser.encodeResourceToString(patient),PatientDto.class);
    }


}
