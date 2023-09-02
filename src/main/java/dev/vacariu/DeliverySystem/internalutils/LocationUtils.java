package dev.vacariu.DeliverySystem.internalutils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static String locationToString(Location location){
        return location.getWorld().getName()+"|"+location.getBlockX()+"|"+location.getBlockY()+"|"+location.getBlockZ();
    }
    public static String locationPreciseToString(Location location){
        return location.getWorld().getName()+"|"+location.getX()+"|"+location.getY()+"|"+ location.getZ()+"|"+location.getYaw()+"|"+location.getPitch();
    }
    public static Location stringToLocation(String toTranslate){
        String[] raw = toTranslate.split("\\|");
        return new Location(Bukkit.getWorld(raw[0]),Integer.parseInt(raw[1]),Integer.parseInt(raw[2]),Integer.parseInt(raw[3]));
    }
    public static Location stringToLocationPrecise(String toTranslate){
        String[] raw = toTranslate.split("\\|");
        return new Location(Bukkit.getWorld(raw[0]),Double.parseDouble(raw[1]),Double.parseDouble(raw[2]),Double.parseDouble(raw[3]),Float.parseFloat(raw[4]),Float.parseFloat(raw[5]));
    }

    public static ArrayList<Block> getSphere(final Location center, final int radius) {
        ArrayList<Block> sphere = new ArrayList<Block>();
        for (int Y = -radius; Y < radius; Y++)
            for (int X = -radius; X < radius; X++)
                for (int Z = -radius; Z < radius; Z++)
                    if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                        final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
                        sphere.add(block);
                    }
        return sphere;
    }
}
