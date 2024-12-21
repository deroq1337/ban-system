package com.github.deroq1337.bansystem.bungee.data.template.models;

import com.github.deroq1337.bansystem.api.BanType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BanTemplate {

    private final String id;
    private final BanType banType;
    private final String reason;
    private final long duration;

}
