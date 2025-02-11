package com.github.deroq1337.bansystem.bungee.data.ban.models;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanTemplate;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class MuteMessage {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final @NotNull Ban ban;
    private final @NotNull BanTemplate template;

    public @NotNull BaseComponent build() {
        return TextComponent.fromLegacy("§cDu wurdest " + formatDuration() + " aus dem Chat gebannt!\n" +
                "§3Grund: §c" + template.reason() + "\n" +
                "§3Verbleibend: §e" + formatMillis(ban.expiresAt() - System.currentTimeMillis()));
    }

    private @NotNull String formatDuration() {
        return template.duration() == Integer.MAX_VALUE
                ? "§4PERMANENT"
                : "für §4" + formatMillis(template.duration());
    }

    private @NotNull String formatMillis(long duration) {
        long days = duration / (1000 * 60 * 60 * 24);
        long hours = (duration / (1000 * 60 * 60)) % 24;
        long minutes = (duration / (1000 * 60)) % 60;
        long seconds = (duration / 1000) % 60;

        if (days > 0) {
            return days + (days == 1 ? " Tag" : " Tage");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " Stunde" : " Stunden");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " Minute" : " Minuten");
        } else if (seconds > 0) {
            return seconds + (seconds == 1 ? " Sekunde" : " Sekunden");
        } else {
            return "0 Sekunden";
        }
    }
}