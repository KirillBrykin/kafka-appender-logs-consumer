package com.example.kafkaappenderlogsconsumer.consumer;

import com.example.kafkaappenderlogsconsumer.services.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static java.text.MessageFormat.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetLogsKafkaConsumer {
    private final MessageService messageService;

    @KafkaListener(id = "budgetLogsListener",
            topics = "${kafka-consumer-config.topic-name}",
            groupId = "${kafka-consumer-config.group-id}")
    public void listen(@NonNull @Payload String message,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.OFFSET) Long offset,
                       Acknowledgment acknowledgment) {
        log.info("Message({}) has been received from partition {} with offset {} and message body:\n{}", key, partition, offset, message);
        try {
            messageService.sendMessage(message);
            acknowledgment.acknowledge();
            log.info("Message({}) has been successfully acknowledged", key);
        } catch (Exception ex) {
            log.error(format("Message({0}) will be rejected, because of handling error: {1}", key, ex.getMessage()), ex);
            acknowledgment.nack(Duration.ofSeconds(3));
            log.error("Message({}) has been rejected", key);
        }
    }
}