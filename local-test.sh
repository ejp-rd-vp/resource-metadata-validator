curl -F "data=@./src/test/resources/errorhandling/organization_broken.ttl" \
-F "shex=@./src/test/resources/errorhandling/organization.shex" \
-F "mapping=@./src/test/resources/errorhandling/organization.sm" \
localhost:8080/validateShexJSONResult
