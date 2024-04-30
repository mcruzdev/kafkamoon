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

helmUpdate:
	helm dependency update charts/kafkamoon-api 
.PHONY: helmUpdate

helmPkg:
	helm package charts/kafkamoon-api
.PHONY: helmPkg

helmInstall:
	helm install kafkamoon kafkamoon-0.1.0.tgz
.PHONY: helmInstall

helmDestroy:
	helm uninstall kafkamoon
.PHONY: helmDestroy

cleanK8s:
	kubectl delete pvc --all
.PHONY: cleanK8s


helm: helmUpdate helmPkg
	helm install kafkamoon kafkamoon-0.1.0.tgz --set=kafkamoon.mode=default --set=keycloak.enabled=false
.PHONY: helm
