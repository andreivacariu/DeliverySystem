package dev.vacariu.DeliverySystem.managers;

import dev.vacariu.DeliverySystem.Main;
import dev.vacariu.DeliverySystem.components.Delivery;
import dev.vacariu.DeliverySystem.components.NPC;
import dev.vacariu.DeliverySystem.internalutils.LocationUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NPCManager {
    private final Main pl;
    public NPCManager(Main pl) {
        this.pl = pl;
    }

    public static final Map<String, NPC> npcs = new HashMap<>();
    public void newNPC(Location location, int tier, String uuid){
        String loc = LocationUtils.locationToString(location);
        npcs.put(uuid, new NPC(pl, loc, tier, uuid));
    }
    public void removeNPC(String uuid) {
        npcs.remove(uuid);
    }
    public NPC getNPC(String UUID) {
        return npcs.get(UUID);
    }

    public NPC getRandomNPCByTier(int tier) {
        List<NPC> npcsByTier = new ArrayList<>();
        for (NPC npc : pl.npcManager.npcs.values()) {
            if (npc.tier == tier) {
                npcsByTier.add(npc);
            }
        }

        if (npcsByTier.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(npcsByTier.size());
        return npcsByTier.get(randomIndex);
    }

    public void saveNpcs() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(pl.getDataFolder() + "/playerdata/NPCS.yaml")) {
            List<Map<String, Object>> npcList = new ArrayList<>();
            for (NPC npc : npcs.values()) {
                Map<String, Object> npcData = new LinkedHashMap<>();
                npcData.put("location", npc.location);
                npcData.put("tier", npc.tier);
                npcData.put("uuid", npc.uuid);
                npcList.add(npcData);
            }
            yaml.dump(npcList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadNpcs() {
        Yaml yaml = new Yaml();
        try {
            Path path = Path.of(pl.getDataFolder() +"/playerdata/NPCS.yaml");
            String fileContent = Files.readString(path);
            List<Map<String, Object>> npcList = yaml.load(fileContent);
            if (npcList == null) {
                npcs.clear();
                return;
            }
            for (Map<String, Object> npcData : npcList) {
                String location = (String) npcData.get("location");
                int tier = (int) npcData.get("tier");
                String uuid = (String) npcData.get("uuid");

                NPC npc = new NPC(pl, location, tier, uuid);
                npcs.put(uuid, npc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
