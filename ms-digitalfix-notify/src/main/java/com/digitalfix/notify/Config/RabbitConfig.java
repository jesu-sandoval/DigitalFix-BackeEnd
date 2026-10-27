package com.digitalfix.notify.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig {

//esto es para la cola y ruta de correo
    public static final String QUEUE_EMAIL = "q.cmd.email";
    public static final String ROUTING_KEY_EMAIL = "email.*";
    public static final String EXCHANGE_NAME = "cmd.topic";

    // ruta de despacho
    public static final String QUEUE_DISPATCH = "q.cmd.dispatch";
    public static final String ROUTING_KEY_DISPATCH = "dispatch.#";

    @Value("${app.rabbitmq.exchange:cmd.direct}")
    private String directExchangeName;

    @Value("${app.rabbitmq.exchange-topic:cmd.topic}")
    private String topicExchangeName;

    @Value("${app.rabbitmq.exchange-dlx:cmd.dead.dlx}")
    private String dlxExchangeName;

    @Value("${app.rabbitmq.queue:q.cmd.report}")
    private String reportQueueName;

    @Value("${app.rabbitmq.queue-dlq:q.cmd.report.dlq}")
    private String reportDlqQueueName;

    @Value("${app.rabbitmq.routing-key:report.gen}")
    private String reportDirectRoutingKey;

    @Value("${app.rabbitmq.routing-key-topic:report.*}")
    private String reportTopicRoutingKey;

    @Value("${app.rabbitmq.routing-key-dlq:report.dlq}")
    private String reportDlqRoutingKey;

    // EXCHANGES
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchangeName, true, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlxExchangeName, true, false);
    }

    // COLAS y Cola DLQ
    @Bean
    public Queue reportDlqQueue() {
        return new Queue(reportDlqQueueName, true);
    }

    @Bean
    public Queue reportQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlxExchangeName);
        args.put("x-dead-letter-routing-key", reportDlqRoutingKey);
        return new Queue(reportQueueName, true, false, false, args);
    }

    //crea la cola de correos 
    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_EMAIL, true);
    }

    //crea la cola de despacho 
    @Bean
    public Queue dispatchQueue() {
        return new Queue(QUEUE_DISPATCH, true);
    }

    //los BINDINGS 
    @Bean
    public Binding reportDirectBinding(Queue reportQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(reportQueue)
                .to(directExchange)
                .with(reportDirectRoutingKey);
    }

    @Bean
    public Binding reportTopicBinding(Queue reportQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(reportQueue)
                .to(topicExchange)
                .with(reportTopicRoutingKey);
    }

    //    conecta la cola de correos con el topic
    @Bean
    public Binding bindingEmail(Queue emailQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(emailQueue).to(topicExchange).with(ROUTING_KEY_EMAIL);
    }

    //conecta la cola de despacho con el intercambiador topic
    @Bean
    public Binding bindingDispatch(Queue dispatchQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(dispatchQueue).to(topicExchange).with(ROUTING_KEY_DISPATCH);
    }

    @Bean
    public Binding reportDlqBinding(Queue reportDlqQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(reportDlqQueue)
                .to(deadLetterExchange)
                .with(reportDlqRoutingKey);
    }

    // CONVERTIDOR JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
