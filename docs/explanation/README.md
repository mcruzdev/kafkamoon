# Explanation

## Topic name

To have a standardization when creating a topic, we decided to define a convention for topic name, based on
this [blogpost](https://cnr.sh/essays/how-paint-bike-shed-kafka-topic-naming-conventions).

### Standard

The topic name is divided into three parts:

`message type`.`dataset name`.`data name`

* Message type - Indicates the type of the message, that can have the following values:
    * logging
    * queuing
    * tracking
    * etl/db
    * streaming
    * push
    * user
* Dataset name - Is analogous to a database name in traditional RDBMS systems. It's used as a category to group topics
  together.
* Data name - Is analogous to a table name in traditional RDBMS systems though it’s fine to include further dotted
  notation if developers wish to impose their own hierarchy within the dataset namespace.

An example of topic name where we can log an event that occurred on **store** where the **package** was **received**.

> message type: `queuing`
>
> dataset name: `store`
>
> data name: `package-received`


!> **IMPORTANT**: The maximum topic name length allowed is 255 characters. This means that the combined length of the message type, dataset name, and data name must not exceed 252 characters, leaving room for additional separators or prefixes.

## Criticality

If you already created a topic using the Kafkamoon API, you saw that you have a field called `criticality`. Actually, we
are
supporting only `TEST` and `test` values. We have this concept to avoid unnecessary **production** **behaviors** and **cost** for
**test** environments.


