package com.github.deroq1337.bansystem.api;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BanTemplate {

    private @NotNull final String id;
    private @NotNull BanType type;
    private @NotNull String reason;
    private long duration;
}
