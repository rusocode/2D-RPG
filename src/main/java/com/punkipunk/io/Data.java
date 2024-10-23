package com.punkipunk.io;

import com.punkipunk.Direction;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    // TODO The environment is missing
    int zone, map, x, y, hp, maxHp, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, gold;
    int weapon, shield, light;
    Direction direction;

    // TODO I think you could use a HashMap
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> amounts = new ArrayList<>();

    // Item on the map
    int[][] itemX, itemY;
    String[][] itemName, loot;
    boolean[][] opened, empty;

}
