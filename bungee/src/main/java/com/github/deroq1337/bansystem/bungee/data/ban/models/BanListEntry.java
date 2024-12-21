package com.github.deroq1337.bansystem.bungee.data.ban.models;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record BanListEntry(@NotNull UUID player, @NotNull String bannedBy, @NotNull String reason, long duration, long expiresAt) {

    public boolean isExpired(){
        return System.currentTimeMillis() > expiresAt;
    }
}
