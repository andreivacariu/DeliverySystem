package dev.vacariu.DeliverySystem.components;

import dev.vacariu.DeliverySystem.Main;

public class Delivery {
    private final Main main;
    public final String location;
    public final String player;
    public int tier;
    public String uuid;
    public Delivery(Main main, String location, int tier, String uuid, String player) {
        this.main = main;
        this.location = location;
        this.tier = tier;
        this.uuid = uuid;
        this.player = player;
    }
}
