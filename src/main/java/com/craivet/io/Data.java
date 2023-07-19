package com.craivet.io;

import com.craivet.Direction;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    // TODO Falta el entorno
    int area, map, x, y, life, maxlife, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, gold;
    Direction direction;

    // TODO Creo que se podria usar un HashMap
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> amounts = new ArrayList<>();

    int currentWeaponSlot, currentShieldSlot, currentLightSlot;

    // Item on map
    int[][] itemX;
    int[][] itemY;
    String[][] itemName;
    String[][] loot;
    boolean[][] opened;
    boolean[][] empty;

}
