package com.github.deroq1337.bansystem.bungee.data.ban.commands;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.command.BaseUnbanCommand;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand extends BaseUnbanCommand {

    public UnbanCommand(@NotNull String name, @NotNull BanSystemPlugin plugin) {
        super(name, plugin, BanType.BAN);
    }
}
