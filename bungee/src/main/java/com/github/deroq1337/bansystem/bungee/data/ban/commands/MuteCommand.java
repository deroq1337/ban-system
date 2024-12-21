package com.github.deroq1337.bansystem.bungee.data.ban.commands;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.command.BaseBanCommand;
import org.jetbrains.annotations.NotNull;

public class MuteCommand extends BaseBanCommand {

    public MuteCommand(@NotNull BanSystemPlugin plugin) {
        super("mute", plugin, BanType.MUTE);
    }
}