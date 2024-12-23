package com.github.deroq1337.bansystem.api;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Ban {

    private @Nullable Integer id;
    private final @NotNull UUID player;
    private final @NotNull String templateId;
    private final @NotNull String bannedBy;
    private final long bannedAt;
    private final long expiresAt;
    private boolean active;
}