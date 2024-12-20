package com.github.lukas2o11.bansystem.bungee.data.messaging;

import com.github.lukas2o11.bansystem.bungee.BanSystemPlugin;
import com.github.lukas2o11.bansystem.bungee.data.ban.messaging.consumer.UnbanMessageConsumer;
import com.rabbitmq.client.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RabbitMQ {

    private static final String EXCHANGE_NAME = "bansystem-exchange";
    private static final String QUEUE_NAME = "unbans";
    private static final String ROUTING_KEY = "unban.player";

    private @NotNull final BanSystemPlugin plugin;
    private @NotNull final ConnectionFactory connectionFactory;
    private Optional<Connection> connection = Optional.empty();
    private Optional<Channel> channel = Optional.empty();

    public RabbitMQ(@NotNull BanSystemPlugin plugin) {
        this.plugin = plugin;
        this.connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
    }

    public void connect() {

        try {
            this.connection = Optional.ofNullable(connectionFactory.newConnection());
            if (connection.isEmpty()) {
                throw new RuntimeException("Could not connect to RabbitMQ: ConnectionFactory returned nullish connection");
            }

            this.channel = Optional.ofNullable(connection.get().createChannel());
            if (channel.isEmpty()) {
                throw new RuntimeException("Could not create channel: Connection returned nullish channel");
            }

            Channel mqChannel = channel.get();
            mqChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
            mqChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
            mqChannel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            mqChannel.basicConsume(QUEUE_NAME, true, new UnbanMessageConsumer(plugin.getBanManager()), cancelCallback());

            System.out.println("RabbitMQ connected");
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to RabbitMQ", e);
        }
    }

    public void disconnect() {
        try {
            if (channel.isPresent()) {
                channel.get().close();
            }

            if (connection.isPresent()) {
                connection.get().close();
            }

            System.out.println("RabbitMQ connection closed");
        } catch (Exception e) {
            throw new RuntimeException("Error closing RabbitMQ connection", e);
        }
    }

    private CancelCallback cancelCallback() {
        return consumerTag -> {
            System.out.println("Cancelled consumer: " + consumerTag);
        };
    }
}
