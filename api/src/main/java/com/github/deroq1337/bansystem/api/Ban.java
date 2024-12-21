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
    private @NotNull final UUID player;
    private @NotNull final String templateId;
    private @NotNull final String bannedBy;
    private final long bannedAt;
    private final long expiresAt;
    private boolean active;
}