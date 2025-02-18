package com.github.deroq1337.bansystem.web.data.repository;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.web.data.models.BanListEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultBanRepository implements BanRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT * " +
            "FROM bansystem_bans " +
            "WHERE id = ?;";

    private static final String FIND_BY_ID_AS_ENTRY_QUERY = "SELECT bb.*, bt.id, bt.reason, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "WHERE bb.id = ?;";

    private static final String FIND_ALL_QUERY = "SELECT *, bt.type " +
            "FROM bansystem_bans bb " +
            "INNER JOIN bansystem_templates bt ON bt.id = bb.templateId " +
            "WHERE bt.type = ? " +
            "LIMIT ? OFFSET ?;";

    private static final String COUNT_QUERY = "SELECT COUNT(*) " +
            "FROM bansystem_bans;";

    private final @NotNull JdbcTemplate jdbcTemplate;

    @Autowired
    public DefaultBanRepository(@NotNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Ban> findById(@NotNull Integer id) {
        return Optional.of(jdbcTemplate.query(FIND_BY_ID_QUERY, banFromRow(), id)).flatMap(bans -> bans.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(bans.getFirst()));
    }

    @Override
    public Optional<BanListEntry> findByIdAsBanListEntry(@NotNull Integer id) {
        return Optional.of(jdbcTemplate.query(FIND_BY_ID_AS_ENTRY_QUERY, listEntryFromRow(), id)).flatMap(bans -> bans.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(bans.getFirst()));
    }

    @Override
    public @NotNull Page<Ban> findAll(Optional<String> type, @NotNull Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        List<Ban> bans = jdbcTemplate.query(FIND_ALL_QUERY, banFromRow(), type.orElse("BAN"), limit, offset);
        int total = Optional.ofNullable(jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class))
                .orElseThrow(() -> new RuntimeException("Could not fetch total count"));

        return new PageImpl<>(bans, pageable, total);
    }

    private RowMapper<Ban> banFromRow() {
        return (rs, rowNum) -> new Ban(
                rs.getInt("id"),
                UUID.fromString(rs.getString("player")),
                rs.getString("templateId"),
                rs.getString("bannedBy"),
                rs.getLong("bannedAt"),
                rs.getLong("expiresAt")
        );
    }

    private RowMapper<BanListEntry> listEntryFromRow() {
        return (rs, rowNum) -> new BanListEntry(
                rs.getInt("id"),
                UUID.fromString(rs.getString("player")),
                rs.getString("templateId"),
                rs.getString("bannedBy"),
                rs.getLong("bannedAt"),
                rs.getLong("expiresAt"),
                rs.getString("reason")
        );
    }
}
