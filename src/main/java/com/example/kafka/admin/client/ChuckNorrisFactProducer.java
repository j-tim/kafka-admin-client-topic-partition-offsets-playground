package com.example.kafka.admin.client;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChuckNorrisFactProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Faker faker;

  public ChuckNorrisFactProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.faker = Faker.instance();
  }

  @Scheduled(cron = "${chuck.norris.fact.producer.cron}")
  public void produce() {
    ChuckNorris chuckNorris = faker.chuckNorris();
    String fact = chuckNorris.fact();

    log.info("Produce Chuck norris fact: {}", fact);

    kafkaTemplate.send(KafkaTopicConfiguration.TOPIC_NAME, fact);
  }

}
