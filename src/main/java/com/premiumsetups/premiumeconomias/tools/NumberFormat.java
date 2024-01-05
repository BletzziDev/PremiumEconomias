package com.premiumsetups.premiumeconomias.tools;

import com.premiumsetups.premiumeconomias.PremiumEconomias;

import java.text.DecimalFormat;
import java.util.List;

public class NumberFormat {
    private static List<String> suffixes = PremiumEconomias.configFile.getStringList("formats");


    public static String format(double number) {
        int suffixIndex = 0;
        while (number >= 1_000 && suffixIndex < suffixes.size() - 1) {
            number /= 1_000;
            suffixIndex++;
        }

        return formatNumber(number) + suffixes.get(suffixIndex);
    }

    private static String formatNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return decimalFormat.format(number);
    }
}