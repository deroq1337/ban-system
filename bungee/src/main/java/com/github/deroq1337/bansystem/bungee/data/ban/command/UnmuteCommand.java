package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import org.jetbrains.annotations.NotNull;

public class UnmuteCommand extends BaseUnbanCommand {

    public UnmuteCommand(@NotNull BanSystemPlugin plugin) {
        super("unmute", plugin, BanType.MUTE);
    }
}

