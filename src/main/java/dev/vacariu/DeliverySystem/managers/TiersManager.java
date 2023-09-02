package dev.vacariu.DeliverySystem.managers;

import com.google.j2objc.annotations.Property;
import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.internalutils.CustomConfig;
import dev.vacariu.DeliverySystem.internalutils.RomanNumber;
import dev.vacariu.DeliverySystem.internalutils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TiersManager {
    private final Main pl;

    public CustomConfig generatorConfig;
    public TiersManager(Main pl) {
        this.pl = pl;
        generatorConfig = new CustomConfig("configs","tiers",pl);
        loadData();
    }

    public static List<Material> material = new ArrayList<>();
    public static List<String> texture = new ArrayList<>();
    public static List<Integer> slot = new ArrayList<>();
    public static List<Integer> reward = new ArrayList<>();
    public static List<Integer> tier = new ArrayList<>();
    public static List<String> name = new ArrayList<>();
    public static List<List<String>> lore = new ArrayList<>();


    public ItemStack getDeliveryItem(int tier) {
        ItemStack i = new ItemStack(material.get(tier-1),1);
        if (material.get(tier-1).equals(Material.PLAYER_HEAD)) {
            UUID hashAsId = new UUID(texture.get(tier-1).hashCode(), texture.get(tier-1).hashCode());
            Bukkit.getUnsafe().modifyItemStack(i, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\""
                    + texture.get(tier-1) + "\"}]}}}");
        }
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(Utils.asColor(name.get(tier-1).replaceAll("%tier%", RomanNumber.toRoman(tier) +"").replaceAll("%reward%",reward.get(tier-1)+"")));
        List<String> il = new ArrayList<>();
        lore.get(tier-1).forEach(s->{
            il.add(Utils.asColor(s.replaceAll("%tier%",RomanNumber.toRoman(tier)+"").replaceAll("%reward%",reward.get(tier-1)+"")));
        });
        im.setLore(il);
        im.getPersistentDataContainer().set(pl.deliveryKey, PersistentDataType.INTEGER,tier);
        i.setItemMeta(im);
        return i;
    }

    private void loadData(){
        FileConfiguration cfg = generatorConfig.getConfig();

        if (cfg.contains("DeliveryType")){
            for(String id : cfg.getConfigurationSection("DeliveryType").getKeys(false)) {
                try{
                    Material mat = Material.valueOf(cfg.getString("DeliveryType." + id + ".type"));
                    material.add(mat);
                    if (mat.equals(Material.PLAYER_HEAD))
                        texture.add(cfg.getString("DeliveryType." + id + ".texture"));
                }catch (Exception ex){
                    Bukkit.getConsoleSender().sendMessage(Utils.asColor("&c[DeliverySystem] Wrong block type, defaulting to PLAYER_HEAD"));
                    material.add(Material.PLAYER_HEAD);
                    texture.add("");
                }
                name.add(Utils.translateHexColorCodes("&#","",cfg.getString("DeliveryType." + id + ".name")));
                lore.add(cfg.getStringList("DeliveryType." + id + ".lore"));
                tier.add(Integer.valueOf(id));
                slot.add(cfg.getInt("DeliveryType." + id + ".slot"));
                reward.add(cfg.getInt("DeliveryType." + id + ".reward"));
            }
        }else{
            loadDefData();
            loadData();
        }

    }


    private void loadDefData(){
        FileConfiguration cfg = generatorConfig.getConfig();
        cfg.set("DeliveryType.1.type","PLAYER_HEAD");
        cfg.set("DeliveryType.1.texture","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThjZWQ3NGEyMjAyMWE1MzVmNmJjZTIxYzhjNjMyYjI3M2RjMmQ5NTUyYjcxYTM4ZDU3MjY5YjM1MzhjZiJ9fX0=");
        cfg.set("DeliveryType.1.slot", 13);
        cfg.set("DeliveryType.1.reward", 1000);
        cfg.set("DeliveryType.1.name","&fDelivery &7[%tier%]");
        cfg.set("DeliveryType.1.lore", Arrays.asList("&7Deliver me to earn &6%reward% coins!"));
        generatorConfig.save();
    }
}
