package com.github.deroq1337.bansystem.bungee.data.ban.messaging.consumer;

import com.github.deroq1337.bansystem.bungee.data.ban.BanManager;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.MalformedUnbanMessageException;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class UnbanMessageConsumer implements DeliverCallback {

    private final @NotNull BanManager banManager;

    public UnbanMessageConsumer(@NotNull BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public void handle(String s, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        if (message.isEmpty()) {
            return;
        }

        int banId;
        try {
            banId = Integer.parseInt(message);
        } catch (NumberFormatException e) {
            throw new MalformedUnbanMessageException("Received invalid unban message: " + message);
        }

        banManager.unbanUserByBanId(banId, "WEB_INTERFACE").thenRun(() -> {
            System.out.println("Unbanned user via webinterface: " + banId);
        });
    }
}
