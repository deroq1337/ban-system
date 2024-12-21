package com.github.deroq1337.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class MalformedUnbanMessageException extends RuntimeException {

    public MalformedUnbanMessageException(@NotNull String message) {
        super(message);
    }
}
