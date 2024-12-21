package com.github.deroq1337.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class BanNotFoundException extends RuntimeException {

    public BanNotFoundException(@NotNull String message) {
        super(message);
    }
}
