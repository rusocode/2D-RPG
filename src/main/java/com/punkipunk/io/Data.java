package com.punkipunk.io;

import com.punkipunk.Direction;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.world.MapID;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    ArrayList<ItemData2>[] itemLists; // Cambiamos a lista de ItemData por mapa

    MapID map;
    int x, y, hp, maxHp, mana, maxMana, strength, dexterity, lvl, exp, nextLvlExp, gold;
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
