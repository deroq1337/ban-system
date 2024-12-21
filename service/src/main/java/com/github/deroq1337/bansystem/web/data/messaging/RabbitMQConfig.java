package com.github.deroq1337.bansystem.web.data.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange_name}")
    private String exchangeName;

    @Value("${rabbitmq.unban.player.queue_name}")
    private String unbanPlayerQueueName;

    @Value("${rabbitmq.unban.player.routing_key}")
    private String unbanPlayerRoutingKey;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName, false, true);
    }

    @Bean
    public Queue unbanPlayerQueue() {
        return new Queue(unbanPlayerQueueName, false, false, false);
    }

    @Bean
    public Binding unbanPlayerBinding(Queue unbanPlayerQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(unbanPlayerQueue)
                .to(directExchange)
                .with(unbanPlayerRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchangeName);
        return rabbitTemplate;
    }
}
