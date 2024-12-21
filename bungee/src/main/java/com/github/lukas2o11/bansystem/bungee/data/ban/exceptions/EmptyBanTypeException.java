package com.github.lukas2o11.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class EmptyBanTypeException extends RuntimeException {

    public EmptyBanTypeException(@NotNull String message) {
        super(message);
    }
}
