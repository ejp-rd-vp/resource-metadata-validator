package org.ejprd.metadata.resource.validation;

import es.weso.rdf.PrefixMap;
import es.weso.rdf.nodes.IRI;
import es.weso.rdf.nodes.RDFNode;
import es.weso.shapemaps.Association;
import es.weso.shapemaps.ResultShapeMap;
import es.weso.shapemaps.ShapeMap;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Iterator;
import scala.collection.immutable.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ShaclexValidatorTest {
    private static Logger logger = LoggerFactory.getLogger(ShaclexValidatorTest.class);

    @ParameterizedTest
    @CsvFileSource(resources = "/shexprimer/testDataFocusNodeAndLabel.csv")
    void testShexPrimerExamplesUsingFocusNodeAndLabel(String fileToValidateName, String shexFileName, String strFocusNode,
                                                      String strShapeLabel, boolean expectedResult) {

        Optional<ResultShapeMap> resultShapeMap = Optional.empty();

        String userDirectory = System.getProperty("user.dir");
        String relativeDirectory = "/src/test/resources/";

        String strRDFData = null;
        String strShex = null;
        try {
            strRDFData = getFileDataAsString(userDirectory, relativeDirectory, fileToValidateName);
            strShex = getFileDataAsString(userDirectory, relativeDirectory, shexFileName);
            resultShapeMap = Optional.of(ShaclexValidator.validateUsingFocusNodeAndLabel(
                    strRDFData, strShex, strFocusNode, strShapeLabel));
            logger.trace("=== ResultShapeMap Begin ===");
            logger.trace(resultShapeMap.get().showShapeMap(false));
            logger.trace("=== ResultShapeMap END ===");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }

        logger.trace("strRDFData = " + strRDFData);
        logger.trace("strShex = " + strShex);
        logger.trace("strFocusNode = " + strFocusNode);
        logger.trace("strShapeLabel = " + strShapeLabel);
        if (resultShapeMap.isPresent()) {
            logger.trace("Conformant shapes = " + resultShapeMap.get().getConformantShapes(
                    RDFNode.fromString(strFocusNode).right().get()));
        } else {
            logger.error("No result was returned!");
        }
    }

    private static String getFileDataAsString(String strUserDirectory, String strRelativeDirectory, String strFileName)
            throws IOException {
        String strAbsoluteFileName = (new StringBuffer(strUserDirectory))
                .append(strRelativeDirectory)
                .append(strFileName)
                .toString();
        File file = new File(strAbsoluteFileName);
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    private static String getFileDataAsString(String strAbsoluteFileName)
            throws IOException {
        File file = new File(strAbsoluteFileName);
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/shexprimer/testDataMappingFile.csv")
    void testShexPrimerExamplesUsingMappingFile(String fileToValidateName, String shexFileName, String mappingFileName,
                                                String expectedResultShapeMapFileName) {
        String userDirectory = System.getProperty("user.dir");
        String relativeDirectory = "/src/test/resources/";
        String absoluteFileToValidateName = (new StringBuffer(userDirectory))
                .append(relativeDirectory).append(fileToValidateName).toString();
        String absoluteShexFileName = (new StringBuffer(userDirectory))
                .append(relativeDirectory).append(shexFileName).toString();
        String absoluteMappingFileName = (new StringBuffer(userDirectory))
                .append(relativeDirectory).append(mappingFileName).toString();


        Optional<ResultShapeMap> resultShapeMapOptional = Optional.empty();
        try {
            resultShapeMapOptional = Optional.of(ShaclexValidator.validateUsingFiles(absoluteFileToValidateName,
                    absoluteShexFileName, absoluteMappingFileName));

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        ResultShapeMap resultShapeMap = resultShapeMapOptional.get();

        if (expectedResultShapeMapFileName == null || expectedResultShapeMapFileName.isEmpty()) {
            logger.trace("Compact result: " + resultShapeMap.showShapeMap(false));
            return;
        }

        String absoluteExpectedResultShapeMapFileName = (new StringBuffer(userDirectory))
                .append(relativeDirectory).append(expectedResultShapeMapFileName).toString();

        ShapeMap expectedShapeMap = null;
        try {
            expectedShapeMap = readCompactResultShapeMap(
                    absoluteExpectedResultShapeMapFileName,
                    resultShapeMap.nodesPrefixMap(),
                    resultShapeMap.shapesPrefixMap()
                    );
        } catch (IOException e) {
            fail("Could not read expected result shape map: " + absoluteExpectedResultShapeMapFileName);
            logger.error(e.getMessage(), e);
        }

        assertTrue(compareResultMaps(resultShapeMap, expectedShapeMap));
    }


    private static ShapeMap readCompactResultShapeMap(String strFileName, PrefixMap nodesPrefixMap, PrefixMap shapesPrefixMap)
            throws IOException {
        String strCompactResultShapeMap = getFileDataAsString(strFileName);
        scala.Option<IRI> none = scala.Option.apply(null);

        ShapeMap shapeMap = ResultShapeMap
                .fromString(strCompactResultShapeMap, "COMPACT", none, nodesPrefixMap, shapesPrefixMap)
                .right()
                .get();
        return shapeMap;
    }

    private static boolean compareResultMaps(ShapeMap actualResultShapeMap, ShapeMap expectedResultShapeMap) {
        if (expectedResultShapeMap == null || actualResultShapeMap == null) {
            logger.debug("expectedResultShapeMap == null || actualResultShapeMap == null");
            return false;
        }

        if (expectedResultShapeMap.associations() == null || actualResultShapeMap.associations() == null) {
            logger.debug("expectedResultShapeMap.associations() == null || actualResultShapeMap.associations() == null");
            return false;
        }

        if (expectedResultShapeMap.associations().size() != actualResultShapeMap.associations().size()) {
            logger.debug("expectedResultShapeMap.associations().size() != actualResultShapeMap.associations().size()");
            return false;
        }

        Iterator<Association> expectedResultShapeMapIterator = expectedResultShapeMap.associations().iterator();
        while (expectedResultShapeMapIterator.hasNext()) {
            Association expectedAssociation = expectedResultShapeMapIterator.next();
            if (!associationsContainAsssociation(actualResultShapeMap.associations(), expectedAssociation)) {
                logger.debug(actualResultShapeMap.associations() + " does not contain " + expectedAssociation);
                return false;
            }
        }

        return true;
    }

    private static boolean associationsContainAsssociation(List<Association> associationList, Association association) {
        if (association == null)
            return false;

        if (associationList == null || associationList.isEmpty())
            return false;

        AtomicBoolean found = new AtomicBoolean(false);
        Iterator<Association> associationIterator = associationList.iterator();
        while (associationIterator.hasNext() && !found.get()) {
            Association associationToCompare = associationIterator.next();
            if (association.node().equals(associationToCompare.node())
                && association.shape().equals(associationToCompare.shape())
                && association.info().status().equals(associationToCompare.info().status()))

                found.set(true);
        }

        return found.get();
    }

}