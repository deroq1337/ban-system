package com.github.deroq1337.bansystem.bungee.data.template.repository;

import com.github.deroq1337.bansystem.api.BanTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BanTemplateRepository {

    @NotNull CompletableFuture<Boolean> createTemplate(@NotNull BanTemplate template);

    @NotNull CompletableFuture<Boolean> deleteTemplateById(@NotNull String id);

    @NotNull CompletableFuture<Optional<BanTemplate>> getTemplateById(@NotNull String id);

    @NotNull CompletableFuture<List<BanTemplate>> getTemplates();
}
