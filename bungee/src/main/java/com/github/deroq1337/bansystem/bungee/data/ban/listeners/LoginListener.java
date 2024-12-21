package com.github.deroq1337.bansystem.bungee.data.ban.listeners;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.BanTemplateNotFoundException;
import com.github.deroq1337.bansystem.bungee.data.ban.models.BanScreen;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LoginListener implements Listener {

    private @NotNull final BanSystemPlugin plugin;

    @EventHandler
    public void onLogin(LoginEvent event) {
        plugin.getBanManager().getBanByPlayer(event.getConnection().getUniqueId(), BanType.BAN).thenAccept(optionalBan -> {
            if (optionalBan.isEmpty()) {
                return;
            }

            Ban ban = optionalBan.get();
            plugin.getTemplateManager().getTemplateById(ban.getTemplateId()).thenAccept(template -> {
                if (template.isEmpty()) {
                    throw new BanTemplateNotFoundException("Error during login: banTemplate with id '" + ban.getId() + "' was not found");
                }

                event.setCancelled(true);
                event.setReason(new BanScreen(ban, template.get()).build());
            });
        });
    }
}
