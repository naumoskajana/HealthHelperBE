package com.example.healthhelper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationWithDiseasesDTO {

    private String name;
    private String description;
    private String image;
    private List<DiseaseDTO> diseases;

}
