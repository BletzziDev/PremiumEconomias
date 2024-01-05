package com.premiumsetups.premiumeconomias.data.database;

import com.google.gson.Gson;
import com.premiumsetups.premiumeconomias.cache.CurrenciesCache;
import com.premiumsetups.premiumeconomias.objects.Currency;
import com.premiumsetups.premiumeconomias.objects.PlayerData;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DAO {
    private Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }
    public void setup() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `premiumeconomias.players` (`key` VARCHAR(16) NOT NULL , `balance` TEXT NOT NULL , PRIMARY KEY (`key`)) ENGINE = InnoDB;");
        stmt.execute();
    }
    public void setupPlayerData(Player player) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT IGNORE INTO `premiumeconomias.players` (`key`, `balance`) VALUES (?, ?);");
            stmt.setString(1, player.getName());
            stmt.setString(2, "{}");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public PlayerData getPlayerData(Player player) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `premiumeconomias.players` WHERE `key` = ?;");
            stmt.setString(1, player.getName());
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();
            HashMap<String, Double> balance = new Gson().fromJson(rs.getString("balance"), HashMap.class);
            for(Currency currency : CurrenciesCache.getCurrencies().values()) {
                String key = currency.getKey();
                if(!balance.containsKey(key)) balance.put(key, currency.getDefaultAmount());
            }
            return new PlayerData(player, balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void savePlayerData(PlayerData playerData) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE `premiumeconomias.players` SET `balance` = ? WHERE `key` = ?;");
            stmt.setString(1, playerData.getBalanceAsJson());
            stmt.setString(2, playerData.getPlayer().getName());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}