# resource-metadata-validator

As part of the European Joint Programme (EJP) for Rare Disease, we are developing 
standards for rare disease registries to describe their metadata that will improve the 
FAIR-ness of these resources.

This service provides a REST endpoint that enables users to validate a turtle file against a SHeX shape. 
In response this service will return a detailed validation report. 


## Building and running the application
This codebase uses Java 11 and Maven 3.2+ to build and run the application.
1. Clone the codebase: `git clone https://github.com/ejp-rd-vp/resource-metadata-validator.git`
2. Build: `mvn clean package`
3. Run: `java -jar ./target/resource-metadata-validator-0.0.1-SNAPSHOT.jar`

This will run a local instance of the application on port 8080.

## Shex validation
The application accepts HTTP POST requests with the following parameters:
* data: A file containing the data that needs to be validated in turtle syntax.
* shex: A file containing the shex shape against which the data must be validated.
* mapping: A file containing the mapping information to be used for shex validation.
* showDetail: (Optional) Accepts values true/false. False is the default value and returns compact information wrt mapping results. When true detailed information wrt the mapping results will be printed out.

Returns: A detailed or compacted report on the validation requested.

## Example request using cURL
Assuming $PROJECT_ROOT points to the top directory of this codebase, the following cURL command
can be used to validate Organization data:

### On Unix
```
curl -F "data=@$PROJECT_ROOT/src/test/resources/metamodel/organization.ttl" \
-F "shex=@$PROJECT_ROOT/src/test/resources/metamodel/organization.shex" \
-F "mapping=@$PROJECT_ROOT/src/test/resources/metamodel/organization.sm" \
-F "showDetail=true" localhost:8080/api/resource-metadata-validator/validateShex
```

### On Windows
```
curl -F "data=@$PROJECT_ROOT/src/test/resources/metamodel/organization.ttl" ^
-F "shex=@$PROJECT_ROOT/src/test/resources/metamodel/organization.shex" ^
-F "mapping=@$PROJECT_ROOT/src/test/resources/metamodel/organization.sm" ^
localhost:8080/api/resource-metadata-validator/validateShex
```

## Swagger documentation
Documentation can be found at $HOST/api/resource-metadata-validator/docs.html and 
the JSON description of the API can be found at $HOST/api/resource-metadata-validator/docs.
A currently hosted instance can be found at http://45.88.81.202/api/resource-metadata-validator/docs.html.