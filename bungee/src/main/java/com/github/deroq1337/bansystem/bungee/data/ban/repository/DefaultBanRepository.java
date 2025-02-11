package com.github.deroq1337.bansystem.bungee.data.ban.repository;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.api.Unban;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanList;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanListEntry;
import com.github.deroq1337.bansystem.bungee.data.database.MySQL;
import com.github.deroq1337.bansystem.bungee.data.database.result.DBRow;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DefaultBanRepository implements BanRepository {

    private static final String BAN_QUERY = "INSERT INTO bansystem_bans" +
            "(player, templateId, bannedBy, bannedAt, expiresAt) " +
            "VALUES" +
            "(?, ?, ?, ?, ?);";

    private static final String UNBAN_INSERT_QUERY = "INSERT INTO bansystem_unbans" +
            "(player, banId, unbannedBy, unbannedAt) " +
            "VALUES" +
            "(?, ?, ?, ?);";

    private static final String IS_BANNED_QUERY = "SELECT bb.id, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "LEFT JOIN bansystem_unbans bu ON bu.banId = bb.id " +
            "WHERE bb.player = ? " +
            "AND bt.type = ? " +
            "AND bu.banId IS NULL " +
            "AND bb.expiresAt < CURRENT_TIMESTAMP " +
            "LIMIT 1;";

    private static final String GET_BAN_BY_ID_QUERY = "SELECT *" +
            "FROM bansystem_bans " +
            "WHERE id = ?;";

    private static final String GET_BAN_BY_PLAYER_QUERY = "SELECT bb.*, bt.type" +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "LEFT JOIN bansystem_unbans bu ON bu.banId = bb.id " +
            "WHERE bb.player = ? " +
            "AND bt.type = ? " +
            "AND bu.banId IS NULL " +
            "AND bb.expiresAt < CURRENT_TIMESTAMP " +
            "LIMIT 1;";

    private static final String GET_BANS_QUERY = "SELECT bb.*, bt.type" +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "WHERE player = ? " +
            "AND bt.type = ?;";

    private static final String GET_BAN_ENTRY_QUERY = "SELECT bb.*, bt.reason, bt.duration, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "LEFT JOIN bansystem_unbans bu ON bu.banId = bb.id " +
            "WHERE bb.player = ? " +
            "AND bt.type = ? " +
            "AND bu.banId IS NULL " +
            "AND bb.expiresAt < CURRENT_TIMESTAMP " +
            "LIMIT 1;";

    private static final String GET_BAN_ENTRIES_QUERY = "SELECT bb.*, bt.reason, bt.duration, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId" +
            "WHERE bb.player = ? " +
            "AND bt.type = ?;";

    private static final String GET_BAN_TYPE_QUERY = "SELECT bb.id, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "WHERE bb.id = ?;";

    private final @NotNull MySQL mySQL;

    public DefaultBanRepository(@NotNull BanSystemPlugin plugin) {
        this.mySQL = plugin.getMySQL();
        createTables();
    }

    private void createTables() {
        mySQL.update("CREATE TABLE IF NOT EXISTS bansystem_bans(" +
                "id INT AUTO_INCREMENT NOT NULL, " +
                "player VARCHAR(36) NOT NULL, " +
                "templateId VARCHAR(8) NOT NULL, " +
                "bannedBy VARCHAR(36) NOT NULL, " +
                "bannedAt BIGINT NOT NULL, " +
                "expiresAt BIGINT NOT NULL, " +
                "PRIMARY KEY(id), " +
                "FOREIGN KEY(templateId) REFERENCES bansystem_templates(id)" +
                ");").join();

        mySQL.update("CREATE TABLE IF NOT EXISTS bansystem_unbans(" +
                "id INT AUTO_INCREMENT NOT NULL, " +
                "player VARCHAR(36) NOT NULL, " +
                "banId INT NOT NULL, " +
                "unbannedBy VARCHAR(36) NOT NULL, " +
                "unbannedAt BIGINT NOT NULL, " +
                "PRIMARY KEY(id), " +
                "FOREIGN KEY(banId) REFERENCES bansystem_bans(id)" +
                ");").join();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> banUser(@NotNull Ban ban) {
        return mySQL.update(BAN_QUERY, ban.player().toString(), ban.templateId(), ban.bannedBy(), ban.bannedAt(), ban.expiresAt())
                .thenApply(count -> count == 1);
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanType>> unbanUser(@NotNull Unban unban) {
        int banId = unban.banId();
        return mySQL.update(UNBAN_INSERT_QUERY, unban.player().toString(), banId, unban.unbannedBy(), unban.unbannedAt())
                .thenCompose(v -> getBanTypeById(banId));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isUserBanned(@NotNull UUID player, @NotNull BanType type) {
        return mySQL.query(IS_BANNED_QUERY, player.toString(), type.toString()).thenApply(result -> result.rows().size() == 1);
    }

    @Override
    public @NotNull CompletableFuture<Optional<Ban>> getBanById(int banId) {
        return mySQL.query(GET_BAN_BY_ID_QUERY, banId).thenApply(result -> {
            if (result.rows().isEmpty()) {
                return Optional.empty();
            }

            if (result.rows().size() > 1) {
                return Optional.empty();
            }

            return Optional.of(banFromRow(result.rows().getFirst()));
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<Ban>> getBanByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return getBanFromQuery(player, type, this::banFromRow, GET_BAN_BY_PLAYER_QUERY);
    }

    @Override
    public @NotNull CompletableFuture<List<Ban>> getBansByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return getBansFromQuery(
                player,
                type,
                rows -> mapBansFromRows(rows, this::banFromRow),
                GET_BANS_QUERY
        );
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanListEntry>> getBanByPlayerAsListEntry(@NotNull UUID player, @NotNull BanType type) {
        return getBanFromQuery(player, type, this::listEntryFromRow, GET_BAN_ENTRY_QUERY);
    }

    @Override
    public @NotNull CompletableFuture<BanList> getBanListByPlayer(@NotNull UUID player, @NotNull BanType type) {
        return getBansFromQuery(
                player,
                type,
                rows -> new BanList(player, mapBansFromRows(rows, this::listEntryFromRow)),
                GET_BAN_ENTRIES_QUERY);
    }

    private <T> CompletableFuture<Optional<T>> getBanFromQuery(
            @NotNull UUID player, @NotNull BanType type,
            @NotNull Function<DBRow, T> mapper, @NotNull String query
    ) {
        return mySQL.query(query, player.toString(), type.toString()).thenApply(result -> {
            List<DBRow> rows = result.rows();
            if (rows.isEmpty()) {
                return Optional.empty();
            }

            if (rows.size() > 1) {
                return Optional.empty();
            }

            DBRow row = rows.getFirst();
            return Optional.of(mapper.apply(row));
        });
    }

    private <T> CompletableFuture<T> getBansFromQuery(
            @NotNull UUID player, @NotNull BanType type,
            @NotNull Function<List<DBRow>, T> mapper, @NotNull String query
    ) {
        return mySQL.query(query, player.toString(), type.toString()).thenApply(result -> {
            List<DBRow> rows = result.rows();
            if (rows.isEmpty()) {
                return mapper.apply(Collections.emptyList());
            }

            return mapper.apply(rows);
        });
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanType>> getBanTypeById(int banId) {
        return mySQL.query(GET_BAN_TYPE_QUERY, banId).thenApply(result -> {
            if (result.rows().isEmpty()) {
                System.out.println("Type for ban '" + banId + "' not found");
                return Optional.empty();
            }

            return result.rows().getFirst().getValueOptional("type", String.class)
                    .map(type -> BanType.valueOf(type.toUpperCase(Locale.ENGLISH)));
        });
    }

    private <T> @NotNull List<T> mapBansFromRows(List<DBRow> rows, Function<DBRow, T> mapper) {
        return rows.stream()
                .map(mapper)
                .toList();
    }

    private @NotNull Ban banFromRow(@NotNull DBRow row) {
        return new Ban(
                row.getValue("id", Integer.class),
                UUID.fromString(row.getValue("player", String.class)),
                row.getValue("templateId", String.class),
                row.getValue("bannedBy", String.class),
                row.getValue("bannedAt", Long.class),
                row.getValue("expiresAt", Long.class)
        );
    }

    private @NotNull BanListEntry listEntryFromRow(@NotNull DBRow row) {
        return new BanListEntry(
                UUID.fromString(row.getValue("player", String.class)),
                row.getValue("bannedBy", String.class),
                row.getValue("reason", String.class),
                row.getValue("duration", Long.class),
                row.getValue("expiresAt", Long.class)
        );
    }
}
