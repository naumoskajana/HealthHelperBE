package com.example.healthhelper.rdf;

import com.example.healthhelper.entity.User;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;

public class RdfModelReport {

    public static Model getReportModel(User user) {
        Model model = ModelFactory.createDefaultModel();

        String ex = "http://example.org/";

        Resource diseaseClass = model.createResource(ex + "Disease");
        Resource medicationClass = model.createResource(ex + "Medication");
        Resource userResource = model.createResource(ex + user.getFullName());

        Property canBeCuredWithProperty = model.createProperty(ex + "canBeCuredWith");
        Property hasDiseaseProperty = model.createProperty(ex + "hasDisease");

        diseaseClass.addProperty(RDF.type, RDFS.Class);
        medicationClass.addProperty(RDF.type, RDFS.Class);
        userResource.addProperty(RDF.type, model.createResource(ex + "User"));

        canBeCuredWithProperty.addProperty(RDF.type, RDF.Property);
        canBeCuredWithProperty.addProperty(RDFS.domain, medicationClass);
        canBeCuredWithProperty.addProperty(RDFS.range, diseaseClass);

        hasDiseaseProperty.addProperty(RDF.type, RDF.Property);
        hasDiseaseProperty.addProperty(RDFS.domain, userResource);
        hasDiseaseProperty.addProperty(RDFS.range, diseaseClass);

        user.getDiseases().forEach(d -> {
            Resource diseaseResource = model.createResource(ex + d.getName());
            diseaseResource.addProperty(RDF.type, diseaseClass);
            userResource.addProperty(hasDiseaseProperty, diseaseResource);

            d.getMedications().forEach(m -> {
                Resource medicationResource = model.createResource(ex + m.getName());
                medicationResource.addProperty(RDF.type, medicationClass);
                diseaseResource.addProperty(canBeCuredWithProperty, medicationResource);
            });
        });

        Model shapesModel = loadSHACLShapes();
        model.add(shapesModel);

        return model;
    }

    public static Model loadSHACLShapes() {
        Model model = ModelFactory.createDefaultModel();
        try (InputStream inputStream = FileManager.get().open("shapes.ttl")) {
            RDFParser.source(inputStream).lang(RDFLanguages.TURTLE).parse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
