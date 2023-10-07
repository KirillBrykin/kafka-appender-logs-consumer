package com.example.kafkaappenderlogsconsumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {
    private String bootstrapServers;
    private String topicName;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;
    private String autoOffsetReset;
    private Boolean autoStartup;
    private String securityProtocol;
    private String saslMechanism;
    private String saslJaasConfig;
}