# Configuration

Here we will see all necessary information to configure the **Kafkaman API**.

## Kafka configuration

The default setting for `KAFKA_AUTO_CREATE_TOPICS_ENABLE` in the docker-compose.yaml file is `false`, ensuring that topics are only created intentionally. 

This setting gives us greater control over topic management, reducing the risk of accidental or unnecessary topic creation. It is generally recommended for production environments to maintain a more stable Kafka infrastructure.


