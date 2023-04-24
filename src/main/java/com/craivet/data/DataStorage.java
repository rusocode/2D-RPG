package com.craivet.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    int x, y, direction, life, maxlife, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, coin;

    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>();

    int currentWeaponSlot, currentShieldSlot;

    // Item on map
    String[][] mapItemNames;
    int[][] mapItemX;
    int[][] mapItemY;
    String[][] mapItemLootNames;
    boolean[][] mapItemOpened;
    boolean[][] mapItemEmpty;
}
