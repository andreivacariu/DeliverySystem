package dev.vacariu.DeliverySystem.commands;


import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.internalutils.LocationUtils;
import dev.vacariu.DeliverySystem.internalutils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;

import javax.sound.midi.SysexMessage;
import java.io.Console;
import java.util.*;

public class DeliveryCommand implements CommandExecutor {
    private final Main pl;

    public DeliveryCommand(Main pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("delivery.admin")){
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                if (sender instanceof ConsoleCommandSender) {
                    Bukkit.getLogger().info(Utils.asColor("&cWrong argument provided! You have to provide the player name to open the gui!"));
                } else {
                    Player psender = (Player) sender;
                    psender.sendMessage(Utils.asColor("&cWrong argument provided! You have to provide the player name to open the gui!"));
                }
                return true;
            }
            InventoryGUI deliveries = new InventoryGUI(Bukkit.createInventory(null, pl.getConfig().getInt("gui.slots"), Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("gui.name")))));
            deliveries.fill(0, pl.getConfig().getInt("gui.slots"),new ItemStack(Material.valueOf(pl.getConfig().getString("gui.fillingItem"))));
            int listSize = pl.tiersManager.material.size();
            for (int i = 0; i < listSize; i++) {
                int currentTier = pl.tiersManager.tier.get(i);
                int slot = pl.tiersManager.slot.get(i);
                deliveries.addButton(ItemButton.create(pl.tiersManager.getDeliveryItem(currentTier), e -> {
                    dev.vacariu.DeliverySystem.components.NPC citizen = pl.npcManager.getRandomNPCByTier(currentTier);
                    NPC npc;
                    if (pl.deliveryManager.getDelivery(p) != null) {
                        p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.deliveryAlreadyStarted"))));
                        deliveries.destroy();
                        p.closeInventory();
                    } else if (citizen != null) {
                        npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(citizen.uuid));
                        pl.deliveryManager.newDelivery(npc, currentTier, p);
                        String location = Math.round(npc.getStoredLocation().getX()) + " " + Math.round(npc.getStoredLocation().getY()) + " " + Math.round(npc.getStoredLocation().getZ());
                        p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.startedDelivery").replaceAll("%location%", location))));
                        deliveries.destroy();
                        p.closeInventory();
                    } else {
                        p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.failedToStartDelivery"))));
                        deliveries.destroy();
                        p.closeInventory();
                    }
                }), slot);
            }
            deliveries.open(p);
            return true;
        }
            if (args.length == 0 && sender.hasPermission("delivery.menu") && sender instanceof Player){
                Player p = (Player) sender;
                InventoryGUI deliveries = new InventoryGUI(Bukkit.createInventory(null, pl.getConfig().getInt("gui.slots"), Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("gui.name")))));
                deliveries.fill(0, pl.getConfig().getInt("gui.slots"),new ItemStack(Material.valueOf(pl.getConfig().getString("gui.fillingItem"))));
                int listSize = pl.tiersManager.material.size();
                for (int i = 0; i < listSize; i++) {
                    int currentTier = pl.tiersManager.tier.get(i);
                    int slot = pl.tiersManager.slot.get(i);
                    deliveries.addButton(ItemButton.create(pl.tiersManager.getDeliveryItem(currentTier), e -> {
                        dev.vacariu.DeliverySystem.components.NPC citizen = pl.npcManager.getRandomNPCByTier(currentTier);
                        NPC npc;
                        if (pl.deliveryManager.getDelivery(p) != null) {
                            p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.deliveryAlreadyStarted"))));
                            deliveries.destroy();
                            p.closeInventory();
                        } else if (citizen != null) {
                            npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(citizen.uuid));
                            pl.deliveryManager.newDelivery(npc, currentTier, p);
                            String location = Math.round(npc.getStoredLocation().getX()) + " " + Math.round(npc.getStoredLocation().getY()) + " " + Math.round(npc.getStoredLocation().getZ());
                            p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.startedDelivery").replaceAll("%location%", location))));
                            deliveries.destroy();
                            p.closeInventory();
                        } else {
                            p.sendMessage(Utils.asColor(Utils.translateHexColorCodes("&#","",pl.getConfig().getString("messages.delivery.failedToStartDelivery"))));
                            deliveries.destroy();
                            p.closeInventory();
                        }
                    }), slot);
                }
                deliveries.open(p);
                return true;
            }


        return true;
    }
}

