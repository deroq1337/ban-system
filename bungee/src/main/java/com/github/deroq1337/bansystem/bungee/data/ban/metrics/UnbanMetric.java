package com.github.deroq1337.bansystem.bungee.data.ban.metrics;

import com.github.deroq1337.bansystem.bungee.data.prometheus.PrometheusMetric;
import io.prometheus.client.Counter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public class UnbanMetric implements PrometheusMetric {

    private static final String METRIC_NAME = "bansystem_unbans_total";

    private @NotNull final List<String> labelNames;
    private @NotNull final Counter counter;

    public UnbanMetric() {
        this.labelNames = Arrays.asList("player", "unbannedBy", "type");
        this.counter = Counter.build()
                .name(METRIC_NAME)
                .help("number of unbans")
                .labelNames(labelNames.toArray(new String[0]))
                .create();
    }
}
