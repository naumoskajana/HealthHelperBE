package com.example.healthhelper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseDTO {

    private String name;
    private String description;
    private String image;

}
