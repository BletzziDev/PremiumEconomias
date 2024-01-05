package com.premiumsetups.premiumeconomias.objects;

import com.premiumsetups.premiumeconomias.enums.SubCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor @Getter
public class Currency {
    private String key;
    private String displayName;
    private List<String> commands;
    private double defaultAmount;
    private HashMap<String, SubCommand> subCommands;
    private HashMap<String, String> usage;
    private HashMap<String, String> permissions;

}