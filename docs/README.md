# Kafkamoon API

Kafkamoon is a Kafka management application, this project aims to demonstrate the integration
with [Kafka APIs](https://docs.confluent.io/kafka/kafka-apis.html) for a hiring test.

## Getting Started

### Prerequisites

Before all you need to have the following tools on your machine.

* **Java:** 17
* **Maven:** 3.9.6
* **docker**
* **docker-compose**

### Running the project locally

Clone the project.

```shell
git clone git@github.com:mcruzdev/kafkamoon.git
```

Go to the API project directory:

```shell
cd kafkamoon-api
```

Build application with:

```shell
make buildAll
```

Running the application:

```shell
docker-compose up -d
```

* Accessing the [OpenAPI specification](http://localhost:8080/swagger-ui.html) to interact with the API resources.
* Accessing the [Kafkamoon Documentation](http://localhost:3000)

### Running only the documentation

Build the documentation container:

```shell
make buildDocs
```

Execute the container:

```shell
docker run --rm -it -p 3000:3000 kafkamoon-docs:latest
```

## How-to guides

See our [how-to guides](how-to/README.md) for getting more contact with Kafkamoon.

## Reference

See our [references](reference/api.md) and [configurations](reference/configurations.md) about the Kafkamoon API.

## Explanation

If you want to see more details about decision and details, [see the explanation](explanation/README.md) section.