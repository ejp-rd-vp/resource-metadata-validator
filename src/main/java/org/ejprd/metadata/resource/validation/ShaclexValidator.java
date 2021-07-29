package org.ejprd.metadata.resource.validation;

import es.weso.shaclex.ShExWrapper;
import es.weso.shaclex.ShExsOptions;
import es.weso.shapemaps.ResultShapeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class ShaclexValidator {
    private static Logger logger = LoggerFactory.getLogger(ShaclexValidator.class);

    public static ResultShapeMap validateUsingMappingFiles(String rdfFileName, String shexFileName, String shapeMapFileName)
                                                    throws FileNotFoundException{

        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        ResultShapeMap resultShapeMap = validateUsingMappingFiles(rdfFileName, shexFileName, shapeMapFileName, shExsOptions);
        return resultShapeMap;
    }

    public static ResultShapeMap validateUsingMappingFiles(String rdfFileName, String shexFileName, String shapeMapFileName,
                                                           ShExsOptions shExsOptions) throws FileNotFoundException {

        InputStream rdfInputStream = new FileInputStream(rdfFileName);
        InputStream shexInputStream = new FileInputStream(shexFileName);
        InputStream shapeMapInputStream = new FileInputStream(shapeMapFileName);
        ResultShapeMap resultShapeMap = ShExWrapper.validate(rdfInputStream, shexInputStream, shapeMapInputStream,
                shExsOptions);
        return resultShapeMap;
    }

    public static ResultShapeMap validate(String data, String shex, String shapeMap) {
        ShExsOptions shExsOptions = ShExsOptions.defaultOptions();
        return validate(data, shex, shapeMap, shExsOptions);
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
}
