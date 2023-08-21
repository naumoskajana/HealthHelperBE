package com.example.healthhelper.service.impl;

import com.example.healthhelper.dto.MedicationWithDiseasesDTO;
import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;
import com.example.healthhelper.repository.MedicationRepository;
import com.example.healthhelper.service.DiseaseService;
import com.example.healthhelper.service.MedicationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final DiseaseService diseaseService;

    public MedicationServiceImpl(MedicationRepository medicationRepository, @Lazy DiseaseService diseaseService) {
        this.medicationRepository = medicationRepository;
        this.diseaseService = diseaseService;
    }

    @Override
    public List<Medication> createAll(List<Medication> medications) {
        List<Medication> medicationsList = new ArrayList<>();
        medications.forEach(m -> {
            Medication medication = medicationRepository.findByName(m.getName());
            if (medication == null) {
                medication = new Medication();
                medication.setName(m.getName());
                medication.setDescription(m.getDescription());
                medication.setImage(m.getImage());
                medicationRepository.save(medication);
            }
            medicationsList.add(medication);
        });
        return medicationsList;
    }

    @Override
    public MedicationWithDiseasesDTO create(Medication searchedMedication, List<Disease> diseases) {
        Medication medication = medicationRepository.findByName(searchedMedication.getName());
        if (medication == null) {
            medication = new Medication();
            medication.setName(searchedMedication.getName());
            medication.setDescription(searchedMedication.getDescription());
            medication.setImage(searchedMedication.getImage());
            medicationRepository.save(medication);
        }

        List<Disease> diseasesFound = diseaseService.create(diseases, medication);

        MedicationWithDiseasesDTO medicationWithDiseasesDTO = new MedicationWithDiseasesDTO();
        medicationWithDiseasesDTO.setName(medication.getName());
        medicationWithDiseasesDTO.setDescription(medication.getDescription());
        medicationWithDiseasesDTO.setImage(medication.getImage());
        medicationWithDiseasesDTO.setDiseases(diseasesFound.stream().map(Disease::getAsDiseaseDTO).collect(Collectors.toList()));

        return medicationWithDiseasesDTO;
    }
}
