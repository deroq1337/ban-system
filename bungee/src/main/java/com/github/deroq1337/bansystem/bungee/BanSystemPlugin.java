package com.github.deroq1337.bansystem.bungee;

import com.github.deroq1337.bansystem.bungee.data.ban.commands.BanCommand;
import com.github.deroq1337.bansystem.bungee.data.ban.commands.MuteCommand;
import com.github.deroq1337.bansystem.bungee.data.ban.commands.UnbanCommand;
import com.github.deroq1337.bansystem.bungee.data.ban.commands.UnmuteCommand;
import com.github.deroq1337.bansystem.bungee.data.ban.listeners.LoginListener;
import com.github.deroq1337.bansystem.bungee.data.database.MySQL;
import com.github.deroq1337.bansystem.bungee.data.ban.BanManager;
import com.github.deroq1337.bansystem.bungee.data.ban.DefaultBanManager;
import com.github.deroq1337.bansystem.bungee.data.messaging.RabbitMQ;
import com.github.deroq1337.bansystem.bungee.data.prometheus.Prometheus;
import com.github.deroq1337.bansystem.bungee.data.template.BanTemplateManager;
import com.github.deroq1337.bansystem.bungee.data.template.DefaultBanTemplateManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
public class BanSystemPlugin extends Plugin {

    private MySQL mySQL;
    private BanManager banManager;
    private BanTemplateManager templateManager;
    private RabbitMQ rabbitMQ;
    private Prometheus prometheus;

    @Override
    public void onEnable() {
        this.mySQL = new MySQL();
        mySQL.connect();

        this.templateManager = new DefaultBanTemplateManager(this);
        this.banManager = new DefaultBanManager(this);

        this.rabbitMQ = new RabbitMQ(this);
        rabbitMQ.connect();

        this.prometheus = new Prometheus();
        prometheus.connect();

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new BanCommand(this));
        pluginManager.registerCommand(this, new MuteCommand(this));
        pluginManager.registerCommand(this, new UnbanCommand(this));
        pluginManager.registerCommand(this, new UnmuteCommand(this));

        pluginManager.registerListener(this, new LoginListener(this));
    }

    @Override
    public void onDisable() {
        banManager.stopExpiredBanReaper();
        prometheus.disconnect();
        rabbitMQ.disconnect();
        mySQL.disconnect();
    }
}
