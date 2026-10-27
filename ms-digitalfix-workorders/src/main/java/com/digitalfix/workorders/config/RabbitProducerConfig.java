package com.digitalfix.workorders.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitProducerConfig {

//enviar los mensajes en formato json 
    @Bean
    public Jackson2JsonMessageConverter producerJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//aqui seprepara la plantilla de rabbitmq en el formarto json
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(producerJsonMessageConverter());
        return template;
    }
}

