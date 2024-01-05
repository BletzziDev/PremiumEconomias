package com.premiumsetups.premiumeconomias.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

@AllArgsConstructor @Getter
public class PlayerData {
    private Player player;
    private HashMap<String, Double> balance;
    public void addBalance(String currency, double amount) {
        if(amount < 1) return;
        amount = balance.get(currency) + amount;
        balance.put(currency, amount);
    }
    public void removeBalance(String currency, double amount) {
        if(amount < 1) return;
        amount = balance.get(currency) - amount;
        if(amount < 1) return;
        balance.put(currency, balance.get(currency) - amount);
    }
    public void setBalance(String currency, double amount) {
        if(amount < 1) amount = 0;
        balance.put(currency, amount);
    }
    public String getBalanceAsJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(balance, HashMap.class);
    }
}