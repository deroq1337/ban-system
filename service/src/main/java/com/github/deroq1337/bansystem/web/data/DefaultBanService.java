package com.github.deroq1337.bansystem.web.data;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.web.data.messaging.publisher.UnbanMessagePublisher;
import com.github.deroq1337.bansystem.web.data.models.BanListEntry;
import com.github.deroq1337.bansystem.web.data.repository.BanRepository;
import com.github.deroq1337.bansystem.web.data.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Optional;

@Service
@ApplicationScope
public class DefaultBanService implements BanService {

    private @NotNull final BanRepository repository;
    private @NotNull final UnbanMessagePublisher publisher;

    @Autowired
    public DefaultBanService(@NotNull BanRepository repository, @NotNull UnbanMessagePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public void unbanUser(@NotNull Integer id) {
        publisher.sendMessage(String.valueOf(id));
    }

    @Override
    public @NotNull Ban getBanById(@NotNull Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ban with id '" + id + "' was not found"));
    }

    @Override
    public @NotNull BanListEntry getBanAsListEntryById(@NotNull Integer id) {
        return repository.findByIdAsListEntry(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ban with id '" + id + "' was not found"));
    }

    @Override
    public @NotNull Page<Ban> listBans(Optional<String> type, Optional<Integer> page, Optional<Integer> pageSize) {
        return repository.findAll(type, createPageable(page, pageSize));
    }

    private @NotNull Pageable createPageable(Optional<Integer> page, Optional<Integer> pageSize) {
        return PageRequest.of(page.orElse(0), pageSize.orElse(25));
    }
}
