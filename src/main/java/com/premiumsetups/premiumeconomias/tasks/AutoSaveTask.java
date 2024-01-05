package com.premiumsetups.premiumeconomias.tasks;

import com.premiumsetups.premiumeconomias.PremiumEconomias;
import com.premiumsetups.premiumeconomias.cache.PlayerDataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            PremiumEconomias.dao.savePlayerData(PlayerDataCache.getPlayerData(player));
        }
        PremiumEconomias.log(String.format("&bForam salvos os dados de %s jogadores.", String.valueOf(Bukkit.getOnlinePlayers().size())));
    }
}