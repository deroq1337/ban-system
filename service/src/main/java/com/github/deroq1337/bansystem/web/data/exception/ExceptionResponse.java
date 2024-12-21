package com.github.deroq1337.bansystem.web.data.exception;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ExceptionResponse {

    private final int statusCode;
    private @NotNull final String message;
    private final long timestamp = System.currentTimeMillis();
}
