package com.github.deroq1337.bansystem.bungee.data.ban;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.api.Unban;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanList;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BanManager {

    @NotNull CompletableFuture<Boolean> banUser(@NotNull Ban ban, @NotNull BanType type);

    @NotNull CompletableFuture<Boolean> unbanUser(@NotNull Unban unban);

    @NotNull CompletableFuture<Boolean> unbanUserById(int banId, @NotNull String unbannedBy);

    @NotNull CompletableFuture<Boolean> isUserBanned(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<Ban>> getBanById(int banId);

    @NotNull CompletableFuture<Optional<Ban>> getBanByPlayer(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<List<Ban>> getBansByPlayer(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<BanListEntry>> getBanByPlayerAsListEntry(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<BanList> getBanListByPlayer(@NotNull UUID player, @NotNull BanType type);
}
