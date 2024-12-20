package com.github.lukas2o11.bansystem.bungee.data.ban.repository;

import com.github.lukas2o11.bansystem.api.Ban;
import com.github.lukas2o11.bansystem.api.BanType;
import com.github.lukas2o11.bansystem.api.Unban;
import com.github.lukas2o11.bansystem.bungee.data.ban.models.BanList;
import com.github.lukas2o11.bansystem.bungee.data.ban.models.BanListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BanRepository {

    @NotNull CompletableFuture<Void> banUser(@NotNull Ban ban);

    @NotNull CompletableFuture<Optional<String>> unbanUser(@NotNull Unban unban);

    @NotNull CompletableFuture<Boolean> isUserBanned(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<Ban>> getBanById(int banId);

    @NotNull CompletableFuture<Optional<Ban>> getBanByPlayer(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<List<Ban>> getBansByPlayer(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<BanListEntry>> getBanByPlayerAsListEntry(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<BanList> getBanListByPlayer(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<String>> getBanTypeById(int banId);
}
