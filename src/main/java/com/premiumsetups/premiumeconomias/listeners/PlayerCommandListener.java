package com.premiumsetups.premiumeconomias.listeners;

import com.premiumsetups.premiumeconomias.PremiumEconomias;
import com.premiumsetups.premiumeconomias.cache.CurrenciesCache;
import com.premiumsetups.premiumeconomias.cache.PlayerDataCache;
import com.premiumsetups.premiumeconomias.data.file.DataFile;
import com.premiumsetups.premiumeconomias.enums.SubCommand;
import com.premiumsetups.premiumeconomias.objects.Currency;
import com.premiumsetups.premiumeconomias.objects.PlayerData;
import com.premiumsetups.premiumeconomias.tools.NumberFormat;
import com.premiumsetups.premiumeconomias.tools.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener implements Listener {
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String[] args = e.getMessage().replace("/", "").split(" ");
        Currency currency = CurrenciesCache.getCurrencyByCommand(args[0]);
        if(currency == null) return;

        e.setCancelled(true);
        Player player = e.getPlayer();
        PlayerData playerData = PlayerDataCache.getPlayerData(player);
        DataFile messages = PremiumEconomias.configFile;
        if(args.length == 1) {
            if(!player.hasPermission(currency.getPermissions().get("balance"))) {
                player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                return;
            }
            player.sendMessage(Text.colorTranslate(messages.getString("messages.balance")
                    .replace("{amount}", NumberFormat.format(playerData.getBalance().get(currency.getKey())))
                    .replace("{currency_name}", currency.getDisplayName())
            ));
            return;
        }

        // SubCommands
        SubCommand subCommand = currency.getSubCommands().get(args[1]);
        Player target = null;
        if(subCommand != null) {
            switch (subCommand) {
                case SET:
                    if(!player.hasPermission(currency.getPermissions().get("set"))) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                        return;
                    }

                    if(args.length != 4) {
                        player.sendMessage(currency.getUsage().get("set"));
                        return;
                    }

                    target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.offlinePlayer")));
                        return;
                    }

                    try {
                        double amount = Double.parseDouble(args[3]);
                        if(amount < 0) {
                            player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                            return;
                        }

                        PlayerData targetData = PlayerDataCache.getPlayerData(target);
                        targetData.setBalance(currency.getKey(), amount);
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.successSetBalance")
                                .replace("{player}", target.getName())
                                .replace("{amount}", NumberFormat.format(amount))
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        return;
                    }catch(Exception ignore) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                        return;
                    }
                case ADD:
                    if(!player.hasPermission(currency.getPermissions().get("add"))) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                        return;
                    }

                    if(args.length != 4) {
                        player.sendMessage(currency.getUsage().get("add"));
                        return;
                    }

                    target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.offlinePlayer")));
                        return;
                    }

                    try {
                        double amount = Double.parseDouble(args[3]);
                        if(amount < 0) {
                            player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                            return;
                        }
                        PlayerData targetData = PlayerDataCache.getPlayerData(target);
                        double targetBalance = targetData.getBalance().get(currency.getKey()) + amount;
                        targetData.setBalance(currency.getKey(), targetBalance);

                        player.sendMessage(Text.colorTranslate(messages.getString("messages.successAddBalance")
                                .replace("{player}", target.getName())
                                .replace("{amount}", NumberFormat.format(amount))
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        return;
                    }catch(Exception ignore) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                        return;
                    }
                case REMOVE:
                    if(!player.hasPermission(currency.getPermissions().get("remove"))) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                        return;
                    }

                    if(args.length != 4) {
                        player.sendMessage(currency.getUsage().get("remove"));
                        return;
                    }

                    target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.offlinePlayer")));
                        return;
                    }

                    try {
                        double amount = Double.parseDouble(args[3]);
                        if(amount < 0) {
                            player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                            return;
                        }
                        PlayerData targetData = PlayerDataCache.getPlayerData(target);
                        double targetBalance = targetData.getBalance().get(currency.getKey()) - amount;
                        targetBalance = targetBalance < 0 ? 0 : targetBalance;
                        targetData.setBalance(currency.getKey(), targetBalance);

                        player.sendMessage(Text.colorTranslate(messages.getString("messages.successRemoveBalance")
                                .replace("{player}", target.getName())
                                .replace("{amount}", NumberFormat.format(amount))
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        return;
                    }catch(Exception ignore) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                        return;
                    }
                case SEND:
                    if(!player.hasPermission(currency.getPermissions().get("send"))) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                        return;
                    }

                    if(args.length != 4) {
                        player.sendMessage(currency.getUsage().get("send"));
                        return;
                    }

                    target = Bukkit.getPlayerExact(args[2]);
                    if(target == null) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.offlinePlayer")));
                        return;
                    }

                    if(target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.yourselfSending")
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        return;
                    }

                    try {
                        double amount = Double.parseDouble(args[3]);
                        if(amount < 0) {
                            player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                            return;
                        }
                        double senderBalance = playerData.getBalance().get(currency.getKey());
                        if(senderBalance < amount) {
                            player.sendMessage(Text.colorTranslate(messages.getString("messages.insufficientAmount")
                                    .replace("{currency_name}", currency.getDisplayName())
                                    .replace("{amount}", NumberFormat.format(amount))
                            ));
                            return;
                        }

                        PlayerData targetData = PlayerDataCache.getPlayerData(target);
                        double targetBalance = targetData.getBalance().get(currency.getKey());
                        targetData.setBalance(currency.getKey(), targetBalance + amount);
                        playerData.setBalance(currency.getKey(), senderBalance - amount);
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.successSendBalance")
                                .replace("{player}", target.getName())
                                .replace("{amount}", NumberFormat.format(amount))
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        target.sendMessage(Text.colorTranslate(messages.getString("messages.receivedAmount")
                                .replace("{player}", player.getName())
                                .replace("{amount}", NumberFormat.format(amount))
                                .replace("{currency_name}", currency.getDisplayName())
                        ));
                        return;
                    }catch(Exception ignore) {
                        player.sendMessage(Text.colorTranslate(messages.getString("messages.invalidAmount")));
                        return;
                    }
            }
        }

        if(args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("ajuda")) {
            if(player.hasPermission("premiumeconomias.admin")) {
                for(String line : Text.colorTranslate(messages.getStringList("messages.currencyAdminHelp"))) {
                    player.sendMessage(line
                            .replace("{currency_name}", currency.getDisplayName())
                            .replace("{currency_command}", currency.getCommands().get(0))
                    );
                }
                return;
            }
            for(String line : Text.colorTranslate(messages.getStringList("messages.currencyHelp"))) {
                player.sendMessage(line
                        .replace("{currency_name}", currency.getDisplayName())
                        .replace("{currency_command}", currency.getCommands().get(0))
                );
            }
            return;
        }
        if(args[1].equalsIgnoreCase("reload")) {
            if(!player.hasPermission("premiumeconomias.reload")) {
                player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
                return;
            }
            PremiumEconomias.reloadPluginData();
            player.sendMessage(Text.colorTranslate(messages.getString("messages.successReload")));
            return;
        }

        // Other player balance
        if(!player.hasPermission(currency.getPermissions().get("others"))) {
            player.sendMessage(Text.colorTranslate(messages.getString("messages.noPermission")));
            return;
        }
        target = Bukkit.getPlayer(args[1]);
        if(target == null) {
            player.sendMessage(Text.colorTranslate(messages.getString("messages.offlinePlayer")));
            return;
        }
        PlayerData targetData = PlayerDataCache.getPlayerData(target);
        player.sendMessage(Text.colorTranslate(messages.getString("messages.balanceOthers")
                .replace("{player}", target.getName())
                .replace("{amount}", NumberFormat.format(targetData.getBalance().get(currency.getKey())))
                .replace("{currency_name}", currency.getDisplayName())
        ));
    }
}