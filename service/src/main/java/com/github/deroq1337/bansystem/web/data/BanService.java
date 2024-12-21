package com.github.deroq1337.bansystem.web.data;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.web.data.models.BanListEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BanService {

    void unbanUser(@NotNull Integer id);

    @NotNull Ban getBanById(@NotNull Integer id);

    @NotNull BanListEntry getBanAsListEntryById(@NotNull Integer id);

    @NotNull Page<Ban> listBans(Optional<String> type, Optional<Integer> page, Optional<Integer> pageSize);
}
