package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.Ban;
import com.github.deroq1337.bansystem.api.BanTemplate;
import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.notify.BanNotify;
import com.github.deroq1337.bansystem.bungee.data.ban.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class BaseBanCommand extends Command {

    private final @NotNull BanSystemPlugin plugin;
    private final @NotNull BanType type;

    public BaseBanCommand(@NotNull String name, @NotNull BanSystemPlugin plugin, @NotNull BanType type) {
        super(name);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextComponent.fromLegacy("§c/" + getName() + " <user> <template>"));
            return;
        }

        if (!sender.hasPermission("bansystem.ban")) {
            sender.sendMessage(TextComponent.fromLegacy("§cKeine Rechte!"));
            return;
        }

        String targetName = args[0];
        String templateId = args[1];
        if (targetName.isEmpty() || templateId.isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacy("§c/" + getName() + " <user> <template>"));
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

                return plugin.getTemplateManager().getTemplateById(templateId).thenCompose(optionalTemplate -> {
                    if (optionalTemplate.isEmpty()) {
                        sender.sendMessage(TextComponent.fromLegacy("§cTemplate nicht gefunden"));
                        return CompletableFuture.completedFuture(null);
                    }

                    BanTemplate template = optionalTemplate.get();
                    if (template.type() != type) {
                        sender.sendMessage(TextComponent.fromLegacy("§cTemplate kann nicht genutzt werden"));
                        return CompletableFuture.completedFuture(null);
                    }

                    long now = System.currentTimeMillis();
                    Ban ban = new Ban(-1, targetUuid, templateId, getBannedBy(sender), now, now + template.duration());

                    return plugin.getBanManager().banUser(ban, type).thenApply(acknowledged -> {
                        if (!acknowledged) {
                            sender.sendMessage(TextComponent.fromLegacy("§cStrafe konnte nicht erstellt werden. Versuche es erneut oder kontaktiere einen Administrator"));
                            return null;
                        }

                        onSuccess(targetUuid, ban, template);
                        new BanNotify(ban).broadcast();

                        sender.sendMessage(TextComponent.fromLegacy("§aStrafe wurde erstellt"));
                        return null;
                    });
                });
            });
        }).exceptionally(t -> {
            t.printStackTrace();
            sender.sendMessage(TextComponent.fromLegacy("§cEin unerwarteter Fehler ist aufgetreten. Versuche es erneut oder kontaktiere einen Administrator."));
            return null;
        });
    }

    public abstract void onSuccess(@NotNull UUID targetUuid, @NotNull Ban ban, @NotNull BanTemplate template);

    private @NotNull String getBannedBy(@NotNull CommandSender sender) {
        return sender instanceof ProxiedPlayer
                ? ((ProxiedPlayer) sender).getUniqueId().toString()
                : sender.getName();
    }
}
