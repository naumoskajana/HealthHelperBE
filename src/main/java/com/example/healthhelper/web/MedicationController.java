package com.example.healthhelper.web;

import com.example.healthhelper.dto.MedicationWithDiseasesDTO;
import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;
import com.example.healthhelper.service.MedicationService;
import com.example.healthhelper.sparql.SparqlResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/fetchDiseasesForMedication")
    public ResponseEntity<?> fetchDiseasesForMedication(@RequestParam String medication) {
        String firstLetter = medication.substring(0, 1).toUpperCase();
        String restOfWord = medication.substring(1).toLowerCase();
        String medicationName =  firstLetter + restOfWord;

        try {
            Medication medicationWithInfo = SparqlResult.getMedication(medicationName);
            List<Disease> diseases = SparqlResult.getDiseasesForMedication(medicationName);
            MedicationWithDiseasesDTO searchedMedication = medicationService.create(medicationWithInfo, diseases);
            return ResponseEntity.ok(searchedMedication);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
