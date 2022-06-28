package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PatientDto {

    private String resourceType;
    private ArrayList<NameDto> name;
}
