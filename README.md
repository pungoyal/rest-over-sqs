# rest-over-sqs

rest-over-sqs intends to provide asynchronous integration using sqs for disparate applications 

## Setup

put your production aws keys in AwsCredentials.properties under src/main/resources/ and test aws keys under src/test/resources/

```java
accessKey=access key
secretKey=secret key
```

## Building

### Compiling & running tests

```shell
mvn compile test
```

### Generating the binary

```shell
mvn package
```

## Running the consumer

```shell
java -jar <jar file> <queue name> <polling interval in seconds>
```


## License

rest-over-sqs is Copyright © 2013 Puneet Goyal. It is free software, and may be redistributed under the terms specified in the [LICENSE](https://github.com/pungoyal/rest-over-sqs/blob/master/LICENSE.md) file.