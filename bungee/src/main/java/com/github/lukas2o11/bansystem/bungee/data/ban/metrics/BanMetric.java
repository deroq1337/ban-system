package com.github.lukas2o11.bansystem.bungee.data.ban.metrics;

import com.github.lukas2o11.bansystem.bungee.data.prometheus.PrometheusMetric;
import io.prometheus.client.Counter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public class BanMetric implements PrometheusMetric {

    private static final String METRIC_NAME = "bansystem_bans_total";

    private @NotNull final List<String> labelNames;
    private @NotNull final Counter counter;

    public BanMetric() {
        this.labelNames = Arrays.asList("player", "templateId", "bannedBy", "type");
        this.counter = Counter.build()
                .name(METRIC_NAME)
                .help("number of bans")
                .labelNames(labelNames.toArray(new String[0]))
                .create();
    }
}
