package com.premiumsetups.premiumeconomias.cache;

import com.premiumsetups.premiumeconomias.objects.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerDataCache {
    @Getter
    private static HashMap<String, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        return players.get(player.getName());
    }
    public static void setPlayerData(PlayerData playerData) {
        players.put(playerData.getPlayer().getName(), playerData);
    }
    public static void removePlayerData(Player player) {
        players.remove(player.getName());
    }
}
