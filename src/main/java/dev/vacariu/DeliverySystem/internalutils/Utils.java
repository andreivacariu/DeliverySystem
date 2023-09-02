package dev.vacariu.DeliverySystem.internalutils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class Utils {
    public static String asColor(String toTranslate){
        return ChatColor.translateAlternateColorCodes('&',toTranslate);
    }
    public static List<String> listAsColor(List<String> toTranslate){
        List<String> toReturn = new ArrayList<>();
        for(String s : toTranslate){
            toReturn.add(asColor(s));
        }
        return toReturn;
    }
    public static String hexTranslate(String string) {
        Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");
        Matcher match = HEX_PATTERN.matcher(string);
        while (match.find()) {
            String color = string.substring(match.start(), match.end());
            string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = HEX_PATTERN.matcher(string);
        }
        return string;
    }

    public static String translateHexColorCodes(String startTag, String endTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

}
