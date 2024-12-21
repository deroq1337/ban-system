package com.github.deroq1337.bansystem.web.data.messaging.publisher;

import org.jetbrains.annotations.NotNull;

public interface RabbitMQMessagePublisher {

    void sendMessage(@NotNull String message);
}
