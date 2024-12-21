package com.github.deroq1337.bansystem.bungee.data.ban.models;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record BanList(@NotNull UUID player, @NotNull List<BanListEntry> entries) {

    public @NotNull List<BanListEntry> getActiveBans(){
        return entries.stream()
                .filter(entry -> !entry.isExpired())
                .toList();
    }
}
