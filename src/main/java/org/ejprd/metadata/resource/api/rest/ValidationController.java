package org.ejprd.metadata.resource.api.rest;

import es.weso.shapemaps.ResultShapeMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.io.IOUtils;
import org.ejprd.metadata.resource.validation.ShaclexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

@RestController
@RequestMapping("/api/resource-metadata-validator")
public class ValidationController {

    private static Logger logger = LoggerFactory.getLogger(ValidationController.class);

    @PostMapping(value = "/validateShex",
            consumes = { "multipart/form-data" },
            produces = { "application/json","text/plain" })
    @Operation(description = "Validates data against the given shex schema using the given mapping.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            description = "When 'resultType=json', a json response will be given based on the " ,
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ResultShapeMap.class),
                                            examples = {@ExampleObject(
                                                    description = "Result for resultType='json'",
                                                    value = "[\n" +
                                                    "  {\n" +
                                                    "    \"node\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/biobank>\",\n" +
                                                    "    \"shape\": \"<http://purl.org/ejp-rd/metadata-model/v1/shex/biobankShape>\",\n" +
                                                    "    \"status\": \"conformant\",\n" +
                                                    "    \"appInfo\": {\n" +
                                                    "      \"evidences\": [\n" +
                                                    "        \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/biobank> is an IRI\",\n" +
                                                    "        \"<http://www.orpha.net/ORDO/Orphanet_171895> is an IRI\",\n" +
                                                    "        \"Austrian Bone Marrow Donor Registry has datatype xsd:string\",\n" +
                                                    "        \"ejp:Biobank is equal to ejp:Biobank\",\n" +
                                                    "        \"<http://www.stammzellspende.at> is an IRI\"\n" +
                                                    "      ]\n" +
                                                    "    },\n" +
                                                    "    \"reason\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/biobank> is an IRI\\n<http://www.orpha.net/ORDO/Orphanet_171895> is an IRI\\nAustrian Bone Marrow Donor Registry has datatype xsd:string\\nejp:Biobank is equal to ejp:Biobank\\n<http://www.stammzellspende.at> is an IRI\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"node\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/organisation>\",\n" +
                                                    "    \"shape\": \"<http://purl.org/ejp-rd/metadata-model/v1/shex/organisationShape>\",\n" +
                                                    "    \"status\": \"conformant\",\n" +
                                                    "    \"appInfo\": {\n" +
                                                    "      \"evidences\": [\n" +
                                                    "        \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/organisation> is an IRI\",\n" +
                                                    "        \"Fonds Österreichisches Stammzellregister has datatype xsd:string\",\n" +
                                                    "        \"foaf:Organisation is equal to foaf:Organisation\"\n" +
                                                    "      ]\n" +
                                                    "    },\n" +
                                                    "    \"reason\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/organisation> is an IRI\\nFonds Österreichisches Stammzellregister has datatype xsd:string\\nfoaf:Organisation is equal to foaf:Organisation\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"node\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/location>\",\n" +
                                                    "    \"shape\": \"<http://purl.org/ejp-rd/metadata-model/v1/shex/locationShape>\",\n" +
                                                    "    \"status\": \"conformant\",\n" +
                                                    "    \"appInfo\": {\n" +
                                                    "      \"evidences\": [\n" +
                                                    "        \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/location> is an IRI\",\n" +
                                                    "        \":location passed  [ dct:Location] {1,1} for path <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\",\n" +
                                                    "        \":location passed  xsd:string{1,1} for path dct:title\",\n" +
                                                    "        \":location passed  xsd:string* for path dct:description\"\n" +
                                                    "      ]\n" +
                                                    "    },\n" +
                                                    "    \"reason\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/location> is an IRI\\n:location passed  [ dct:Location] {1,1} for path <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\\n:location passed  xsd:string{1,1} for path dct:title\\n:location passed  xsd:string* for path dct:description\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"node\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/population_coverage>\",\n" +
                                                    "    \"shape\": \"<http://purl.org/ejp-rd/metadata-model/v1/shex/populationCoverageShape>\",\n" +
                                                    "    \"status\": \"conformant\",\n" +
                                                    "    \"appInfo\": {\n" +
                                                    "      \"evidences\": [\n" +
                                                    "        \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/population_coverage> is an IRI\",\n" +
                                                    "        \":population_coverage passed  [ sio:SIO_001166] {1,1} for path <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\",\n" +
                                                    "        \":population_coverage passed  [  \\\"National\\\" \\\"International\\\" \\\"Regional\\\"] {1,1} for path rdfs:label\"\n" +
                                                    "      ]\n" +
                                                    "    },\n" +
                                                    "    \"reason\": \"<http://purl.org/ejp-rd/metadata-model/v1/example-rdf/population_coverage> is an IRI\\n:population_coverage passed  [ sio:SIO_001166] {1,1} for path <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\\n:population_coverage passed  [  \\\"National\\\" \\\"International\\\" \\\"Regional\\\"] {1,1} for path rdfs:label\"\n" +
                                                    "  }\n" +
                                                    "]")}
                                    ),
                                    @Content(
                                            mediaType = "text/plain",
                                            examples = {
                                                    @ExampleObject(description = "Result for resultType='compact'",
                                                    value = ":biobank@:biobankShape, :organisation@:organisationShape, " +
                                                            ":location@:locationShape, :population_coverage@:populationCoverageShape")
                                            }
                                     )
                            })
            })
    String validateShex(
            @Parameter(description = "A file containing the data that needs to be validated in turtle syntax")
                @RequestParam MultipartFile data,
            @Parameter(description = "A file containing the shex shape against which the data must be validated.")
                @RequestParam MultipartFile shex,
            @Parameter(description = "A file containing the mapping information to be used for shex validation.")
                @RequestParam MultipartFile mapping,
            @Parameter(description = "The type of the result to return. Possible values are 'json','compact' and 'compact-detail'. The default is 'json'.")
                @RequestParam(value = "resultType", defaultValue = "json", required = false) String resultType) {
        logger.info("### validateShex called");
        try {
            logger.trace("data = " + data.getOriginalFilename());
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
        logger.trace("shex = " + shex.getOriginalFilename());
        logger.trace("mapping = " + mapping.getOriginalFilename());

        Optional<String>  optionalData = Optional.empty();
        Optional<String>  optionalShex = Optional.empty();
        Optional<String>  optionalMapping = Optional.empty();

        try {
            optionalData = Optional.of(IOUtils.toString(data.getInputStream(), Charset.defaultCharset()));
            optionalShex = Optional.of(IOUtils.toString(shex.getInputStream(), Charset.defaultCharset()));
            optionalMapping = Optional.of(IOUtils.toString(mapping.getInputStream(), Charset.defaultCharset()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.trace("optionalData = " + optionalData.get());
        logger.trace("optionalShex = " + optionalShex.get());
        logger.trace("optionalMapping = " + optionalMapping.get());

        ResultShapeMap resultShapeMap = ShaclexValidator.validate(optionalData.get(), optionalShex.get(),
                optionalMapping.get());

        logger.trace("resultShapeMap as JSON = " + resultShapeMap.toJson());
        ResultType resultTypeEnum = ResultType.fromString(resultType);
        switch (resultTypeEnum) {
            case JSON: return resultShapeMap.toJson().toString();
            case COMPACT: return resultShapeMap.showShapeMap(false);
            case COMPACT_DETAIL: return resultShapeMap.showShapeMap(true);
        }

        return resultShapeMap.toJson().toString();
    }

}
