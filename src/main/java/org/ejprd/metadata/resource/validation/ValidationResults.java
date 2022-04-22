package org.ejprd.metadata.resource.validation;

import es.weso.rdf.nodes.IRI;
import es.weso.shapemaps.IRILabel;
import es.weso.shapemaps.Info;
import io.circe.Json;
import scala.jdk.javaapi.OptionConverters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValidationResults {
    private Map conformantShapes = new HashMap<IRI, Map<IRILabel, Info>>();
    private Map nonConformantShapes = new HashMap<IRI, Map<IRILabel, Info>>();
    private String validationResult = "Not validated";
    private String detailedValidationFailureErrors = "";
    private String detailedEvidenceOfConformance = "";
    private Json jsonReasons = Json.Null();

    public Map getConformantShapes() {
        return conformantShapes;
    }

    public Map getNonConformantShapes() {
        return nonConformantShapes;
    }

    public void updateValidationResults() {
        if (nonConformantShapes.isEmpty()) {
            validationResult = "Validation was successful";
        } else {
            validationResult = "Validation failed";
            detailedValidationFailureErrors = getReasonsForNonConformance();
        }
        if (!conformantShapes.isEmpty()) {
            detailedEvidenceOfConformance = getReasonsForConformance();
        }

    }

    public String getShortValidationResult() {
        return (nonConformantShapes.isEmpty()) ? validationResult : validationResult + "\n" + detailedValidationFailureErrors ;
    }

    private String getReasonsForConformance() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Evidence of conformance of the data against the following shapes are:\n");
        conformantShapes.forEach((k, v) -> {
            Map<IRILabel, Info> detail = (Map<IRILabel, Info>) conformantShapes.get(k);
            stringBuilder.append("Shape " + k + " is conformant based on the following evidence:\n");
            detail.forEach((k1, v1) -> {
                Optional<String> reasonOptional = OptionConverters.toJava(v1.reason());
                if (reasonOptional.isPresent())
                    stringBuilder.append("Label has " + k1.iri().str() + " " + reasonOptional.get() + "\n");;
            });
        });

        return stringBuilder.toString();
    }

    private String getReasonsForNonConformance() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Evidence of non-conformance of the data against the following shapes are:\n");
        nonConformantShapes.forEach((k, v) -> {
            Map<IRILabel, Info> detail = (Map<IRILabel, Info>) nonConformantShapes.get(k);
            stringBuilder.append("Shape " + k + " is non-conformant based on the following evidence:\n");
            detail.forEach((k1, v1) -> {
                Optional<String> reasonOptional = OptionConverters.toJava(v1.reason());
                Optional<Json> jsonReasonOptional = OptionConverters.toJava(v1.appInfo());
                if (reasonOptional.isPresent())
                    stringBuilder.append("Label has " + k1.iri().str() + " " + reasonOptional.get() + "\n");
                if (jsonReasonOptional.isPresent())
                    jsonReasons.deepMerge(jsonReasonOptional.get());
            });
        });

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "ValidationResults{" +
                "validationResult='" + validationResult + '\'' +
                ", nonConformantShapes=" + nonConformantShapes +
                ", detailedValidationFailureErrors='" + detailedValidationFailureErrors + '\'' +
                ", conformantShapes=" + conformantShapes +
                ", detailedEvidenceOfConformance='" + detailedEvidenceOfConformance + '\'' +
                '}';
    }
}
