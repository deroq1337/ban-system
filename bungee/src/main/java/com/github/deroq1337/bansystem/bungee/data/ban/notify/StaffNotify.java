package com.github.deroq1337.bansystem.bungee.data.ban.notify;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public interface StaffNotify {

    default void broadcast() {
        ProxyServer.getInstance().getPlayers().stream()
                .filter(player -> player.hasPermission("bansystem.notify"))
                .forEach(player -> player.sendMessage(TextComponent.fromLegacy(getMessage())));
    }

    @NotNull String getMessage();
}
