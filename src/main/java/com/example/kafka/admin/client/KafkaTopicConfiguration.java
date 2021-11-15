package com.example.kafka.admin.client;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class KafkaTopicConfiguration {

  public static final String TOPIC_NAME = "chuck-norris";

  @Bean
  public NewTopic createChuckNorrisTopic() {
    return TopicBuilder.name(TOPIC_NAME)
      .partitions(3)
      .replicas(1)
      .build();
  }
}
