package com.github.lukas2o11.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class UnbanException extends RuntimeException {

    public UnbanException(@NotNull String message) {
        super(message);
    }
}
