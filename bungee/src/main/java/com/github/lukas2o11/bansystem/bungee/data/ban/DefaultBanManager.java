package com.github.lukas2o11.bansystem.bungee.data.ban;

import com.github.lukas2o11.bansystem.api.Ban;
import com.github.lukas2o11.bansystem.api.BanType;
import com.github.lukas2o11.bansystem.api.Unban;
import com.github.lukas2o11.bansystem.bungee.BanSystemPlugin;
import com.github.lukas2o11.bansystem.bungee.data.ban.exceptions.BanNotFoundException;
import com.github.lukas2o11.bansystem.bungee.data.ban.exceptions.UnbanException;
import com.github.lukas2o11.bansystem.bungee.data.ban.metrics.BanMetric;
import com.github.lukas2o11.bansystem.bungee.data.ban.metrics.UnbanMetric;
import com.github.lukas2o11.bansystem.bungee.data.ban.repository.BanRepository;
import com.github.lukas2o11.bansystem.bungee.data.ban.models.BanList;
import com.github.lukas2o11.bansystem.bungee.data.ban.models.BanListEntry;
import com.github.lukas2o11.bansystem.bungee.data.ban.repository.DefaultBanRepository;
import com.github.lukas2o11.bansystem.bungee.data.prometheus.PrometheusMetric;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultBanManager implements BanManager {

    private @NotNull final BanRepository repository;
    private @NotNull final PrometheusMetric banMetricExporter;
    private @NotNull final PrometheusMetric unbanMetricExporter;

    // TODO: cache

    public DefaultBanManager(@NotNull BanSystemPlugin plugin) {
        this.repository = new DefaultBanRepository(plugin);
        this.banMetricExporter = new BanMetric();
        this.unbanMetricExporter = new UnbanMetric();
    }

    @Override
    public @NotNull CompletableFuture<Void> banUser(@NotNull Ban ban, @NotNull String type) {
        return repository.banUser(ban).thenCompose(v -> {
            return banMetricExporter.export(ban.getPlayer().toString(), ban.getTemplateId(), ban.getBannedBy(), type);
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> unbanUser(@NotNull Unban unban) {
        return repository.unbanUser(unban).thenCompose(banType -> {
            if (banType.isEmpty()) {
                throw new UnbanException("Warning on unbanUser: banType is empty");
            }

            return banType
                    .map(type -> unbanMetricExporter.export(unban.player().toString(), unban.unbannedBy(), type))
                    .orElseThrow(() -> new UnbanException("Warning on unbanUser: banType is empty"));
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> unbanUserById(int banId, @NotNull String unbannedBy) {
        return getBanById(banId).thenCompose(optionalBan -> {
            Unban unban = optionalBan
                    .map(ban -> new Unban(ban.getPlayer(), banId, unbannedBy, System.currentTimeMillis()))
                    .orElseThrow(() -> new BanNotFoundException("Could not unban user by banId: Ban with id '" + banId + "' was not found"));
            return unbanUser(unban);
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isUserBanned(@NotNull UUID player, @NotNull BanType type) {
        return repository.isUserBanned(player, type);
    }

    @Override
    public @NotNull CompletableFuture<Optional<Ban>> getBanById(int banId) {
        return repository.getBanById(banId);
    }

    @Override
    public @NotNull CompletableFuture<Optional<Ban>> getBanByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return repository.getBanByPlayer(player, type);
    }

    @Override
    public @NotNull CompletableFuture<List<Ban>> getBansByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return repository.getBansByPlayer(player, type);
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanListEntry>> getBanByPlayerAsListEntry(@NotNull UUID player, @NotNull BanType type) {
        return repository.getBanByPlayerAsListEntry(player, type);
    }

    @Override
    public @NotNull CompletableFuture<BanList> getBanListByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return repository.getBanListByPlayer(player, type);
    }
}