package com.example.healthhelper.sparql;

public class SparqlQueries {

    public static String diseaseQuery(String disease){
        return "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT DISTINCT ?diseaseDescription ?diseaseImage\n" +
                "WHERE {\n" +
                "  ?disease rdfs:label '" + disease + "'@en .\n" +
                "  ?disease dbo:abstract ?diseaseDescriptionRaw .\n" +
                "  ?disease dbo:thumbnail ?diseaseImage .\n" +
                "  FILTER (LANG(?diseaseDescriptionRaw) = 'en')\n" +
                "  BIND(STR(?diseaseDescriptionRaw) AS ?diseaseDescription)\n" +
                "}";
    }

    public static String medicationsForDiseaseQuery(String disease) {
        return "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT DISTINCT ?medicationName ?medicationDescription ?medicationImage\n" +
                "WHERE {\n" +
                "  ?disease rdfs:label '" + disease + "'@en;\n" +
                "  dbo:medication ?medicationResource .\n" +
                "  ?medicationResource rdfs:label ?medicationNameRaw .\n" +
                "  ?medicationResource dbo:abstract ?medicationDescriptionRaw .\n" +
                "  ?medicationResource dbo:thumbnail ?medicationImage .\n" +
                "  FILTER (LANG(?medicationNameRaw) = 'en')\n" +
                "  FILTER (LANG(?medicationDescriptionRaw) = 'en')\n" +
                "  BIND(STR(?medicationNameRaw) AS ?medicationName)\n" +
                "  BIND(STR(?medicationDescriptionRaw) AS ?medicationDescription)\n" +
                "}";
    }

    public static String medicationQuery(String medication){
        return "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT DISTINCT ?medicationDescription ?medicationImage\n" +
                "WHERE {\n" +
                "  ?disease dbp:medication ?medicationResource .\n" +
                "  ?medicationResource rdfs:label '" + medication + "'@en .\n" +
                "  ?medicationResource dbo:abstract ?medicationDescriptionRaw .\n" +
                "  ?medicationResource dbo:thumbnail ?medicationImage .\n" +
                "  FILTER (LANG(?medicationDescriptionRaw) = 'en')\n" +
                "  BIND(STR(?medicationDescriptionRaw) AS ?medicationDescription)\n" +
                "}";
    }

    public static String diseasesForMedicationQuery(String medication){
        return "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT DISTINCT ?diseaseName ?diseaseDescription ?diseaseImage\n" +
                "WHERE {\n" +
                "  ?disease dbp:medication ?medicationResource .\n" +
                "  ?medicationResource rdfs:label '" + medication + "'@en .\n" +
                "  ?disease rdfs:label ?diseaseLabel .\n" +
                "  ?disease dbo:thumbnail ?diseaseImage .\n" +
                "  ?disease dbo:abstract ?diseaseDescriptionRaw .\n" +
                "  FILTER (LANG(?diseaseLabel) = 'en')\n" +
                "  FILTER (LANG(?diseaseDescriptionRaw) = 'en')\n" +
                "  BIND(STR(?diseaseLabel) AS ?diseaseName)\n" +
                "  BIND(STR(?diseaseDescriptionRaw) AS ?diseaseDescription)\n" +
                "}";

    }

}
