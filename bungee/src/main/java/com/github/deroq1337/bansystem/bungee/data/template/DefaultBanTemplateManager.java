package com.github.deroq1337.bansystem.bungee.data.template;

import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.database.MySQL;
import com.github.deroq1337.bansystem.bungee.data.database.result.DBRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultBanTemplateManager implements BanTemplateManager {

    private static final String CREATE_TEMPLATE_QUERY = "INSERT INTO bansystem_templates " +
            "(id, type, reason, duration) " +
            "VALUES (?, ?, ?, ?);";
    private static final String DELETE_TEMPLATE_QUERY = "DELETE FROM bansystem_templates " +
            "WHERE id = ?;";
    private static final String GET_TEMPLATE_QUERY = "SELECT * FROM bansystem_templates " +
            "WHERE id = ?;";
    private static final String GET_ALL_TEMPLATES_QUERY = "SELECT * FROM bansystem_templates;";

    private final MySQL mySQL;

    public DefaultBanTemplateManager(BanSystemPlugin plugin) {
        this.mySQL = plugin.getMySQL();
        createTables();
    }

    private void createTables() {
        mySQL.update("CREATE TABLE IF NOT EXISTS bansystem_templates(" +
                "id VARCHAR(8) NOT NULL, " +
                "type VARCHAR(32) NOT NULL, " +
                "reason VARCHAR(64) NOT NULL, " +
                "duration BIGINT NOT NULL, " +
                "PRIMARY KEY(id)" +
                ");").join();
    }

    @Override
    public @NotNull CompletableFuture<Optional<BanTemplate>> getTemplate(@NotNull String templateId) {
        return mySQL.query(GET_TEMPLATE_QUERY, templateId).thenApply(result -> {
            if (result.rows().isEmpty()) {
                return Optional.empty();
            }

            DBRow row = result.rows().getFirst();
            return Optional.of(new BanTemplate(
                    row.getValue("id", String.class),
                    BanType.valueOf(row.getValue("type", String.class)),
                    row.getValue("reason", String.class),
                    row.getValue("duration", Long.class)
            ));
        });
    }

    @Override
    public @NotNull CompletableFuture<List<BanTemplate>> getAllTemplates() {
        return mySQL.query(GET_ALL_TEMPLATES_QUERY).thenApply(result -> {
            List<DBRow> rows = result.rows();
            return mapTemplatesFromRows(rows);
        });
    }

    @Override
    public void createTemplate(@NotNull String templateId, @NotNull BanType banType, @NotNull String reason, long duration) {
        mySQL.update(CREATE_TEMPLATE_QUERY, templateId, banType.toString(), reason, duration);
    }

    @Override
    public void deleteTemplate(@NotNull String templateId) {
        mySQL.update(DELETE_TEMPLATE_QUERY, templateId).join();
    }

    private List<BanTemplate> mapTemplatesFromRows(List<DBRow> rows) {
        List<BanTemplate> templates = new ArrayList<>();
        for (DBRow row : rows) {
            templates.add(new BanTemplate(
                    row.getValue("id", String.class),
                    BanType.valueOf(row.getValue("type", String.class)),
                    row.getValue("reason", String.class),
                    row.getValue("duration", Long.class)
            ));
        }
        return templates;
    }

}
