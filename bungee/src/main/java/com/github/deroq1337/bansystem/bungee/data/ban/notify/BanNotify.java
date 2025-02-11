package com.github.deroq1337.bansystem.bungee.data.ban.notify;

import com.github.deroq1337.bansystem.api.Ban;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BanNotify extends StaffNotify {

    private final @NotNull Ban ban;

    @Override
    public @NotNull String getMessage() {
        return "§c" + ban.player().toString() + " §3wurde von §a" + ban.bannedBy() + " §3bestraft\n" +
                "§3Template-ID: §e" + ban.templateId();
    }
}
