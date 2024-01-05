package com.premiumsetups.premiumeconomias.data.database;

import com.premiumsetups.premiumeconomias.PremiumEconomias;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String HOST = PremiumEconomias.configFile.getString("database.host");
    private final String DATABASE = PremiumEconomias.configFile.getString("database.database");
    private final String USER = PremiumEconomias.configFile.getString("database.user");
    private final String PASSWORD = PremiumEconomias.configFile.getString("database.password");
    private final Connection CONNECTION;
    public Database() throws SQLException {
        CONNECTION = DriverManager.getConnection("jdbc:mysql://{host}/{database}?useSSL=false&AutoReconnect=true".replace("{host}", HOST).replace("{database}", DATABASE), USER, PASSWORD);
    }
    public boolean isConnected() {
        return CONNECTION != null;
    }
    public void closeConnection() {
        if(!isConnected()) return;
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            throw new RuntimeException("§cOcorreu um erro ao encerrar a conexão com o banco de dados");
        }
    }
    public Connection getConnection() {
        if(isConnected()) return CONNECTION;
        return null;
    }
}