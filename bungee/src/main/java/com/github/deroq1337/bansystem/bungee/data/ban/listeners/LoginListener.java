package com.github.deroq1337.bansystem.bungee.data.ban.listeners;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.BanTemplateNotFoundException;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanScreen;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class LoginListener implements Listener {

    private final @NotNull BanSystemPlugin plugin;

    @EventHandler
    public void onLogin(LoginEvent event) {
        plugin.getBanManager().getBanByPlayer(event.getConnection().getUniqueId(), BanType.BAN).thenCompose(optionalBan -> {
            return optionalBan.map(ban -> {
                return plugin.getTemplateManager().getTemplateById(ban.templateId()).thenAccept(optionalTemplate -> {
                    optionalTemplate.ifPresentOrElse(template -> {
                        event.setCancelled(true);
                        event.setReason(new BanScreen(ban, template).build());
                    }, () -> {
                        throw new BanTemplateNotFoundException("Error during login: BanTemplate with id '" + ban.id() + "' was not found");
                    });
                });
            }).orElseGet(() -> CompletableFuture.completedFuture(null));
        });
    }
}
