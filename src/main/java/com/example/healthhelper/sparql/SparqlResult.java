package com.example.healthhelper.sparql;

import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.Medication;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SparqlResult {

    public static Disease getDisease(String disease){
        Disease foundDisease = new Disease();

        String sparqlQueryString = SparqlQueries.diseaseQuery(disease);
        Query query = QueryFactory.create(sparqlQueryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = queryExecution.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String diseaseDescription = solution.getLiteral("diseaseDescription").getString();
                String diseaseImage = solution.getResource("diseaseImage").getURI();

                foundDisease.setName(disease);
                foundDisease.setDescription(diseaseDescription);
                foundDisease.setImage(diseaseImage);
            }
        }

        return foundDisease;
    }

    public static List<Medication> getMedicationsForDisease(String disease){
        List<Medication> medications = new ArrayList<>();

        String sparqlQueryString = SparqlQueries.medicationsForDiseaseQuery(disease);
        Query query = QueryFactory.create(sparqlQueryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = queryExecution.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String medicationName = solution.getLiteral("medicationName").getString();
                String medicationDescription = solution.getLiteral("medicationDescription").getString();
                String medicationImage = solution.getResource("medicationImage").getURI();

                Medication medication = new Medication();
                medication.setName(medicationName);
                medication.setDescription(medicationDescription);
                medication.setImage(medicationImage);
                medications.add(medication);
            }
        }

        return medications;
    }

    public static Medication getMedication(String medication){
        Medication foundMedication = new Medication();

        String sparqlQueryString = SparqlQueries.medicationQuery(medication);
        Query query = QueryFactory.create(sparqlQueryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = queryExecution.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String medicationDescription = solution.getLiteral("medicationDescription").getString();
                String medicationImage = solution.getResource("medicationImage").getURI();

                foundMedication.setName(medication);
                foundMedication.setDescription(medicationDescription);
                foundMedication.setImage(medicationImage);
            }
        }

        return foundMedication;
    }

    public static List<Disease> getDiseasesForMedication(String medication){
        List<Disease> diseases = new ArrayList<>();

        String sparqlQueryString = SparqlQueries.diseasesForMedicationQuery(medication);
        Query query = QueryFactory.create(sparqlQueryString);
        try (QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = queryExecution.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String diseaseName = solution.getLiteral("diseaseName").getString();
                String diseaseDescription = solution.getLiteral("diseaseDescription").getString();
                String diseaseImage = solution.getResource("diseaseImage").getURI();

                Disease disease = new Disease();
                disease.setName(diseaseName);
                disease.setDescription(diseaseDescription);
                disease.setImage(diseaseImage);
                diseases.add(disease);
            }
        }

        return diseases;
    }

}
