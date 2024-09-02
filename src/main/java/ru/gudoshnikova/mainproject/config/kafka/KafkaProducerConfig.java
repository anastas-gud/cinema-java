package ru.gudoshnikova.mainproject.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.ui.Model;
import ru.gudoshnikova.mainproject.dto.OrderDTO;
import ru.gudoshnikova.mainproject.model.Order;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ReplyingKafkaTemplate < String, OrderDTO, OrderDTO > replyKafkaTemplate(
            @Qualifier("producer") ProducerFactory < String, OrderDTO > pf,
            KafkaMessageListenerContainer < String, OrderDTO > lc) {
        ReplyingKafkaTemplate<String, OrderDTO, OrderDTO> replyKafkaTemplate = new ReplyingKafkaTemplate<>(pf, lc);
        replyKafkaTemplate.setSharedReplyTopic(false);
        replyKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        return replyKafkaTemplate;
    }

    @Bean
    public KafkaMessageListenerContainer < String, OrderDTO > replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties("topic-2");
        return new KafkaMessageListenerContainer < > (replyConsumerFactory(), containerProperties);
    }

    @Bean
    public Map < String, Object > consumerConfigs() {
        Map < String, Object > props = new HashMap < > ();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        return props;
    }

    @Bean
    public Map < String, Object > producerConfigs() {
        Map < String, Object > props = new HashMap < > ();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    @Qualifier("producer")
    public ProducerFactory < String, OrderDTO > requestProducerFactory() {
        return new DefaultKafkaProducerFactory < > (producerConfigs());
    }

    @Bean
    public ConsumerFactory < String, OrderDTO > replyConsumerFactory() {
        return new DefaultKafkaConsumerFactory < > (consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<>(OrderDTO.class));
    }

    @Bean
    @Qualifier("producerKafkaTemplate")
    public KafkaTemplate<String, OrderDTO> userKafkaTemplate() {
        return new KafkaTemplate<>(requestProducerFactory());
    }

}
