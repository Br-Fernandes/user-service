package io.github.brfernandes.userservice.configs;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import io.github.brfernandes.userservice.dtos.UserAuthenticationDto;
import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.utils.serializers.UserAuthenticationSerializer;
import io.github.brfernandes.userservice.utils.serializers.UserSerializer;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    // -- User Email Configuration --

    @Bean
    public ProducerFactory<String, UserDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class); // Alterado para StringSerializer

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UserDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // -- User Authentication Configuration --

    @Bean
    public ProducerFactory<String, UserAuthenticationDto> authenticationProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserAuthenticationSerializer.class); 

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UserAuthenticationDto> authenticationKafkaTemplate() {
        return new KafkaTemplate<>(authenticationProducerFactory());
    }
}