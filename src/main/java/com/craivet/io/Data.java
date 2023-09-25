package com.craivet.io;

import com.craivet.Direction;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    // TODO Falta el entorno
    int zone, map, x, y, life, maxlife, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, gold;
    int weapon, shield, light;
    Direction direction;

    // TODO Creo que se podria usar un HashMap
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> amounts = new ArrayList<>();

    // Item en el mapa
    int[][] itemX, itemY;
    String[][] itemName, loot;
    boolean[][] opened, empty;

}
