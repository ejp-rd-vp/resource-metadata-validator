package org.ejprd.metadata.resource.api.rest;

import es.weso.shapemaps.ResultShapeMap;
import org.apache.commons.io.IOUtils;
import org.ejprd.metadata.resource.validation.ShaclexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

@RestController
public class ValidationController {

    private static Logger logger = LoggerFactory.getLogger(ValidationController.class);

    @PostMapping(value = "/validateShex", consumes = { "multipart/form-data" })
    ResultShapeMap validateShex(@RequestParam MultipartFile data,
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

        ResultShapeMap resultShapeMap = ShaclexValidator.validate(optionalData.get(), optionalShex.get(),
                optionalMapping.get());

        logger.trace("resultShapeMap = " + resultShapeMap.showShapeMap(true));
        return resultShapeMap;
    }


}
