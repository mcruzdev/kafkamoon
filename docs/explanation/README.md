# Explanation

## Topic name

To improve the communication and to have a standardization when creating a topic, I decided to define a convention for topic name, based on
this [blogpost](https://cnr.sh/essays/how-paint-bike-shed-kafka-topic-naming-conventions).

### Topic name convention

The topic name is divided into three parts: **message type**, **dataset** name and **data name**.

* **Message type** - Indicates the type of the message, that can have the following values:
    * logging
    * queuing
    * tracking
    * etl/db
    * streaming
    * push
    * user
* **Dataset name** - Is analogous to a database name in traditional RDBMS systems. It's used as a category to group topics
  together.
* **Data name** - Is analogous to a table name in traditional RDBMS systems though it’s fine to include further dotted
  notation if developers wish to impose their own hierarchy within the dataset namespace.

An example of topic name where we can log an event that occurred on **store** where the **package** was **received**.

> message type: `queuing`
>
> dataset name: `store`
>
> data name: `package-received`


By default, the Kafkamoon API concatenates these fields to create the final topic name, resulting in `queuing.store.package-receive`.

!> **IMPORTANT**: The maximum topic name length allowed is 255 characters. This means that the combined length of the message type, dataset name, and data name must not exceed 252 characters, leaving room for additional separators (`.`).

## Criticality

If you have created a topic using the Kafkamoon API, you may have noticed a field called `criticality`. Currently, this field only supports the values `TEST` or `test`. The concept behind this restriction is to minimize unintended **production-like behavior** and **costs** in **test environments**.


