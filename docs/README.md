# Kafkamoon API

Welcome to Kafkamoon, a Kafka management application. This This project demonstrates integration
with [Kafka APIs](https://docs.confluent.io/kafka/kafka-apis.html) as part of a hiring test.

## Table of Contents

1. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Running the Project Locally](#running-the-project-locally)
    - [Running Only the Documentation](#running-only-the-documentation)
2. [How-to Guides](#how-to-guides)
3. [References](#references)
4. [Explanation](#explanation)

## Getting Started

### Prerequisites

Before you begin, make sure you have the following tools installed:

- **Java:** Version 17
- **Maven:** Version 3.9.6
- **Docker**
- **Docker Compose**

### Running the project locally

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
docker-compose up -d
```

After the application is running, you can interact with the following resources:

* [OpenAPI specification](http://localhost:8080/swagger-ui.html) for exploring and interacting with the API.
* [Kafkamoon Documentation](http://localhost:3000) for additional information and guidance.

### Running Only the Documentation

To build and run only the documentation, do the following:

1. Build the documentation container:

```shell
make buildDocs
```

2. Run the container:

```shell
docker run --rm -it -p 3000:3000 kafkamoon-docs:latest
```

This starts the documentation server on port 3000.

## How-to guides

For additional guidance on using Kafkamoon, check out our [how-to guides](how-to/).

## Reference

For more detailed information on the Kafkamoon API, refer to our [API references](reference/api.md)
and [configuration references](reference/configurations.md).

## Explanation

If you're interested in learning about the design decisions and other details of the project, visit the [explanation section](explanation/?id=explanation).
