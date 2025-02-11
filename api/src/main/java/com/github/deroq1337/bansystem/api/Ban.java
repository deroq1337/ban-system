package com.github.deroq1337.bansystem.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Ban(int id, @NotNull UUID player, @NotNull String templateId, @NotNull String bannedBy, long bannedAt, long expiresAt) {

}