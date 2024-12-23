package com.github.deroq1337.bansystem.web.data.messaging.publisher;

import com.github.deroq1337.bansystem.web.data.exception.BanSystemUnavailableException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Log4j2
public class UnbanMessagePublisher implements RabbitMQMessagePublisher {

    private final @NotNull RabbitTemplate template;

    @Value("${rabbitmq.exchange_name}")
    private String exchangeName;

    @Value("${rabbitmq.unban.player.routing_key}")
    private String unbanPlayerRoutingKey;

    @Autowired
    public UnbanMessagePublisher(@NotNull RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        MessageProperties properties = new MessageProperties();
        properties.setReplyTo("");

        try {
            Optional.ofNullable(template.convertSendAndReceive(exchangeName, unbanPlayerRoutingKey, buildMessage(message, properties)))
                    .orElseThrow(BanSystemUnavailableException::new);
            log.info("RabbitMQ message sent: {}", message);
        } catch (AmqpException e) {
            log.error("Could not send RabbitMQ message", e);
            throw new RuntimeException(e);
        }
    }

    private @NotNull Message buildMessage(@NotNull String message, @NotNull MessageProperties properties) {
        return new Message(message.getBytes(StandardCharsets.UTF_8), properties);
    }
}
