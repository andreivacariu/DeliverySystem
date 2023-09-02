package dev.vacariu.DeliverySystem.components;

import dev.vacariu.DeliverySystem.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NPC {
        private final Main main;
        public final String location;
        public final String uuid;
        public int tier;
        public NPC(Main main, String location, int tier, String uuid) {
            this.main = main;
            this.location = location;
            this.tier = tier;
            this.uuid = uuid;
        }

    }

