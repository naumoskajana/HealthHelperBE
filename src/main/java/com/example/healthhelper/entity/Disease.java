package com.example.healthhelper.entity;

import com.example.healthhelper.dto.DiseaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "disease_medication",
            joinColumns = @JoinColumn(name = "disease_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id"))
    private List<Medication> medications;

    @JsonIgnore
    public DiseaseDTO getAsDiseaseDTO(){
        DiseaseDTO diseaseDTO = new DiseaseDTO();
        diseaseDTO.setName(this.name);
        diseaseDTO.setDescription(this.description);
        diseaseDTO.setImage(this.image);
        return diseaseDTO;
    }

}
