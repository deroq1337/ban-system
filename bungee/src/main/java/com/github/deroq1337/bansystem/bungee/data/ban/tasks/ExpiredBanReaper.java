package com.github.deroq1337.bansystem.bungee.data.ban.tasks;

import com.github.deroq1337.bansystem.bungee.BanSystemPlugin;
import com.github.deroq1337.bansystem.bungee.data.ban.repository.BanRepository;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ExpiredBanReaper implements Runnable {

    private final @NotNull BanRepository repository;
    private final @NotNull ScheduledTask task;

    public ExpiredBanReaper(@NotNull BanSystemPlugin plugin, @NotNull BanRepository repository) {
        this.repository = repository;
        this.task = ProxyServer.getInstance().getScheduler().schedule(plugin, this, 10, 10, TimeUnit.MINUTES);
    }

    public void cancel() {
        task.cancel();
    }

    @Override
    public void run() {
        repository.reapExpiredBans().thenAccept(reapedCount ->
                System.out.println("Reaped " + reapedCount + " bans"));
    }
}
