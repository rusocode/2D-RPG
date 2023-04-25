package com.craivet.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    int x, y, direction, life, maxlife, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, coin;

    // TODO Creo que se podria usar un HashMap
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> amounts = new ArrayList<>();

    int currentWeaponSlot, currentShieldSlot;

    // Item on map
    int[][] itemX;
    int[][] itemY;
    String[][] itemName;
    String[][] loot;
    boolean[][] opened;
    boolean[][] empty;
}
