package com.github.deroq1337.bansystem.web.data.controller;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.web.data.BanService;
import com.github.deroq1337.bansystem.web.data.models.BanListEntry;
import com.github.deroq1337.bansystem.web.data.models.UnbanRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(
        path = "/api/v1/bans",
        produces = "application/json"
)
public class BanController {

    private @NotNull final BanService service;

    @Autowired
    public BanController(@NotNull BanService service) {
        this.service = service;
    }

    @PostMapping(
            path = "/unban",
            consumes = "application/json"
    )
    public ResponseEntity<Void> unbanUser(@RequestBody @NotNull @Valid UnbanRequest request) {
        service.unbanUser(request.getId());
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Ban> getBanById(@PathVariable("id") @NotNull Integer id) {
        return ResponseEntity.ok(service.getBanById(id));
    }

    @GetMapping(path = "/{id}/entry")
    public ResponseEntity<BanListEntry> getBanAsListEntryById(@PathVariable("id") @NotNull Integer id) {
        return ResponseEntity.ok(service.getBanAsListEntryById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Ban>> listBans(@RequestParam("type") Optional<String> type, @RequestParam("page") Optional<Integer> page,
                                              @RequestParam("pageSize") Optional<Integer> pageSize) {
        return ResponseEntity.ok(service.listBans(type, page, pageSize));
    }
}
