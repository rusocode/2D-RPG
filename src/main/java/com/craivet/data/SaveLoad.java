package com.craivet.data;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import java.io.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class SaveLoad {

    private final Game game;
    private final World world;

    public SaveLoad(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Guarda los datos del player.
     * <p>
     * Almacena los datos del player en el DataStorage y los escribe en el flujo de salida hacia el archivo
     * especificado.
     */
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));
            DataStorage ds = new DataStorage();

            // Player status
            ds.area = world.area;
            ds.map = world.map;
            ds.x = world.player.x;
            ds.y = world.player.y;
            ds.direction = world.player.direction;
            ds.life = world.player.HP;
            ds.maxlife = world.player.maxHP;
            ds.mana = world.player.mana;
            ds.maxMana = world.player.maxMana;
            ds.strength = world.player.strength;
            ds.dexterity = world.player.dexterity;
            ds.lvl = world.player.lvl;
            ds.exp = world.player.exp;
            ds.nextLvlExp = world.player.nextLvlExp;
            ds.gold = world.player.gold;

            // Player inventory
            for (int i = 0; i < world.player.inventory.size(); i++) {
                ds.names.add(world.player.inventory.get(i).name);
                ds.amounts.add(world.player.inventory.get(i).amount);
            }

            // Player equipment
            ds.currentWeaponSlot = world.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = world.player.getCurrentShieldSlot();
            ds.currentLightSlot = world.player.getCurrentLightSlot();

            // Items on map
            ds.itemName = new String[MAX_MAP][world.items[1].length];
            ds.itemX = new int[MAX_MAP][world.items[1].length];
            ds.itemY = new int[MAX_MAP][world.items[1].length];
            ds.loot = new String[MAX_MAP][world.items[1].length];
            ds.opened = new boolean[MAX_MAP][world.items[1].length];
            ds.empty = new boolean[MAX_MAP][world.items[1].length];
            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    Entity item = world.items[map][i];
                    if (item == null) ds.itemName[map][i] = "NA";
                    else {
                        ds.itemName[map][i] = item.name;
                        ds.itemX[map][i] = item.x;
                        ds.itemY[map][i] = item.y;
                        if (item.loot != null) ds.loot[map][i] = item.loot.name;
                        ds.opened[map][i] = item.opened;
                        ds.empty[map][i] = item.empty;
                    }
                }
            }

            oos.writeObject(ds);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carga los datos del player.
     * <p>
     * Lee los bytes desde el flujo de entrada y los deserializa en un objeto DataStorage.
     */
    public void load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));
            DataStorage ds = (DataStorage) ois.readObject();
            world.area = ds.area;
            world.map = ds.map;
            world.player.x = ds.x;
            world.player.y = ds.y;
            world.player.direction = ds.direction;
            world.player.HP = ds.life;
            world.player.maxHP = ds.maxlife;
            world.player.mana = ds.mana;
            world.player.maxMana = ds.maxMana;
            world.player.strength = ds.strength;
            world.player.dexterity = ds.dexterity;
            world.player.lvl = ds.lvl;
            world.player.exp = ds.exp;
            world.player.nextLvlExp = ds.nextLvlExp;
            world.player.gold = ds.gold;

            world.player.inventory.clear();
            for (int i = 0; i < ds.names.size(); i++) {
                world.player.inventory.add(game.generator.getItem(ds.names.get(i)));
                world.player.inventory.get(i).amount = ds.amounts.get(i);
            }
            world.player.weapon = world.player.inventory.get(ds.currentWeaponSlot);
            world.player.shield = world.player.inventory.get(ds.currentShieldSlot);
            world.player.light = world.player.inventory.get(ds.currentLightSlot);
            world.player.getAttack();
            world.player.getDefense();
            world.player.loadWeaponImages(world.player.weapon.type == TYPE_SWORD ? entity_player_sword : entity_player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    if (ds.itemName[map][i].equals("NA")) world.items[map][i] = null;
                    else {
                        world.items[map][i] = game.generator.getItem(ds.itemName[map][i]);
                        world.items[map][i].x = ds.itemX[map][i];
                        world.items[map][i].y = ds.itemY[map][i];
                        if (ds.loot[map][i] != null && !ds.empty[map][i])
                            world.items[map][i].loot = game.generator.getItem(ds.loot[map][i]);
                        world.items[map][i].opened = ds.opened[map][i];
                        world.items[map][i].empty = ds.empty[map][i];
                        if (world.items[map][i].opened) world.items[map][i].image = world.items[map][i].image2;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
