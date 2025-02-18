package com.github.deroq1337.bansystem.bungee.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<E, ID> {

    @NotNull CompletableFuture<Boolean> create(@NotNull E entity);

    @NotNull CompletableFuture<Boolean> deleteById(@NotNull ID id);

    @NotNull CompletableFuture<Optional<E>> findById(@NotNull ID id);

    @NotNull CompletableFuture<List<E>> listAll();
}
