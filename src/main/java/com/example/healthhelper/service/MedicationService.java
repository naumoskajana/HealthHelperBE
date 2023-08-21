package com.example.healthhelper.service;

import com.example.healthhelper.dto.MedicationWithDiseasesDTO;
import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;

import java.util.List;

public interface MedicationService {

    List<Medication> createAll(List<Medication> medications);
    MedicationWithDiseasesDTO create(Medication searchedMedication, List<Disease> diseases);

}
