package com.premiumsetups.premiumeconomias.hooks;

import com.premiumsetups.premiumeconomias.PremiumEconomias;
import com.premiumsetups.premiumeconomias.cache.CurrenciesCache;
import com.premiumsetups.premiumeconomias.cache.PlayerDataCache;
import com.premiumsetups.premiumeconomias.objects.Currency;
import com.premiumsetups.premiumeconomias.objects.PlayerData;
import com.premiumsetups.premiumeconomias.tools.NumberFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceHolderExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "premiumeconomias";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Bletzzi";
    }

    @Override
    public @NotNull String getVersion() {
        return PremiumEconomias.instance.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        for(String currencyKey : CurrenciesCache.getCurrencies().keySet()) {
            if(params.equalsIgnoreCase(currencyKey)) {
                PlayerData playerData = PlayerDataCache.getPlayerData(player);
                return NumberFormat.format(playerData.getBalance().get(currencyKey));
            }
            if(params.equalsIgnoreCase(currencyKey+"_raw")) {
                PlayerData playerData = PlayerDataCache.getPlayerData(player);
                return NumberFormat.format(playerData.getBalance().get(currencyKey));
            }
        }

        return null;
    }
}
