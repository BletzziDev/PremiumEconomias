package com.premiumsetups.premiumeconomias.cache;

import com.premiumsetups.premiumeconomias.data.file.DataFile;
import com.premiumsetups.premiumeconomias.enums.SubCommand;
import com.premiumsetups.premiumeconomias.objects.Currency;
import com.premiumsetups.premiumeconomias.tools.Text;
import lombok.Getter;

import java.util.HashMap;

public class CurrenciesCache {
    @Getter
    private static HashMap<String, Currency> currencies = new HashMap<>();
    private static HashMap<String, Currency> commands = new HashMap<>();
    public static void loadFromFile(DataFile file) {
        currencies.clear();
        commands.clear();
        for(String key : file.getConfigurationSection("currencies").getKeys(false)) {
            HashMap<String, SubCommand> subCommands = new HashMap<>();
            HashMap<String, String> usage = new HashMap<>();
            HashMap<String, String> permissions = new HashMap<>();
            for(String subCommandKey : file.getConfigurationSection(String.format("currencies.%s.subCommands", key)).getKeys(false)) {
                SubCommand subCommand = SubCommand.valueOf(subCommandKey.toUpperCase());
                for(String option : file.getStringList(String.format("currencies.%s.subCommands.%s", key, subCommandKey))) {
                    subCommands.put(option, subCommand);
                }
            }
            for(String usageKey : file.getConfigurationSection(String.format("currencies.%s.usage", key)).getKeys(false)) {
                usage.put(usageKey, Text.colorTranslate(file.getString(String.format("currencies.%s.usage.%s", key, usageKey))));
            }
            for(String permissionKey : file.getConfigurationSection(String.format("currencies.%s.permissions", key)).getKeys(false)) {
                permissions.put(permissionKey, file.getString(String.format("currencies.%s.permissions.%s", key, permissionKey)));
            }

            Currency currency = new Currency(
                    key,
                    Text.colorTranslate(file.getString("currencies." + key + ".displayName")),
                    file.getStringList("currencies." + key + ".commands"),
                    file.getDouble("currencies." + key + ".defaultAmount"),
                    subCommands,
                    usage,
                    permissions
            );
            currencies.put(key, currency);
            for(String command : currency.getCommands()) {
                commands.put(command, currency);
            }
        }
    }
    public static boolean containsKey(String key) {
        return currencies.containsKey(key);
    }
    public static Currency getCurrencyByCommand(String command) {
        return commands.get(command);
    }
}