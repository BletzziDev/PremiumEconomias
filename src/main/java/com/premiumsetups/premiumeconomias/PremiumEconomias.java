package com.premiumsetups.premiumeconomias;

import com.premiumsetups.premiumeconomias.cache.CurrenciesCache;
import com.premiumsetups.premiumeconomias.cache.PlayerDataCache;
import com.premiumsetups.premiumeconomias.data.database.DAO;
import com.premiumsetups.premiumeconomias.data.database.Database;
import com.premiumsetups.premiumeconomias.data.file.DataFile;
import com.premiumsetups.premiumeconomias.hooks.PlaceHolderExpansion;
import com.premiumsetups.premiumeconomias.listeners.PlayerCommandListener;
import com.premiumsetups.premiumeconomias.listeners.PlayerConnectionListener;
import com.premiumsetups.premiumeconomias.objects.Currency;
import com.premiumsetups.premiumeconomias.objects.PlayerData;
import com.premiumsetups.premiumeconomias.tasks.AutoSaveTask;
import com.premiumsetups.premiumeconomias.tools.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class PremiumEconomias extends JavaPlugin {
    public static PremiumEconomias instance;
    public static ConsoleLogger logger = new ConsoleLogger("&b[PremiumEconomias]");
    public static DataFile configFile;
    public static DataFile currenciesFile;
    public static Database database;
    public static DAO dao;
    @Override
    public void onEnable() {
        instance = this;
        log("&eInicializando plugin...");

        log("&eCarregando arquivos...");
        try {
            configFile = new DataFile("config.yml", this);
            currenciesFile = new DataFile("economias.yml", this);
        } catch (IOException | InvalidConfigurationException e) {
            log("&cNão foi possível carregar os arquivos! Desabilitando plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }
        log("&bArquivos carregados com sucesso!");

        log("&eSe conectando ao banco de dados...");
        try {
            database = new Database();
            dao = new DAO(database.getConnection());
            dao.setup();
        } catch (SQLException e) {
            log("&cNão foi possível se conectar ao banco de dados, Desabilitando plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }
        log("&bBanco de dados conectado com sucesso!");

        log("&eCarregando economias...");
        CurrenciesCache.loadFromFile(currenciesFile);
        log(String.format("&bForam carregados %s economias.", String.valueOf(CurrenciesCache.getCurrencies().size())));

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandListener(), this);

        long autoSaveTaskDelay = configFile.getLong("autoSaveTaskDelay");
        new AutoSaveTask().runTaskTimerAsynchronously(this, (autoSaveTaskDelay * 20) * 60, (autoSaveTaskDelay * 20) * 60);

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null) {
            new PlaceHolderExpansion().register();
            log("&bHook realizado com o PlaceHolderAPI!");
        }

        log("&aPlugin inicializado com sucesso!");
    }

    @Override
    public void onDisable() {
        log("&eDesabilitando plugin...");

        for(Player player : Bukkit.getOnlinePlayers()) {
            PremiumEconomias.dao.savePlayerData(PlayerDataCache.getPlayerData(player));
        }
        PremiumEconomias.log(String.format("&bForam salvos os dados de %s jogadores.", String.valueOf(Bukkit.getOnlinePlayers().size())));
        if(database != null) database.closeConnection();

        log("&cPlugin desabilitado.");
    }
    public static void log(String message) {
        logger.log(message);
    }

    public static void reloadPluginData() {
        try {
            configFile = new DataFile("config.yml", instance);
            currenciesFile = new DataFile("economias.yml", instance);
        } catch (IOException | InvalidConfigurationException e) {
            log("&cNão foi possível carregar os arquivos! Desabilitando plugin.");
            instance.getPluginLoader().disablePlugin(instance);
            return;
        }
        CurrenciesCache.loadFromFile(currenciesFile);
        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerDataCache.getPlayerData(player);
            for(Currency currency : CurrenciesCache.getCurrencies().values()) {
                String key = currency.getKey();
                if(playerData.getBalance().containsKey(key)) continue;
                playerData.getBalance().put(key, currency.getDefaultAmount());
            }
        }
        log(String.format("&bForam carregados %s economias.", String.valueOf(CurrenciesCache.getCurrencies().size())));
    }
}