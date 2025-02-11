package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.models.MuteMessage;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class MuteCommand extends BaseBanCommand {

    public MuteCommand(@NotNull BanSystemPlugin plugin) {
        super("mute", plugin, BanType.MUTE);
    }

    @Override
    public void onSuccess(@NotNull UUID targetUuid, @NotNull Ban ban, @NotNull BanTemplate template) {
        Optional.ofNullable(ProxyServer.getInstance().getPlayer(targetUuid)).ifPresent(player -> {
            if (player.isConnected()) {
                player.sendMessage(new MuteMessage(ban, template).build());
            }
        });
    }
}