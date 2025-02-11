package com.github.deroq1337.bansystem.bungee.data.ban.models;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanTemplate;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class BanScreen {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final @NotNull Ban ban;
    private final @NotNull BanTemplate template;

    public @NotNull BaseComponent build() {
        return TextComponent.fromLegacy("§cDu wurdest vom Netzwerk gebannt!\n" +
                "§3Grund: §e" + template.reason() + "\n" +
                formatDuration());
    }

    private @NotNull String formatDuration() {
        return template.duration() == Integer.MAX_VALUE
                ? "§3Dauer: §4PERMANENT"
                : "§3Bis: §e" + formatExpiry(ban.expiresAt());
    }

    private @NotNull String formatExpiry(long expiresAt) {
        return Instant.ofEpochMilli(expiresAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DATE_TIME_FORMATTER);
    }
}

