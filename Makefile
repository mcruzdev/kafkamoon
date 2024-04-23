buildDocs:
	echo "Building kafkamoon-docs docker image"
	docker build -f Dockerfile.docs -t kafkamoon-api-docs:latest .
.PHONY: buildDocs

buildApp:
	echo "Building kafkamoon-api with Maven"
	mvn clean package
	echo "Generating docker image from Dockerfile"
	docker build -f Dockerfile -t kafkamoon-api:latest .
.PHONY: buildApp

buildAll: buildApp buildDocs
.PHONY: buildAll

