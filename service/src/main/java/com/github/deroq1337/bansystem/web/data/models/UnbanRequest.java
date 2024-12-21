package com.github.deroq1337.bansystem.web.data.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UnbanRequest {

    private @NotNull Integer id;
}
