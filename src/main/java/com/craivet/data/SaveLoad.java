package com.craivet.data;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.entity.item.*;

import java.io.*;
import java.util.IllegalFormatCodePointException;

import static com.craivet.gfx.Assets.entity_player_attack_axe;
import static com.craivet.gfx.Assets.entity_player_attack_sword;
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
            ds.x = world.player.x;
            ds.y = world.player.y;
            ds.direction = world.player.direction;
            ds.life = world.player.life;
            ds.maxlife = world.player.maxLife;
            ds.mana = world.player.mana;
            ds.maxMana = world.player.maxMana;
            ds.strength = world.player.strength;
            ds.dexterity = world.player.dexterity;
            ds.lvl = world.player.lvl;
            ds.exp = world.player.exp;
            ds.nextLvlExp = world.player.nextLvlExp;
            ds.coin = world.player.coin;

            // Player inventory
            for (int i = 0; i < world.player.inventory.size(); i++) {
                ds.itemNames.add(world.player.inventory.get(i).name);
                ds.itemAmounts.add(world.player.inventory.get(i).amount);
            }

            // Player equipment
            ds.currentWeaponSlot = world.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = world.player.getCurrentShieldSlot();

            // Items on map
            ds.mapItemNames = new String[MAX_MAP][world.items[1].length];
            ds.mapItemX = new int[MAX_MAP][world.items[1].length];
            ds.mapItemY = new int[MAX_MAP][world.items[1].length];
            ds.mapItemLootNames = new String[MAX_MAP][world.items[1].length];
            ds.mapItemOpened = new boolean[MAX_MAP][world.items[1].length];
            ds.mapItemEmpty = new boolean[MAX_MAP][world.items[1].length];
            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    Entity item = world.items[map][i];
                    if (item == null) ds.mapItemNames[map][i] = "NA";
                    else {
                        ds.mapItemNames[map][i] = item.name;
                        ds.mapItemX[map][i] = item.x;
                        ds.mapItemY[map][i] = item.y;
                        if (item.loot != null)
                            ds.mapItemLootNames[map][i] = item.loot.name;
                        ds.mapItemOpened[map][i] = item.opened;
                        ds.mapItemEmpty[map][i] = item.empty;
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
            world.player.x = ds.x;
            world.player.y = ds.y;
            world.player.direction = ds.direction;
            world.player.life = ds.life;
            world.player.maxLife = ds.maxlife;
            world.player.mana = ds.mana;
            world.player.maxMana = ds.maxMana;
            world.player.strength = ds.strength;
            world.player.dexterity = ds.dexterity;
            world.player.lvl = ds.lvl;
            world.player.exp = ds.exp;
            world.player.nextLvlExp = ds.nextLvlExp;
            world.player.coin = ds.coin;

            world.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {
                world.player.inventory.add(getItem(ds.itemNames.get(i)));
                world.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }
            world.player.weapon = world.player.inventory.get(ds.currentWeaponSlot);
            world.player.shield = world.player.inventory.get(ds.currentShieldSlot);
            world.player.getAttack();
            world.player.getDefense();
            world.player.loadAttackImages(world.player.weapon.type == TYPE_SWORD ? entity_player_attack_sword : entity_player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    if (ds.mapItemNames[map][i].equals("NA")) world.items[map][i] = null;
                    else {
                        world.items[map][i] = getItem(ds.mapItemNames[map][i]);
                        world.items[map][i].x = ds.mapItemX[map][i];
                        world.items[map][i].y = ds.mapItemY[map][i];
                        if (ds.mapItemLootNames[map][i] != null && !ds.mapItemEmpty[map][i])
                            world.items[map][i].loot = getItem(ds.mapItemLootNames[map][i]);
                        world.items[map][i].opened = ds.mapItemOpened[map][i];
                        world.items[map][i].empty = ds.mapItemEmpty[map][i];
                        if (world.items[map][i].opened) world.items[map][i].image = world.items[map][i].image2;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene el item.
     *
     * @param name nombre del item.
     * @return el item.
     */
    private Entity getItem(String name) {
        Entity item = null;
        switch (name) {
            case "Axe" -> item = new Axe(game, world);
            case "Boots" -> item = new Boots(game, world);
            case "Chest" -> item = new Chest(game, world);
            case "Door" -> item = new Door(game, world);
            case "Key" -> item = new Key(game, world, 1);
            case "Lantern" -> item = new Lantern(game, world);
            case "Red Potion" -> item = new PotionRed(game, world);
            case "Blue Shield" -> item = new ShieldBlue(game, world);
            case "Wood Shield" -> item = new ShieldWood(game, world);
            case "Normal Sword" -> item = new SwordNormal(game, world);
            case "Tent" -> item = new Tent(game, world);
        }
        return item;
    }

}
