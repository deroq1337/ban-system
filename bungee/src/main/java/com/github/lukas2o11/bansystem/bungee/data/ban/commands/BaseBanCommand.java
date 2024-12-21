package com.github.lukas2o11.bansystem.bungee.data.ban.commands;

import com.github.lukas2o11.bansystem.api.Ban;
import com.github.lukas2o11.bansystem.api.BanType;
import com.github.lukas2o11.bansystem.bungee.BanSystemPlugin;
import com.github.lukas2o11.bansystem.bungee.data.ban.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class BaseBanCommand extends Command {

    private @NotNull final BanSystemPlugin plugin;
    private @NotNull final BanType type;

    public BaseBanCommand(@NotNull String name, @NotNull BanSystemPlugin plugin, @NotNull BanType type) {
        super(name);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextComponent.fromLegacy("§c/ban <user> <template>"));
            return;
        }

        if (!sender.hasPermission("bansystem.ban")) {
            sender.sendMessage(TextComponent.fromLegacy("§cKeine Rechte!"));
            return;
        }

        String targetName = args[0];
        String templateId = args[1];
        if (targetName.isEmpty() || templateId.isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacy("§c/ban <user> <template>"));
            return;
        }

        if (sender.getName().equalsIgnoreCase(targetName)) {
            sender.sendMessage(TextComponent.fromLegacy("§cDu kannst dich nicht selber bestrafen"));
            return;
        }

        UUIDFetcher.getUuid(targetName).thenCompose(optionalUuid -> {
            if (optionalUuid.isEmpty()) {
                sender.sendMessage(TextComponent.fromLegacy("§cUUID konnte nicht gefetched werden"));
                return CompletableFuture.completedFuture(null);
            }

            UUID targetUuid = optionalUuid.get();
            if (sender instanceof ProxiedPlayer
                    && ((ProxiedPlayer) sender).getUniqueId().equals(targetUuid)) {
                sender.sendMessage(TextComponent.fromLegacy("§cDu kannst dich nicht selber bestrafen"));
                return CompletableFuture.completedFuture(null);
            }

            return plugin.getBanManager().isUserBanned(targetUuid, type).thenCompose(banned -> {
                if (banned) {
                    sender.sendMessage(TextComponent.fromLegacy("§cDieser Spieler wurde bereits bestraft"));
                    return CompletableFuture.completedFuture(null);
                }

                return plugin.getTemplateManager().getTemplate(templateId).thenCompose(optionalTemplate -> {
                    if (optionalTemplate.isEmpty()) {
                        sender.sendMessage(TextComponent.fromLegacy("§cTemplate nicht gefunden"));
                        return CompletableFuture.completedFuture(null);
                    }

                    if (optionalTemplate.get().getBanType() != type) {
                        sender.sendMessage(TextComponent.fromLegacy("§cTemplate kann nicht genutzt werden"));
                        return CompletableFuture.completedFuture(null);
                    }


                    long now = System.currentTimeMillis();
                    Ban ban = new Ban(targetUuid, templateId, getBanner(sender), now, now + optionalTemplate.get().getDuration());

                    return plugin.getBanManager().banUser(ban, type).thenApply(acknowledged -> {
                        if (!acknowledged) {
                            sender.sendMessage(TextComponent.fromLegacy("§cSpieler konnte nicht gebannt werden. Versuche es erneut oder kontaktiere einen Administrator"));
                            return null;
                        }

                        sender.sendMessage(TextComponent.fromLegacy("§aBann wurde erstellt"));
                        return null;
                    });
                });
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            sender.sendMessage(TextComponent.fromLegacy("§cEin unerwarteter Fehler ist aufgetreten. Versuche es erneut oder kontaktiere einen Administrator."));
            return null;
        });
    }

    private @NotNull String getBanner(@NotNull CommandSender sender) {
        return sender instanceof ProxiedPlayer
                ? ((ProxiedPlayer) sender).getUniqueId().toString()
                : sender.getName();
    }
}
