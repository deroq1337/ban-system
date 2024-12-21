package com.github.deroq1337.bansystem.bungee.data.prometheus;

import io.prometheus.client.Counter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PrometheusMetric {

    default CompletableFuture<Void> export(String... labelValues) {
        return CompletableFuture.runAsync(() -> {
            if (labelValues.length != getLabelNames().size()) {
                throw new IllegalStateException("Error exporting metric " + getClass() + ": labelValues length is not equal to labelNames size");
            }

            getCounter().labels(labelValues).inc();
            System.out.println(getClass().getSimpleName() + " exported metric: " + Arrays.toString(labelValues));
        });
    }

    @NotNull Counter getCounter();

    @NotNull List<String> getLabelNames();
}
