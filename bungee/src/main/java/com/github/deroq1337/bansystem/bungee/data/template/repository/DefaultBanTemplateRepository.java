package com.github.deroq1337.bansystem.bungee.data.template.repository;

import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.database.MySQL;
import com.github.deroq1337.bansystem.bungee.data.database.result.DBRow;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultBanTemplateRepository implements BanTemplateRepository {

    private static final String CREATE_TEMPLATE_QUERY = "INSERT INTO bansystem_templates " +
            "(id, type, reason, duration) " +
            "VALUES (?, ?, ?, ?);";

    private static final String DELETE_TEMPLATE_QUERY = "DELETE FROM bansystem_templates " +
            "WHERE id = ?;";

    private static final String GET_TEMPLATE_QUERY = "SELECT * FROM bansystem_templates " +
            "WHERE id = ?;";

    private static final String GET_ALL_TEMPLATES_QUERY = "SELECT * FROM bansystem_templates;";

    private @NotNull
    final MySQL mySQL;

    public DefaultBanTemplateRepository(@NotNull BanSystemPlugin plugin) {
        this.mySQL = plugin.getMySQL();

        createTable();
    }

    private void createTable() {
        mySQL.update("CREATE TABLE IF NOT EXISTS bansystem_templates(" +
                "id VARCHAR(8) NOT NULL, " +
                "type VARCHAR(32) NOT NULL, " +
                "reason VARCHAR(64) NOT NULL, " +
                "duration BIGINT NOT NULL, " +
                "PRIMARY KEY(id)" +
                ");").join();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> createTemplate(@NotNull BanTemplate template) {
        return mySQL.update(CREATE_TEMPLATE_QUERY, template.id(), template.type().toString(), template.reason(), template.duration())
                .thenApply(count -> count == 1);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteTemplateById(@NotNull String templateId) {
        return mySQL.update(DELETE_TEMPLATE_QUERY, templateId)
                .thenApply(count -> count == 1);
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanTemplate>> getTemplateById(@NotNull String templateId) {
        return mySQL.query(GET_TEMPLATE_QUERY, templateId).thenApply(result -> {
            if (result.rows().isEmpty()) {
                return Optional.empty();
            }

            DBRow row = result.rows().getFirst();
            return Optional.of(mapTemplateFromRow(row));
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BanTemplate>> getTemplates() {
        return mySQL.query(GET_ALL_TEMPLATES_QUERY).thenApply(result -> result.rows().stream()
                .map(this::mapTemplateFromRow)
                .toList());
    }

    private @NotNull BanTemplate mapTemplateFromRow(@NotNull DBRow row) {
        return new BanTemplate(
                row.getValue("id", String.class),
                BanType.valueOf(row.getValue("type", String.class)),
                row.getValue("reason", String.class),
                row.getValue("duration", Long.class)
        );
    }
}
