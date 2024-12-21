package com.github.deroq1337.bansystem.web.data.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BanSystemUnavailableException extends RuntimeException {

    public BanSystemUnavailableException(String message) {
        super(message);
    }
}
