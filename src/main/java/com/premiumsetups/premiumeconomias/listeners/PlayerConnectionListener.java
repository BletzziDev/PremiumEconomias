package com.premiumsetups.premiumeconomias.listeners;

import com.premiumsetups.premiumeconomias.PremiumEconomias;
import com.premiumsetups.premiumeconomias.cache.PlayerDataCache;
import com.premiumsetups.premiumeconomias.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PremiumEconomias.dao.setupPlayerData(player);
        PlayerData playerData = PremiumEconomias.dao.getPlayerData(player);
        PlayerDataCache.setPlayerData(playerData);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerData playerData = PlayerDataCache.getPlayerData(player);
        PremiumEconomias.dao.savePlayerData(playerData);
        PlayerDataCache.removePlayerData(player);
    }
}