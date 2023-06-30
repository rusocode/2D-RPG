package com.craivet.io;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;

import java.io.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class GameFile {

    private final Game game;
    private final World world;
    private final String saveFile = "save.dat";

    public GameFile(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Guarda los datos del juego.
     */
    public void save() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            Data data = new Data();

            // Player status
            data.area = world.area;
            data.map = world.map;
            data.x = world.player.x;
            data.y = world.player.y;
            data.direction = world.player.direction;
            data.life = world.player.HP;
            data.maxlife = world.player.maxHP;
            data.mana = world.player.mana;
            data.maxMana = world.player.maxMana;
            data.strength = world.player.strength;
            data.dexterity = world.player.dexterity;
            data.lvl = world.player.lvl;
            data.exp = world.player.exp;
            data.nextLvlExp = world.player.nextLvlExp;
            data.gold = world.player.gold;

            // Player inventory
            for (int i = 0; i < world.player.inventory.size(); i++) {
                data.names.add(world.player.inventory.get(i).name);
                data.amounts.add(world.player.inventory.get(i).amount);
            }

            // Player equipment
            data.currentWeaponSlot = world.player.getCurrentWeaponSlot();
            data.currentShieldSlot = world.player.getCurrentShieldSlot();
            data.currentLightSlot = world.player.getCurrentLightSlot();

            // Items on map
            data.itemName = new String[MAX_MAP][world.items[1].length];
            data.itemX = new int[MAX_MAP][world.items[1].length];
            data.itemY = new int[MAX_MAP][world.items[1].length];
            data.loot = new String[MAX_MAP][world.items[1].length];
            data.opened = new boolean[MAX_MAP][world.items[1].length];
            data.empty = new boolean[MAX_MAP][world.items[1].length];
            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    Entity item = world.items[map][i];
                    if (item == null) data.itemName[map][i] = "NA";
                    else {
                        data.itemName[map][i] = item.name;
                        data.itemX[map][i] = item.x;
                        data.itemY[map][i] = item.y;
                        if (item.loot != null) data.loot[map][i] = item.loot.name;
                        data.opened[map][i] = item.opened;
                        data.empty[map][i] = item.empty;
                    }
                }
            }

            output.writeObject(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos del juego.
     */
    public void load() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile))) {
            // Lee los bytes desde el flujo de entrada y los deserializa en un objeto Data
            Data data = (Data) input.readObject();
            world.area = data.area;
            world.map = data.map;
            world.player.x = data.x;
            world.player.y = data.y;
            world.player.direction = data.direction;
            world.player.HP = data.life;
            world.player.maxHP = data.maxlife;
            world.player.mana = data.mana;
            world.player.maxMana = data.maxMana;
            world.player.strength = data.strength;
            world.player.dexterity = data.dexterity;
            world.player.lvl = data.lvl;
            world.player.exp = data.exp;
            world.player.nextLvlExp = data.nextLvlExp;
            world.player.gold = data.gold;

            world.player.inventory.clear();
            for (int i = 0; i < data.names.size(); i++) {
                world.player.inventory.add(game.entity.getItem(data.names.get(i)));
                world.player.inventory.get(i).amount = data.amounts.get(i);
            }
            world.player.weapon = world.player.inventory.get(data.currentWeaponSlot);
            world.player.shield = world.player.inventory.get(data.currentShieldSlot);
            world.player.light = world.player.inventory.get(data.currentLightSlot);
            world.player.getAttack();
            world.player.getDefense();
            world.player.loadWeaponImages(world.player.weapon.type == TYPE_SWORD ? entity_player_sword : entity_player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    if (data.itemName[map][i].equals("NA")) world.items[map][i] = null;
                    else {
                        world.items[map][i] = game.entity.getItem(data.itemName[map][i]);
                        world.items[map][i].x = data.itemX[map][i];
                        world.items[map][i].y = data.itemY[map][i];
                        if (data.loot[map][i] != null && !data.empty[map][i])
                            world.items[map][i].loot = game.entity.getItem(data.loot[map][i]);
                        world.items[map][i].opened = data.opened[map][i];
                        world.items[map][i].empty = data.empty[map][i];
                        if (world.items[map][i].opened) world.items[map][i].image = world.items[map][i].image2;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
