package com.lukas2o11.bansystem.web.data.models;

import com.github.lukas2o11.bansystem.api.Ban;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BanListEntry extends Ban {

    @NotNull
    private final String reason;

    public BanListEntry(@NotNull Integer id, @NotNull UUID player, @NotNull String templateId, @NotNull String bannedBy, long bannedAt, long expiresAt, boolean active, @NotNull String reason) {
        super(id, player, templateId, bannedBy, bannedAt, expiresAt, active);
        this.reason = reason;
    }
}
