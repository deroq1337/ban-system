package com.github.deroq1337.bansystem.web.data.repository;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.web.data.models.BanListEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BanRepository {

    Optional<Ban> findById(@NotNull Integer id);

    Optional<BanListEntry> findByIdAsListEntry(@NotNull Integer id);

    @NotNull Page<Ban> findAll(Optional<String> type, @NotNull Pageable pageable);
}
