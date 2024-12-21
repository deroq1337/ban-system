package com.github.deroq1337.bansystem.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Unban(@NotNull UUID player, @NotNull Integer banId, @NotNull String unbannedBy, long unbannedAt) {
}
