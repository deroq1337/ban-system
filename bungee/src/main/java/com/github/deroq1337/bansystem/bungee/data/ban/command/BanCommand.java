package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanScreen;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BanCommand extends BaseBanCommand {

    public BanCommand(@NotNull BanSystemPlugin plugin) {
        super("ban", plugin, BanType.BAN);
    }

    @Override
    public void onSuccess(@NotNull UUID targetUuid, @NotNull Ban ban, @NotNull BanTemplate template) {
        Optional.ofNullable(ProxyServer.getInstance().getPlayer(targetUuid)).ifPresent(player -> {
            if (player.isConnected()) {
                player.disconnect(new BanScreen(ban, template).build());
            }
        });
    }
}
