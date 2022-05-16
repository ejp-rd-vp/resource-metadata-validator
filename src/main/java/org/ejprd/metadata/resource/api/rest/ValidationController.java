package org.ejprd.metadata.resource.api.rest;

import es.weso.rdf.nodes.RDFNode;
import es.weso.shapemaps.Info;
import es.weso.shapemaps.ResultShapeMap;
import es.weso.shapemaps.ShapeMapLabel;
import io.circe.Json;
import org.apache.commons.io.IOUtils;
import org.ejprd.metadata.resource.validation.ShaclexValidator;
import org.ejprd.metadata.resource.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import scala.jdk.CollectionConverters.*;
import scala.jdk.javaapi.CollectionConverters;

@RestController
public class ValidationController {

    private static Logger logger = LoggerFactory.getLogger(ValidationController.class);

    @PostMapping(value = "/validateShexReadableResult", consumes = { "multipart/form-data" })
    String validateShexReadableResult(@RequestParam MultipartFile data,
                        @RequestParam MultipartFile shex,
                        @RequestParam MultipartFile mapping) {
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

        ValidationResults validationResults = ShaclexValidator.validateAndReturnReadableResult(optionalData.get(), optionalShex.get(),
                optionalMapping.get());

        logger.trace("resultShapeMap = " + validationResults.getShortValidationResult());


        return validationResults.getShortValidationResult();
    }

    @PostMapping(value = "/validateShex", consumes = { "multipart/form-data" }, produces = { "application/json" })
    String validateShex(@RequestParam MultipartFile data,
                        @RequestParam MultipartFile shex,
                        @RequestParam MultipartFile mapping,
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
