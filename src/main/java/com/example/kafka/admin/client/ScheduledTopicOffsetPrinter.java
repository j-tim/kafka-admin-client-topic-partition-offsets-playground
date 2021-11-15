package com.example.kafka.admin.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.kafka.admin.client.KafkaTopicConfiguration.TOPIC_NAME;

@Component
@Slf4j
public class ScheduledTopicOffsetPrinter {

  private final KafkaAdmin kafkaAdmin;
  private final AdminClient adminClient;

  public ScheduledTopicOffsetPrinter(KafkaAdmin kafkaAdmin) {
    this.kafkaAdmin = kafkaAdmin;
    adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
  }

  @Scheduled(fixedRateString = "${print.offsets.rate.ms}")
  public void printOfSetsForChuckNorrisTopic() throws ExecutionException, InterruptedException {
    try {
      Map<String, TopicDescription> mapping = kafkaAdmin.describeTopics(TOPIC_NAME);
      int numberOfPartitions = mapping.get(TOPIC_NAME).partitions().size();

      ListOffsetsResult result = listOffsetResult(TOPIC_NAME, numberOfPartitions);
      printOffsets(result);
    } catch (KafkaException kafkaException) {
      log.warn("Error receiving topic information!", kafkaException);
    }
  }

  private void printOffsets(ListOffsetsResult result) throws ExecutionException, InterruptedException {
    Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> map = result.all().get();

    log.info("============================================================");
    for (TopicPartition topicPartition : map.keySet()) {
      String topic = topicPartition.topic();
      int partition = topicPartition.partition();
      ListOffsetsResult.ListOffsetsResultInfo listOffsetsResultInfo = map.get(topicPartition);
      long timestamp = listOffsetsResultInfo.timestamp();
      LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
      log.info("Topic: {}, Partition: {}, Offset: {}, Timestamp: {}, Date: {}", topic, partition, listOffsetsResultInfo.offset(), timestamp, localDateTime);
    }
    log.info("============================================================");
  }

  private ListOffsetsResult listOffsetResult(String topicName, int numberOfPartitions) {
    Map<TopicPartition, OffsetSpec> topicPartitionOffsets = new HashMap<>();

    for (int i = 0; i < numberOfPartitions; i++) {
      topicPartitionOffsets.put(new TopicPartition(topicName, i), OffsetSpec.maxTimestamp());
    }

    return adminClient.listOffsets(topicPartitionOffsets);
  }
}
