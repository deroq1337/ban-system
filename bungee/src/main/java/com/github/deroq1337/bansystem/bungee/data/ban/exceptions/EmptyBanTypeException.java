package com.github.deroq1337.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class EmptyBanTypeException extends RuntimeException {

    public EmptyBanTypeException(@NotNull String message) {
        super(message);
    }
}
