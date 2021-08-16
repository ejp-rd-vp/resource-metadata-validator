package org.ejprd.metadata.resource.validation;

import es.weso.rdf.PrefixMap;
import es.weso.rdf.nodes.IRI;
import es.weso.rdf.nodes.RDFNode;
import es.weso.shapemaps.Association;
import es.weso.shapemaps.ResultShapeMap;
import es.weso.shapemaps.ShapeMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.opentest4j.AssertionFailedError;
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
    private static final String RELATIVE_PATH = "${RelativePath}";

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
            resultShapeMapOptional = Optional.of(ShaclexValidator.validateUsingMappingFiles(absoluteFileToValidateName,
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

        compareActualShapeMapWithExpected(resultShapeMap, absoluteExpectedResultShapeMapFileName);
    }

    private void compareActualShapeMapWithExpected(ResultShapeMap resultShapeMap, String absoluteExpectedResultShapeMapFileName) {
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

        try {
            assertTrue(compareResultMaps(resultShapeMap, expectedShapeMap));
        } catch (AssertionFailedError e) {
            logger.trace("Actual result different from expected: " + resultShapeMap.showShapeMap(false));
            throw e;
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/shexprimer/testDataSchemasWithImports.csv")
    void testShexPrimerImportExamples(String fileToValidateName, String shexFileName, String mappingFileName,
                                                String expectedResultShapeMapFileName) {
        String userDirectory = System.getProperty("user.dir");
        String relativeDirectory = "/src/test/resources/";
        String absoluteRelativePath = (new StringBuffer(userDirectory))
                .append(relativeDirectory).toString();
        String absoluteFileToValidateName = (new StringBuffer(absoluteRelativePath))
                .append(fileToValidateName).toString();
        String absoluteShexFileName = (new StringBuffer(absoluteRelativePath))
                .append(shexFileName).toString();
        String absoluteMappingFileName = (new StringBuffer(absoluteRelativePath))
                .append(mappingFileName).toString();
        String absoluteExpectedResultShapeMapFileName = (new StringBuffer(absoluteRelativePath))
                .append(expectedResultShapeMapFileName).toString();
        String data = null;
        String shexWithTokens = null;
        String mapping = null;
        String expected = null;

        try {
            data = getFileDataAsString(absoluteFileToValidateName);
            shexWithTokens = getFileDataAsString(absoluteShexFileName);
            mapping = getFileDataAsString(absoluteMappingFileName);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            fail();
        }

        String absoluteRelativePathWithProtocol = (new StringBuffer("file://")).append(absoluteRelativePath).toString();
        String shex = StringUtils.replace(shexWithTokens, RELATIVE_PATH, absoluteRelativePathWithProtocol);
        ResultShapeMap resultShapeMap = ShaclexValidator.validate(data, shex, mapping);
        compareActualShapeMapWithExpected(resultShapeMap, absoluteExpectedResultShapeMapFileName);
    }

    @Test
    void testShexValidationUsingStrings() {
        String data = new StringBuilder("PREFIX inst: <http://example.com/users/> \n")
                .append("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n")
                .append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n")
                .append("inst:User1 \n")
                .append("foaf:name  'Bob Smith'^^xsd:string .").toString();

        String schema = new StringBuilder("PREFIX my: <http://my.example/#> \n")
                .append("PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n")
                .append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n")
                .append("my:UserShape { \n")
                .append("foaf:name xsd:string \n")
                .append("}").toString();

        String mapping = new String("inst:User1@my:UserShape");

        ResultShapeMap resultShapeMap = ShaclexValidator.validate(data, schema, mapping);

        int i = 0;
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