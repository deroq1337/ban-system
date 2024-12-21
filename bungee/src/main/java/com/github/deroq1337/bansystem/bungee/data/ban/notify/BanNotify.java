package com.github.deroq1337.bansystem.bungee.data.ban.notify;

import com.github.deroq1337.bansystem.api.Ban;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BanNotify implements StaffNotify {

    private @NotNull final Ban ban;

    @Override
    public @NotNull String getMessage() {
        return "§c" + ban.getPlayer().toString() + " §3wurde von §a" + ban.getBannedBy() + " §3bestraft\n" +
                "§3Template-ID: §e" + ban.getTemplateId();
    }
}
