package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand extends BaseUnbanCommand {

    public UnbanCommand(@NotNull BanSystemPlugin plugin) {
        super("unban", plugin, BanType.BAN);
    }
}
