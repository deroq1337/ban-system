package com.github.deroq1337.bansystem.api;

import org.jetbrains.annotations.NotNull;

public record BanTemplate(@NotNull String id, @NotNull BanType type, @NotNull String reason, long duration) {

}
