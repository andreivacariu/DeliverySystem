package dev.vacariu.DeliverySystem.managers;

import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.components.Delivery;
import dev.vacariu.DeliverySystem.internalutils.InventoryUtils;
import dev.vacariu.DeliverySystem.internalutils.LocationUtils;
import dev.vacariu.DeliverySystem.internalutils.RomanNumber;
import dev.vacariu.DeliverySystem.internalutils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DeliveryManager {
    private final Main pl;
    public DeliveryManager(Main pl) {
        this.pl = pl;
    }

    public static final Map<String, Delivery> Deliveries = new HashMap<>();
    public void newDelivery(NPC npc, int tier, Player player){
        String loc = LocationUtils.locationToString(npc.getStoredLocation());
        Deliveries.put(player.getName(), new Delivery(pl, loc, tier, npc.getUniqueId().toString(), player.getName()));
        InventoryUtils.addItem(player, pl.tiersManager.getDeliveryItem(tier));
    }
    public void finishDelivery(Player player) {
        Delivery delivery = getDelivery(player);
        Deliveries.remove(player.getName());
        player.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.finishedDelivery").replaceAll("%tier%", RomanNumber.toRoman(delivery.tier) +"").replaceAll("%coins%", String.valueOf(pl.tiersManager.reward.get(delivery.tier-1))))));
        ItemStack[] inventoryContents = player.getInventory().getContents();
        for (ItemStack item : inventoryContents) {
            if (item != null && item.getType() == pl.tiersManager.material.get(delivery.tier - 1)) {
                ItemMeta im = item.getItemMeta();
                if (im != null && im.getPersistentDataContainer().has(pl.deliveryKey, PersistentDataType.INTEGER)) {
                    int amount = item.getAmount();
                    int tier = im.getPersistentDataContainer().get(pl.deliveryKey, PersistentDataType.INTEGER);
                    if (tier == delivery.tier)
                        player.getInventory().remove(item);
                }
            }
        }
        pl.economyHandler.economy.depositPlayer(player, pl.tiersManager.reward.get(delivery.tier-1));
    }
    public Delivery getDelivery(Player player) {
        return Deliveries.get(player.getName());
    }

    public void saveDelivery() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(pl.getDataFolder() + "/playerdata/deliveries.yaml")) {
            List<Map<String, Object>> deliveryList = new ArrayList<>();
            for (Delivery delivery : Deliveries.values()) {
                Map<String, Object> deliveryData = new LinkedHashMap<>();
                deliveryData.put("location", delivery.location);
                deliveryData.put("tier", delivery.tier);
                deliveryData.put("uuid", delivery.uuid);
                deliveryData.put("owner", delivery.player);
                deliveryList.add(deliveryData);
            }
            yaml.dump(deliveryList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDelivery() {
        Yaml yaml = new Yaml();
        try {
            Path path = Path.of(pl.getDataFolder() +"/playerdata/deliveries.yaml");
            String fileContent = Files.readString(path);
            List<Map<String, Object>> deliveryList = yaml.load(fileContent);
            if (deliveryList == null) {
                Deliveries.clear();
                return;
            }
            for (Map<String, Object> deliveryData : deliveryList) {
                String location = (String) deliveryData.get("location");
                int tier = (int) deliveryData.get("tier");
                String uuid = (String) deliveryData.get("uuid");
                String player = (String) deliveryData.get("owner");

                Delivery delivery = new Delivery(pl, location, tier, uuid, player);
                Deliveries.put(player, delivery);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
