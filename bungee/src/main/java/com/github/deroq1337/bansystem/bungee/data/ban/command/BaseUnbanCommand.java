package com.github.deroq1337.bansystem.bungee.data.ban.command;

import com.github.deroq1337.bansystem.api.BanType;
import com.github.deroq1337.bansystem.api.Unban;
import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.notify.UnbanNotify;
import com.github.deroq1337.bansystem.bungee.data.ban.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class BaseUnbanCommand extends Command {

    private final @NotNull BanSystemPlugin plugin;
    private final @NotNull BanType type;

    public BaseUnbanCommand(@NotNull String name, @NotNull BanSystemPlugin plugin, @NotNull BanType type) {
        super(name);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(TextComponent.fromLegacy("§c/" + getName() + " <user>"));
            return;
        }

        if (!sender.hasPermission("bansystem.unban")) {
            sender.sendMessage(TextComponent.fromLegacy("§cKeine Rechte!"));
            return;
        }

        String targetName = args[0];
        if (targetName.isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacy("§c/" + getName() + " <user>"));
            return;
        }

        if (sender.getName().equalsIgnoreCase(targetName)) {
            sender.sendMessage(TextComponent.fromLegacy("§cDu kannst nicht mit dir selber interagieren"));
            return;
        }

        UUIDFetcher.getUuid(targetName).thenCompose(optionalUuid -> {
            return optionalUuid.map(targetUuid -> {
                if (sender instanceof ProxiedPlayer
                        && ((ProxiedPlayer) sender).getUniqueId().equals(targetUuid)) {
                    sender.sendMessage(TextComponent.fromLegacy("§cDu kannst nicht mit dir selber interagieren"));
                    return CompletableFuture.completedFuture(null);
                }

                return plugin.getBanManager().isUserBanned(targetUuid, type).thenCompose(banned -> {
                    if (!banned) {
                        sender.sendMessage(TextComponent.fromLegacy("§cFür diesen Spieler liegt aktuell keine Strafe vor"));
                        return CompletableFuture.completedFuture(null);
                    }

                    return plugin.getBanManager().getBanByPlayerAndType(targetUuid, type).thenCompose(optionalBan -> {
                        return optionalBan.map(ban -> {
                            Unban unban = new Unban(targetUuid, ban.id(), getUnbannedBy(sender), System.currentTimeMillis());

                            return plugin.getBanManager().unbanUser(unban).thenAccept(acknowledged -> {
                                if (!acknowledged) {
                                    sender.sendMessage(TextComponent.fromLegacy("§cStrafe konnte nicht aufgehoben werden. Versuche es erneut oder kontaktiere einen Administrator"));
                                    return;
                                }

                                new UnbanNotify(unban).broadcast();
                                sender.sendMessage(TextComponent.fromLegacy("§aStrafe wurde aufgehoben"));
                            });
                        }).orElseGet(() -> {
                            sender.sendMessage(TextComponent.fromLegacy("§cEs konnte keine Strafe für diesen Spieler gefunden werden"));
                            return CompletableFuture.completedFuture(null);
                        });
                    });
                });
            }).orElseGet(() -> {
                sender.sendMessage(TextComponent.fromLegacy("§cUUID konnte nicht gefetched werden"));
                return CompletableFuture.completedFuture(null);
            });
        }).exceptionally(t -> {
            t.printStackTrace();
            sender.sendMessage(TextComponent.fromLegacy("§cEin unerwarteter Fehler ist aufgetreten. Versuche es erneut oder kontaktiere einen Administrator."));
            return null;
        });
    }

    private @NotNull String getUnbannedBy(@NotNull CommandSender sender) {
        return sender instanceof ProxiedPlayer
                ? ((ProxiedPlayer) sender).getUniqueId().toString()
                : sender.getName();
    }
}
