curl -F "data=@./src/test/resources/metamodel/organization.ttl" \
-F "shex=@./src/test/resources/metamodel/organization.shex" \
-F "mapping=@./src/test/resources/metamodel/organization.sm" \
-F "showDetail=true" localhost:8080/validateShexReadableResult
