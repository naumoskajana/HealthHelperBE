package com.example.healthhelper.web;

import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;
import com.example.healthhelper.entity.User;
import com.example.healthhelper.service.DiseaseService;
import com.example.healthhelper.service.UserService;
import com.example.healthhelper.sparql.SparqlResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    private final DiseaseService diseaseService;
    private final UserService userService;

    public DiseaseController(DiseaseService diseaseService, UserService userService) {
        this.diseaseService = diseaseService;
        this.userService = userService;
    }

    @GetMapping("/fetchMedicationsForDisease")
    public ResponseEntity<?> fetchMedicationsForDisease(@RequestParam String disease) {
        String firstLetter = disease.substring(0, 1).toUpperCase();
        String restOfWord = disease.substring(1).toLowerCase();
        String diseaseName =  firstLetter + restOfWord;

        try {
            Disease diseaseWithInfo = SparqlResult.getDisease(diseaseName);
            List<Medication> medications = SparqlResult.getMedicationsForDisease(diseaseName);
            Disease searchedDisease = diseaseService.create(diseaseWithInfo, medications);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.addDisease(user, searchedDisease);
            return ResponseEntity.ok(searchedDisease);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
