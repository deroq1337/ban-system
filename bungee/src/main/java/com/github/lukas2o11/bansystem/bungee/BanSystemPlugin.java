package com.github.lukas2o11.bansystem.bungee;

import com.github.lukas2o11.bansystem.bungee.data.database.MySQL;
import com.github.lukas2o11.bansystem.bungee.data.ban.BanManager;
import com.github.lukas2o11.bansystem.bungee.data.ban.DefaultBanManager;
import com.github.lukas2o11.bansystem.bungee.data.messaging.RabbitMQ;
import com.github.lukas2o11.bansystem.bungee.data.prometheus.Prometheus;
import com.github.lukas2o11.bansystem.bungee.data.template.BanTemplateManager;
import com.github.lukas2o11.bansystem.bungee.data.template.DefaultBanTemplateManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

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
    }

    @Override
    public void onDisable() {
        prometheus.disconnect();
        rabbitMQ.disconnect();
        mySQL.disconnect();
    }
}
