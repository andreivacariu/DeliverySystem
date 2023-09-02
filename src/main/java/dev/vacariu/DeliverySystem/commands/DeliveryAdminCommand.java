package dev.vacariu.DeliverySystem.commands;


import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.internalutils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;

import java.util.UUID;

public class DeliveryAdminCommand implements CommandExecutor {
    private final Main pl;

    public DeliveryAdminCommand(Main pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length==3&&args[0].equalsIgnoreCase("create")){
            if (sender.hasPermission("delivery.admin") && sender instanceof Player){
                Player p = (Player) sender;
                String name = "";
                int tier = 1;
                try{
                    name = String.valueOf(args[1]);
                }catch (Exception ex){
                    p.sendMessage(Utils.asColor("&cWrong argument provided! You have to provide the npc name!"));
                    return true;
                }
                try{
                    tier = Integer.parseInt(args[2]);
                }catch (Exception ex){
                    p.sendMessage(Utils.asColor("&cWrong argument provided! You have to provide the npc reward tier!"));
                    return true;
                }
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
                npc.spawn(p.getLocation());
                pl.npcManager.newNPC(npc.getStoredLocation(),tier, npc.getUniqueId().toString());
                return true;
            }
        }
        return true;
    }
}

