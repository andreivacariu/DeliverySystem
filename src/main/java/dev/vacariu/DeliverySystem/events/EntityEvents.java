package dev.vacariu.DeliverySystem.events;

import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.components.Delivery;
import dev.vacariu.DeliverySystem.managers.NPCManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;


public class EntityEvents implements Listener {
    private final Main pl;

    public EntityEvents(Main pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        boolean isCitizensNPC = entity.hasMetadata("NPC");

        if (!isCitizensNPC)  return;

        NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
        dev.vacariu.DeliverySystem.components.NPC citizen = pl.npcManager.getNPC(npc.getUniqueId().toString());
        Delivery deliveryData = pl.deliveryManager.getDelivery(player);
        if (citizen == null || deliveryData == null)
            return;

        if (!Objects.equals(deliveryData.uuid, citizen.uuid))
            return;

        pl.deliveryManager.finishDelivery(player);


    }

}
