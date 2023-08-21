package com.example.healthhelper.repository;

import com.example.healthhelper.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Disease findByName(String name);

}
