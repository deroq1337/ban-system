package com.github.deroq1337.bansystem.bungee.data.template;

import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.template.repository.BanTemplateRepository;
import com.github.deroq1337.bansystem.bungee.data.template.repository.DefaultBanTemplateRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DefaultBanTemplateManager implements BanTemplateManager {

    private final @NotNull BanTemplateRepository repository;
    private final @NotNull LoadingCache<String, CompletableFuture<Optional<BanTemplate>>> templateCache;

    public DefaultBanTemplateManager(@NotNull BanSystemPlugin plugin) {
        this.repository = new DefaultBanTemplateRepository(plugin);
        this.templateCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull CompletableFuture<Optional<BanTemplate>> load(@NotNull String key) throws Exception {
                        return getTemplateByIdFromDatabase(key);
                    }
                });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> createTemplate(@NotNull BanTemplate template) {
        return repository.createTemplate(template).thenApply(acknowledged -> {
            templateCache.invalidate(template.getId());
            return acknowledged;
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteTemplateById(@NotNull String id) {
        return repository.deleteTemplateById(id).thenApply(acknowledged -> {
            templateCache.invalidate(id);
            return acknowledged;
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanTemplate>> getTemplateById(@NotNull String id) {
        return templateCache.getUnchecked(id);
    }

    private @NotNull CompletableFuture<Optional<BanTemplate>> getTemplateByIdFromDatabase(@NotNull String id) {
        return repository.getTemplateById(id.toUpperCase());
    }

    @Override
    public @NotNull CompletableFuture<List<BanTemplate>> getTemplates() {
        return repository.getTemplates();
    }
}
