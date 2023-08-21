package com.example.healthhelper.repository;

import com.example.healthhelper.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    Medication findByName(String name);

}
