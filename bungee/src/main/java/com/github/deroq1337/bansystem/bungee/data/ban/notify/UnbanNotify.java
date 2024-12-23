package com.github.deroq1337.bansystem.bungee.data.ban.notify;

import com.github.deroq1337.bansystem.api.Unban;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class UnbanNotify implements StaffNotify {

    private final @NotNull Unban unban;

    @Override
    public @NotNull String getMessage() {
        return "§a" + unban.unbannedBy() + " §3hat die Strafe von §c" + unban.player().toString() + " §3aufgehoben";
    }
}
