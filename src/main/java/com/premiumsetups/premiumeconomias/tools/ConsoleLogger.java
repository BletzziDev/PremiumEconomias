package com.premiumsetups.premiumeconomias.tools;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@AllArgsConstructor
public class ConsoleLogger {
    public final String prefix;
    public void log(String message){
        Bukkit.getConsoleSender().sendMessage(Text.colorTranslate(String.format("%s &f%s", prefix, message)));
    }
}