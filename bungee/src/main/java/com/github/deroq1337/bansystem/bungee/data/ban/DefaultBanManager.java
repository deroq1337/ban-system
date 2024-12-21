package com.github.deroq1337.bansystem.bungee.data.ban;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.api.Unban;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.BanNotFoundException;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.EmptyBanTypeException;
import com.github.deroq1337.bansystem.bungee.data.ban.metrics.BanMetric;
import com.github.deroq1337.bansystem.bungee.data.ban.metrics.UnbanMetric;
import com.github.deroq1337.bansystem.bungee.data.ban.repository.BanRepository;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanList;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanListEntry;
import com.github.deroq1337.bansystem.bungee.data.ban.repository.DefaultBanRepository;
import com.github.deroq1337.bansystem.bungee.data.ban.tasks.ExpiredBanReaper;
import com.github.deroq1337.bansystem.bungee.data.prometheus.PrometheusMetric;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultBanManager implements BanManager {

    private @NotNull final BanRepository repository;
    private @NotNull final PrometheusMetric banMetric;
    private @NotNull final PrometheusMetric unbanMetric;
    private @NotNull final ExpiredBanReaper expiredReaper;

    // TODO: cache

    public DefaultBanManager(@NotNull BanSystemPlugin plugin) {
        this.repository = new DefaultBanRepository(plugin);
        this.banMetric = new BanMetric();
        this.unbanMetric = new UnbanMetric();
        this.expiredReaper = new ExpiredBanReaper(plugin, repository);
    }

    @Override
    public void stopExpiredBanReaper() {
        expiredReaper.cancel();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> banUser(@NotNull Ban ban, @NotNull BanType type) {
        return repository.banUser(ban).thenApply(acknowledged -> {
            banMetric.export(ban.getPlayer().toString(), ban.getTemplateId(), ban.getBannedBy(), type.toString());
            return acknowledged;
        }).exceptionally(t -> {
            System.err.println("Error banning user: " + t);
            return false;
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> unbanUser(@NotNull Unban unban) {
        return repository.unbanUser(unban).thenApply(banType -> {
            if (banType.isEmpty()) {
                throw new EmptyBanTypeException("Error unbanning user: banType is empty");
            }

            return banType.map(type -> {
                unbanMetric.export(unban.player().toString(), unban.unbannedBy(), type);
                return true;
            }).orElseThrow(() -> new EmptyBanTypeException("Warning on unbanUser: banType is empty"));
        }).exceptionally(t -> {
            System.err.println("Error unbanning user: " + t);
            return false;
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> unbanUserById(int banId, @NotNull String unbannedBy) {
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