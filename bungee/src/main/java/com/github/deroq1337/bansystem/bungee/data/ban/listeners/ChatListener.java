package com.github.deroq1337.bansystem.bungee.data.ban.listeners;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.exceptions.BanTemplateNotFoundException;
import com.github.deroq1337.bansystem.bungee.data.ban.models.MuteMessage;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final @NotNull BanSystemPlugin plugin;

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }

        plugin.getBanManager().getBanByPlayer(player.getUniqueId(), BanType.MUTE).thenAccept(optionalBan -> {
            if (optionalBan.isEmpty()) {
                return;
            }

            Ban ban = optionalBan.get();
            plugin.getTemplateManager().getTemplateById(ban.templateId()).thenAccept(template -> {
                if (template.isEmpty()) {
                    throw new BanTemplateNotFoundException("Error during login: BanTemplate with id '" + ban.id() + "' was not found");
                }

                event.setCancelled(true);
                player.sendMessage(new MuteMessage(ban, template.get()).build());
            });
        });
    }
}