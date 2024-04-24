# API

The OpenAPI specification for Kafkamoon API can be seen on `{host}/swagger-ui/index.html`.

## Problems detail

This API aims to closely follow the [Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807)
specification.

### Topic with conflict

This error can occur when there is a topic with the name provided. To solve this problem you need to:

- Create a new topic with another name

!> We do not have a request to update the topic name.

### Invalid request

This error is a client error with status 400 (Bad Request). Please see the OpenAPI specification to use the API
correctly.

### Topic name exceeded limit

This error occurs because the full topic name, composed by `${messageType}.${dataset}.${dataName}` exceeded the max
length limit.

Explanation to topic name [here](explanation/?id=topic-name).

### Entity not found

This error means that the resource that you are find does not exist.
