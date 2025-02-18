package com.github.deroq1337.bansystem.bungee.data.ban.repository;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.api.Unban;
import com.github.deroq1337.bansystem.bungee.data.Repository;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanList;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BanRepository extends Repository<Ban, Integer> {

    @NotNull CompletableFuture<Optional<BanType>> unbanUser(@NotNull Unban unban);

    @NotNull CompletableFuture<Boolean> existsBanByPlayerAndType(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<Ban>> findByPlayerAndType(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<List<Ban>> listByPlayerAndType(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<BanListEntry>> findByPlayerAndTypeAsBanListEntry(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<BanList> findByPlayerAndTypeAsBanList(@NotNull UUID player, @NotNull BanType type);

    @NotNull CompletableFuture<Optional<BanType>> findTypeOfBanById(Integer banId);
}
