# Kafka Admin Client - Topic Partition offset playground

Playground to play around with the Kafka client version 3.0.0.

Next to:
* `OffsetSpec.EarliestSpec`
* `OffsetSpec.LatestSpec`
* `OffsetSpec.TimestampSpec`

Since version 3.0.0 you can get 
* `OffsetSpec.MaxTimestampSpec()`: this returns the offset and timestamp for the record with the highest timestamp.

See also
* [Extend ListOffset to fetch offset with max timestamp (KIP-734)](https://issues.apache.org/jira/browse/KAFKA-12541)
* [KIP-734](https://cwiki.apache.org/confluence/display/KAFKA/KIP-734%3A+Improve+AdminClient.listOffsets+to+return+timestamp+and+offset+for+the+record+with+the+largest+timestamp)

## Notes

* This project is using Spring Boot 2.6.0 release candidate 1.
* Spring Boot 2.6.0 RC1 bring the latest version 2.8.0-RC1 of Spring Kafka
* Spring Kafka brings version 3.0.0 of the Kafka (Admin) client

## How to run

Run Kafka, Zookeeper and Kafka manager using Docker:

```
docker-compose up -d
```

Build the project:

```
./mvnw clean package
```

Start the Spring Boot application using Maven. Or import the project in your favorite IDE.

```
./mvnw spring-boot:run
```

Every second a Chuck Norris 'fact' will be produced to topic with name `chuck-norris`.
Every 10 seconds the latest offsets and timestamps for each partition in the topic will be printed.

To change the defaults check the: `application.yml`:

* `print.offsets.rate.ms`: To change the rate in milliseconds to print the partition offsets.
* `chuck.norris.fact.producer.cron`: Change the cron of the producer  

## Errors

In case you are running into this issue your broker don't support MAX_TIMESTAMP offset feature.
Use Kafka version 3.0.0 or Confluent Kafka version 7.0.0 or later!

```
java.util.concurrent.ExecutionException: org.apache.kafka.common.errors.UnsupportedVersionException: Broker 1 does not support MAX_TIMESTAMP offset spec
at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:395) ~[na:na]
at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:1999) ~[na:na]
at org.apache.kafka.common.internals.KafkaFutureImpl.get(KafkaFutureImpl.java:165) ~[kafka-clients-3.0.0.jar:na]
at ScheduledTopicOffsetPrinter.doSomething(AdminService.java:38) ~[classes/:na]
at jdk.internal.reflect.GeneratedMethodAccessor28.invoke(Unknown Source) ~[na:na]
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:84) ~[spring-context-5.3.12.jar:5.3.12]
at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54) ~[spring-context-5.3.12.jar:5.3.12]
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515) ~[na:na]
at java.base/java.util.concurrent.FutureTask.runAndReset$$$capture(FutureTask.java:305) ~[na:na]
at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java) ~[na:na]
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628) ~[na:na]
at java.base/java.lang.Thread.run(Thread.java:834) ~[na:na]
Caused by: org.apache.kafka.common.errors.UnsupportedVersionException: Broker 1 does not support MAX_TIMESTAMP offset spec
```