package com.github.lukas2o11.bansystem.bungee.data.ban.commands;

import com.github.lukas2o11.bansystem.api.BanType;
import com.github.lukas2o11.bansystem.bungee.BanSystemPlugin;
import org.jetbrains.annotations.NotNull;

public class MuteCommand extends BaseBanCommand {

    public MuteCommand(@NotNull String name, @NotNull BanSystemPlugin plugin) {
        super(name, plugin, BanType.MUTE);
    }
}