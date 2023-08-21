package com.example.healthhelper.service;

import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;

import java.util.List;

public interface DiseaseService {

    Disease create(Disease searchedDisease, List<Medication> medications);
    List<Disease> create(List<Disease> searchedDiseases, Medication medication);

}
