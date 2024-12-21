package com.github.lukas2o11.bansystem.bungee.data.ban.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UUIDFetcher {

    private static final LoadingCache<String, CompletableFuture<Optional<UUID>>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull CompletableFuture<Optional<UUID>> load(@NotNull String name) {
                    return Optional.ofNullable(ProxyServer.getInstance().getPlayer(name))
                            .map(player -> CompletableFuture.completedFuture(Optional.of(player.getUniqueId())))
                            .orElse(fetchUuid(name));
                }
            });

    public static @NotNull CompletableFuture<Optional<UUID>> getUuid(@NotNull String name) {
        return cache.getUnchecked(name.toLowerCase());
    }

    private static @NotNull CompletableFuture<Optional<UUID>> fetchUuid(@NotNull String name) {
        // implement fetch
        return CompletableFuture.completedFuture(Optional.of(UUID.randomUUID()));
    }
}
