package com.example.healthhelper.service.impl;

import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;
import com.example.healthhelper.repository.DiseaseRepository;
import com.example.healthhelper.service.DiseaseService;
import com.example.healthhelper.service.MedicationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final MedicationService medicationService;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository, MedicationService medicationService) {
        this.diseaseRepository = diseaseRepository;
        this.medicationService = medicationService;
    }

    @Override
    public Disease create(Disease searchedDisease, List<Medication> medications) {
        List<Medication> diseaseMedications = medicationService.createAll(medications);

        Disease disease = diseaseRepository.findByName(searchedDisease.getName());
        if (disease == null) {
            disease = new Disease();
            disease.setName(searchedDisease.getName());
            disease.setDescription(searchedDisease.getDescription());
            disease.setImage(searchedDisease.getImage());
            disease.setMedications(diseaseMedications);
        }
        else{
            List<Medication> medicationsToAdd = new ArrayList<>();
            Disease finalDisease = disease;
            diseaseMedications.forEach(newMedication -> {
                boolean foundMedication = finalDisease.getMedications().stream()
                        .anyMatch(oldMedication -> oldMedication.getName().equals(newMedication.getName()));
                if (!foundMedication){
                    medicationsToAdd.add(newMedication);
                }
            });
            if (!medicationsToAdd.isEmpty()){
                disease.getMedications().addAll(medicationsToAdd);
                diseaseRepository.save(disease);
            }
        }

        return diseaseRepository.save(disease);
    }

    @Override
    public List<Disease> create(List<Disease> searchedDiseases, Medication medication) {
        List<Disease> diseases = new ArrayList<>();

        searchedDiseases.forEach(d -> {
            Disease disease = diseaseRepository.findByName(d.getName());
            if (disease == null) {
                disease = new Disease();
                disease.setName(d.getName());
                disease.setDescription(d.getDescription());
                disease.setImage(d.getImage());
                List<Medication> diseaseMedications = new ArrayList<>();
                diseaseMedications.add(medication);
                disease.setMedications(diseaseMedications);
                diseaseRepository.save(disease);
            }
            else {
                boolean foundMedication = disease.getMedications().stream()
                        .anyMatch(m -> m.getName().equals(medication.getName()));
                if (!foundMedication) {
                    disease.getMedications().add(medication);
                }
            }
            diseases.add(disease);
        });

        return diseases;
    }
}
