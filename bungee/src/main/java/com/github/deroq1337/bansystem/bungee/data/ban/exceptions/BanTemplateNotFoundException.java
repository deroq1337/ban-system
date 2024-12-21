package com.github.deroq1337.bansystem.bungee.data.ban.exceptions;

import org.jetbrains.annotations.NotNull;

public class BanTemplateNotFoundException extends RuntimeException {

    public BanTemplateNotFoundException(@NotNull String message) {
        super(message);
    }
}