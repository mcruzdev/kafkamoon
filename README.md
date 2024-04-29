# Kafkamoon API

Welcome to Kafkamoon, a Kafka management application. This project demonstrates integration
with [Kafka APIs](https://docs.confluent.io/kafka/kafka-apis.html) as part of a hiring test.

> [!NOTE] This README documentation contains information on how to run the Kafkamoon application locally using Docker or Kubernetes.
> If you want to see all the information about the decisions made in this project, see the official documentation.

## Table of Contents

- [Kafkamoon API](#kafkamoon-api)
  - [Table of Contents](#table-of-contents)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Running the project locally with Docker Compose](#running-the-project-locally-with-docker-compose)
    - [Running the project locally on Kubernetes](#running-the-project-locally-on-kubernetes)
    - [Running the project on EKS](#running-the-project-on-eks)
- [Running on EKS](#running-on-eks)
## Getting Started

### Prerequisites

Before you begin, make sure you have the following tools installed:

- **Java:** Version 17
- **Maven:** Version 3.9.6
- **Docker**
- **Docker Compose**
- **Helm**
- **KinD**
- **eksctl**

### Running the project locally with Docker Compose

To run the project locally, follow these steps:

1. Clone the repository:

```shell
git clone git@github.com:mcruzdev/kafkamoon.git
```

2. Navigate to the project directory:

```shell
cd kafkamoon-api
```

3. Build the application:

```shell
make buildAll
```

4. Start the application using Docker Compose:

```shell
docker-compose --profile local up -d
```

Running with docker-compose you will have:

- Kafka cluster with 3 replicas
- [Kafkamoon documentation](http://localhost:3000)
- [Kafkamoon API](http://localhost:8080/swagger-ui/index.html)

> [!NOTE] If you want to run only the infrastructure (Kafka) and Documentation run the following command:
> ```shell
> docker-compose --profile dev up -d
> ```

After the application is running, you can interact with the following resources:

* [OpenAPI specification](http://localhost:8080/swagger-ui.html) for exploring and interacting with the API.
* [Kafkamoon Documentation](http://localhost:3000) for additional information and guidance.


### Running the project locally on Kubernetes

To run the project locally on Kubernetes, follow these steps:

1. Create the Kubernetes cluster:

```shell
kind create cluster --name local --config=kind/kind-cluster.yaml
```

2. Configure the context:

```shell
kubectl cluster-info --context kind-local
```

3. Update Helm dependencies

```shell
make helmUpdate
```

4. Package Helm

```shell
make helmPkg
```

5. Install the **Kafkamoon** application with Helm:

```shell
make helmInstall
```

This installation contains:

- Grafana
- Prometheus
- Kafka cluster with 2 replicas
- Kafkamoon API
- Kafkamoon Documentation

**Accessing Grafana:**

1. Get Grafana admin to access Grafana Dashboards

```shell
kubectl get secret kafkamoon-grafana-operator-grafana-admin-credentials -o jsonpath="{.data['GF_SECURITY_ADMIN_PASSWORD']}" | base64 --decode
```

The ouput should like something like this:

```shell
abc123_d==%
```

> [!IMPORTANT] Remove the last character (`%`) from the password.

2. Do a `port-forward` command:

```shell
kubectl port-forward svc/kafkamoon-grafana-operator-grafana-service 8888:3000
```

Access the Grafana through this [url](http://localhost:8888).

> [!IMPORTANT] The username is **admin**.

### Running the project on EKS

First of all, you need:

1. To have your AWS credentials (`~/.aws/credentials`) configured locally. 

> Run the following command: `aws configure`


2. To have a bucket created to store the `terraform.tfstate` file.

> Run the following command: `export TF_VAR_bucket=<add_your_bucket_name>`

# Running on EKS

1. Go to `terraform-gitops` directory

```shell
cd terraform-gitops
```

2. Execute `terraform init`

```shell
terraform init
```

3. Execute `terraform plan`

```shell
terraform plan
```

Check the `terraform plan` output.

4. Apply the terraform manifests

```shell
terraform apply --auto-approve
```

5. Update current kubeconfig

```shell
aws eks update-kubeconfig --name platformoon-kafkamoon --region us-east-1
```

6. Configure Amazon EBS CSI for EKS

The Kafka application creates a PVC and the following configuration is necessary to give all necessary rights.

[See here how to configure Amazon EBS CSI for EKS](EKS_PVC_CONFIGURATION.md)

7. Install helm charts

```shell
make helmUpdate && make helmPkg && make helmInstall
```
