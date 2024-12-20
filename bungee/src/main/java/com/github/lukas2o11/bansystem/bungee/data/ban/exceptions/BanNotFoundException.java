package com.github.lukas2o11.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class BanNotFoundException extends RuntimeException {

    public BanNotFoundException(@NotNull String message) {
        super(message);
    }
}
