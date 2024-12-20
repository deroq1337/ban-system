package com.github.lukas2o11.bansystem.bungee.data.prometheus;

import io.prometheus.client.exporter.HTTPServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class Prometheus {

    private @NotNull final Thread thread;
    private Optional<HTTPServer> httpServer = Optional.empty();

    public Prometheus() {
        this.thread = new Thread(() -> {
            try {
                this.httpServer = Optional.of(new HTTPServer("localhost", 9201));
                System.out.println("Prometheus connected");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void connect() {
        thread.start();
    }

    public void disconnect() {
        httpServer.ifPresent(HTTPServer::close);
        thread.interrupt();

        System.out.println("Prometheus connection closed");
    }
}
