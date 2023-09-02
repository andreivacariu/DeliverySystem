package dev.vacariu.DeliverySystem;
import dev.vacariu.DeliverySystem.commands.DeliveryAdminCommand;
import dev.vacariu.DeliverySystem.commands.DeliveryCommand;
import dev.vacariu.DeliverySystem.events.EntityEvents;
import dev.vacariu.DeliverySystem.internalutils.EconomyHandler;
import dev.vacariu.DeliverySystem.managers.DeliveryManager;
import dev.vacariu.DeliverySystem.managers.NPCManager;
import dev.vacariu.DeliverySystem.managers.TiersManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class Main extends JavaPlugin {
    public NamespacedKey deliveryKey = new NamespacedKey(this,"deliverysystem.item");
    public DeliveryManager deliveryManager;
    public NPCManager npcManager;
    public TiersManager tiersManager;
    public EconomyHandler economyHandler;

    public void checkFolder() {
        String folderPath = getDataFolder() + "/playerdata/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        tiersManager = new TiersManager(this);
        deliveryManager = new DeliveryManager(this);
        npcManager = new NPCManager(this);
        getCommand("delivery").setExecutor(new DeliveryCommand(this));
        getCommand("deliveryadmin").setExecutor(new DeliveryAdminCommand(this));
        getServer().getPluginManager().registerEvents(new EntityEvents(this),this);
        economyHandler = new EconomyHandler(this);
        economyHandler.init();
        checkFolder();
        deliveryManager.loadDelivery();
        npcManager.loadNpcs();
    }

    @Override
    public void onDisable() {
        deliveryManager.saveDelivery();
        npcManager.saveNpcs();
    }
}
