package org.ejprd.metadata.resource.validation;

import es.weso.rdf.nodes.IRI;
import es.weso.rdf.nodes.RDFNode;
import es.weso.shaclex.ShExWrapper;
import es.weso.shaclex.ShExsOptions;
import es.weso.shapemaps.Info;
import es.weso.shapemaps.ResultShapeMap;

import es.weso.shapemaps.ShapeMapLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.jdk.javaapi.CollectionConverters;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShaclexValidator {
    private static Logger logger = LoggerFactory.getLogger(ShaclexValidator.class);


    public static ResultShapeMap validateUsingMappingFiles(String rdfFileName, String shexFileName, String shapeMapFileName)
            throws IOException {

        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        ResultShapeMap resultShapeMap = validateUsingMappingFiles(rdfFileName, shexFileName, shapeMapFileName, shExsOptions);
        return resultShapeMap;
    }

    public static ResultShapeMap validateUsingMappingFiles(String rdfFileName, String shexFileName, String shapeMapFileName,
                                                           ShExsOptions shExsOptions)
            throws IOException {

        URL rdfURL = new URL(rdfFileName);
        URL shexURL = new URL(shexFileName);
        URL shapeMapURL = new URL(shapeMapFileName);
        InputStream rdfInputStream = new BufferedInputStream(rdfURL.openStream());
        InputStream shexInputStream = new BufferedInputStream(shexURL.openStream());
        InputStream shapeMapInputStream = new BufferedInputStream(shapeMapURL.openStream());
        ResultShapeMap resultShapeMap = ShExWrapper.validate(rdfInputStream, shexInputStream, shapeMapInputStream,
                shExsOptions);
        return resultShapeMap;
    }

    public static ValidationResults validateUsingMappingFilesAndReadableResult(String rdfFileName, String shexFileName, String shapeMapFileName)
            throws IOException {

        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        ResultShapeMap resultShapeMap = validateUsingMappingFiles(rdfFileName, shexFileName, shapeMapFileName, shExsOptions);

        return parseResult(resultShapeMap);
    }


    public static ResultShapeMap validate(String data, String shex, String shapeMap) {
        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        return validate(data, shex, shapeMap, shExsOptions);
    }

    public static ValidationResults validateAndReturnReadableResult(String data, String shex, String shapeMap) {
        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        ResultShapeMap resultShapeMap = validate(data, shex, shapeMap, shExsOptions);
        return parseResult(resultShapeMap);
    }

    public static ResultShapeMap validate(String data, String shex, String shapeMap, ShExsOptions shExsOptions) {
        return ShExWrapper.validate(data, shex, shapeMap, shExsOptions);
    }


    public static ResultShapeMap validateUsingFocusNodeAndLabel(String rdfData, String shexSchema, String node, String label){
        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        ResultShapeMap resultShapeMap = validateUsingFocusNodeAndLabel(rdfData, shexSchema, node, label, shExsOptions);
        return resultShapeMap;
    }

    public static ResultShapeMap validateUsingFocusNodeAndLabel(String rdfData, String shexSchema, String node, String label,
                                               ShExsOptions shExsOptions){
        ResultShapeMap resultShapeMap = ShExWrapper.validateNodeShape(rdfData, shexSchema, node, label, shExsOptions);
        return resultShapeMap;
    }

    private static ValidationResults parseResult(ResultShapeMap resultShapeMap) {
        ValidationResults validationResults = new ValidationResults();

        Map<RDFNode, scala.collection.immutable.Map<ShapeMapLabel, Info>> resultMap =
                CollectionConverters.asJava(resultShapeMap.resultMap());
        resultMap.forEach((k, v) -> {
            CollectionConverters.asJava(resultShapeMap.getConformantShapes(k))
                    .forEach(e -> validationResults.getConformantShapes().putIfAbsent(k, new HashMap<>()));
            CollectionConverters.asJava(resultShapeMap.getNonConformantShapes(k))
                    .forEach(e -> validationResults.getNonConformantShapes().putIfAbsent(k, new HashMap<>()));
            Map detail = CollectionConverters.asJava(v);
            if (validationResults.getConformantShapes().containsKey(k)) {
                validationResults.getConformantShapes().put(k, detail);
            }
            if (validationResults.getNonConformantShapes().containsKey(k)) {
                validationResults.getNonConformantShapes().put(k, detail);
            }
        });
        validationResults.updateValidationResults();
        logger.trace(validationResults.toString());
        return validationResults;
    }
}
