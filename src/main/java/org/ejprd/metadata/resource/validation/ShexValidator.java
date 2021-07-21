package org.ejprd.metadata.resource.validation;

//import fr.inria.lille.shexjava.schema.Label;
//import fr.inria.lille.shexjava.schema.ShexSchema;
//import fr.inria.lille.shexjava.schema.parsing.GenParser;
//import fr.inria.lille.shexjava.shapeMap.BaseShapeMap;
//import fr.inria.lille.shexjava.shapeMap.ResultShapeMap;
//import fr.inria.lille.shexjava.shapeMap.parsing.ShapeMapParsing;
//import fr.inria.lille.shexjava.validation.RecursiveValidation;
//import fr.inria.lille.shexjava.validation.ValidationAlgorithmAbstract;
//import org.apache.commons.rdf.api.Graph;
//import org.apache.commons.rdf.api.RDF;
//import org.apache.commons.rdf.api.RDFTerm;
//import org.apache.commons.rdf.rdf4j.RDF4J;
//import org.apache.commons.rdf.simple.SimpleRDF;
//import org.eclipse.rdf4j.model.Model;
//import org.eclipse.rdf4j.rio.RDFFormat;
//import org.eclipse.rdf4j.rio.Rio;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Paths;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ShexValidator {
//    private static Logger logger = LoggerFactory.getLogger(ShexValidator.class);
//    private final static RDF rdfFactory = new SimpleRDF();
//
//    @Deprecated
//    public static boolean validate(File fileToValidate) {
//        ShexSchema shexSchema = null;
//        org.eclipse.rdf4j.model.Model dataModel = null;
//        try {
//            shexSchema = GenParser.parseSchema(rdfFactory, Paths.get( "/home/henriette/ebi-dev/ejprd/shex-java/example/student.shex"));
//            InputStream inputStream = new FileInputStream(fileToValidate);
//            dataModel = Rio.parse(inputStream, fileToValidate.getAbsolutePath(),
//                    Rio.getParserFormatForFileName(fileToValidate.getAbsolutePath()).orElse(RDFFormat.RDFXML));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Graph rdfGraph = new RDF4J().asGraph(dataModel);
//        ValidationAlgorithmAbstract shexValidator = new RecursiveValidation(shexSchema, rdfGraph);
//        RDFTerm focusNode = rdfFactory.createIRI("http://example.com/users/Student1");
//        Label shapeLabel = new Label(rdfFactory.createIRI("http://school.example/#Enrollee"));
//        boolean result = shexValidator.validate(focusNode, shapeLabel);
//        return result;
//    }
//
//    public static boolean validate(String fileToValidateName, String shexFileName, String strFocusNode,
//                                   String strShapeLabel) {
//        File fileToValidate = new File(fileToValidateName);
//        return validate(fileToValidate, shexFileName, strFocusNode, strShapeLabel);
//    }
//
//    public static boolean validate(File fileToValidate, String shexFileName, String strFocusNode,
//                                   String strShapeLabel) {
//
//        ShexSchema shexSchema = getShexSchema(shexFileName);
//        Graph rdfGraph =  getRDFGraph(fileToValidate.getAbsolutePath());
//        ValidationAlgorithmAbstract shexValidator = new RecursiveValidation(shexSchema, rdfGraph);
//        RDFTerm focusNode = rdfFactory.createIRI(strFocusNode);
//        Label shapeLabel = new Label(rdfFactory.createIRI(strShapeLabel));
//        boolean result = shexValidator.validate(focusNode, shapeLabel);
//        return result;
//    }
//
//    public static ResultShapeMap validate(String fileToValidateName, String shexFileName, String shapeMapFileName) {
//        File fileToValidate = new File(fileToValidateName);
//        return validate(fileToValidate, shexFileName, shapeMapFileName);
//    }
//
//    public static ResultShapeMap validate(File fileToValidate, String shexFileName, String shapeMapFileName) {
//
//        ShexSchema shexSchema = getShexSchema(shexFileName);
//        Graph rdfGraph =  getRDFGraph(fileToValidate.getAbsolutePath());
//        ValidationAlgorithmAbstract shexValidator = new RecursiveValidation(shexSchema, rdfGraph);
//        BaseShapeMap shapeMap = getShapeMap(shapeMapFileName);
//        ResultShapeMap resultShapeMap = shexValidator.validate(shapeMap);
//        return resultShapeMap;
//    }
//
//    private static ShexSchema getShexSchema(String shexFileName) {
//        ShexSchema schema;
//        try {
//            schema = GenParser.parseSchema(rdfFactory,Paths.get(shexFileName));
//        } catch (IOException e) {
//            logger.error("Error reading the shex schema file:" + shexFileName, e);
//            return null;
//        } catch (Exception e) {
//            logger.error("Error while parsing the schema file. Caused by: " + e);
//            return null;
//        }
//        return schema;
//    }
//
//    private static Graph getRDFGraph(String dataFileName) {
//        Model dataModel;
//        try {
//            InputStream inputStream = new FileInputStream(dataFileName);
//            dataModel = Rio.parse(inputStream, dataFileName,
//                    Rio.getParserFormatForFileName(dataFileName).orElse(RDFFormat.RDFXML));
//        } catch (Exception e) {
//            logger.error("Error while reading the data file: " + dataFileName, e);
//            return null;
//        }
//        Graph rdfGraph = new RDF4J().asGraph(dataModel);
//        return rdfGraph;
//    }
//
//    private static Graph getRDFGraph(File dataFile) {
//        Model dataModel;
//        try {
//            InputStream inputStream = new FileInputStream(dataFile);
//            dataModel = Rio.parse(inputStream, dataFile.getAbsolutePath(),
//                    Rio.getParserFormatForFileName(dataFile.getAbsolutePath()).orElse(RDFFormat.RDFXML));
//        } catch (Exception e) {
//            logger.error("Error while reading the data file: " + dataFile.getAbsolutePath(), e);
//            return null;
//        }
//        Graph rdfGraph = new RDF4J().asGraph(dataModel);
//        return rdfGraph;
//    }
//
//    private static BaseShapeMap getShapeMap(String shapeMapFileName) {
//
//        BaseShapeMap shapeMap = null;
//        try {
//            InputStream inputStream = new FileInputStream(shapeMapFileName);
//            ShapeMapParsing parser = new ShapeMapParsing();
//            shapeMap = parser.parse(inputStream);
//        } catch (Exception e) {
//            logger.error("Error while reading the shape map file: " + shapeMapFileName, e);
//            return null;
//        }
//        return shapeMap;
//    }
}
