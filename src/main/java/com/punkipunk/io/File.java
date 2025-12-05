package com.punkipunk.io;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.item.Chest;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemID;
import com.punkipunk.json.model.VolumeData;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class File {

    private final IGame game;
    private final World world;
    private final String data = "data.dat";

    public File(IGame game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Save the game settings.
     */
    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"))) {
            // bw.write(game.fullScreen ? ON : OFF);
            // bw.newLine();
            bw.write(String.valueOf(game.getGameSystem().audio.getMusic().volume));
            bw.newLine();
            bw.write(String.valueOf(game.getGameSystem().audio.getAmbient().volume));
            bw.newLine();
            bw.write(String.valueOf(game.getGameSystem().audio.getSound().volume));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Save game data.
     * <p>
     * TODO No guarda la posicino de los mobs
     */
    public void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(data))) {
            Data data = new Data();

            // Player stats
            data.map = world.map.id;
            data.x = world.entitySystem.player.position.x;
            data.y = world.entitySystem.player.position.y;
            data.direction = world.entitySystem.player.direction;
            data.hp = world.entitySystem.player.stats.hp;
            data.maxHp = world.entitySystem.player.stats.maxHp;
            data.mana = world.entitySystem.player.stats.mana;
            data.maxMana = world.entitySystem.player.stats.maxMana;
            data.strength = world.entitySystem.player.stats.strength;
            data.dexterity = world.entitySystem.player.stats.dexterity;
            data.lvl = world.entitySystem.player.stats.lvl;
            data.exp = world.entitySystem.player.stats.exp;
            data.nextLvlExp = world.entitySystem.player.stats.nextExp;
            data.gold = world.entitySystem.player.stats.gold;

            // Player inventory
            /* for (int i = 0; i < world.entities.player.inventory.size(); i++) {
                data.names.add(world.entities.player.inventory.get(i).stats.name);
                data.amounts.add(world.entities.player.inventory.get(i).amount);
            } */

            // Player equipment
            // data.weapon = world.entities.player.inventory.getSlot(world.entities.player.weapon);
            // data.shield = world.entities.player.inventory.getSlot(world.entities.player.shield);
            // data.light = world.entities.player.inventory.getSlot(world.entities.player.light);

            // Items on map
            /* data.itemName = new String[MAPS][world.entities.items[1].length];
            data.itemX = new int[MAPS][world.entities.items[1].length];
            data.itemY = new int[MAPS][world.entities.items[1].length];
            data.loot = new String[MAPS][world.entities.items[1].length];
            data.opened = new boolean[MAPS][world.entities.items[1].length];
            data.empty = new boolean[MAPS][world.entities.items[1].length];
            for (int map = 0; map < MAPS; map++) {
                for (int i = 0; i < world.entities.items[1].length; i++) {
                    Item item = world.entities.items[map][i];
                    if (item == null) data.itemName[map][i] = "NA";
                    else {
                        data.itemName[map][i] = item.stats.name;
                        data.itemX[map][i] = item.pos.x;
                        data.itemY[map][i] = item.pos.y;
                        if (item.loot != null) data.loot[map][i] = item.loot.stats.name;
                        data.opened[map][i] = item.opened;
                        data.empty[map][i] = item.empty;
                    }
                }
            } */

            // Items en el mapa

            data.itemLists = new ArrayList[MapID.values().length];
            for (MapID mapId : MapID.values()) {
                data.itemLists[mapId.ordinal()] = new ArrayList<>();
                for (Item item : world.entitySystem.getItems(mapId)) {
                    ItemData2 itemData = new ItemData2();
                    itemData.name = item.stats.name;
                    itemData.x = item.position.x;
                    itemData.y = item.position.y;
                    if (item instanceof Chest chest) {
                        if (chest.loot != null) {
                            itemData.loot = chest.loot.stats.name;
                        }
                        itemData.opened = chest.opened;
                        itemData.empty = chest.empty;
                    }
                    data.itemLists[mapId.ordinal()].add(itemData);
                }
            }

            output.writeObject(data);

        } catch (IOException e) {
            throw new RuntimeException("Error saving path " + data, e);
        }
    }

    /**
     * Load game data.
     */
    public void loadData() {
        loadConfig();
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(data))) {
            // Reads the bytes from the input stream and deserializes them into a Data object
            Data data = (Data) input.readObject();
            world.map.id = data.map;
            world.entitySystem.player.position.x = data.x;
            world.entitySystem.player.position.y = data.y;
            switch (data.direction) {
                case DOWN -> world.entitySystem.player.currentFrame = world.entitySystem.player.down.getFirstFrame();
                case UP -> world.entitySystem.player.currentFrame = world.entitySystem.player.up.getFirstFrame();
                case LEFT -> world.entitySystem.player.currentFrame = world.entitySystem.player.left.getFirstFrame();
                case RIGHT -> world.entitySystem.player.currentFrame = world.entitySystem.player.right.getFirstFrame();
            }
            world.entitySystem.player.direction = data.direction;
            world.entitySystem.player.stats.hp = data.hp;
            world.entitySystem.player.stats.maxHp = data.maxHp;
            world.entitySystem.player.stats.mana = data.mana;
            world.entitySystem.player.stats.maxMana = data.maxMana;
            world.entitySystem.player.stats.strength = data.strength;
            world.entitySystem.player.stats.dexterity = data.dexterity;
            world.entitySystem.player.stats.lvl = data.lvl;
            world.entitySystem.player.stats.exp = data.exp;
            world.entitySystem.player.stats.nextExp = data.nextLvlExp;
            world.entitySystem.player.stats.gold = data.gold;

            world.entitySystem.player.inventory.clear();
            /* for (int i = 0; i < data.names.size(); i++) {
                world.entities.player.inventory.add(game.system.itemGenerator.generate(data.names.get(i)));
                world.entities.player.inventory.get(i).amount = data.amounts.get(i);
            }
            world.entities.player.weapon = world.entities.player.inventory.get(data.weapon);
            world.entities.player.shield = world.entities.player.inventory.get(data.shield);
            world.entities.player.light = world.entities.player.inventory.get(data.light); */
            world.entitySystem.player.getAttack();
            world.entitySystem.player.getDefense();
            // world.entities.player.frame.loadWeaponFrames(world.entities.player.weapon.type == Type.SWORD ? player_sword : player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            /* for (int map = 0; map < MAPS; map++) {
                for (int i = 0; i < world.entities.items[1].length; i++) {
                    if (data.itemName[map][i].equals("NA")) world.entities.items[map][i] = null;
                    else {
                        // TODO Tendrias que crear un metodo generador en esta clase
                        //world.entities.items[map][i] = game.system.itemGenerator.generate(data.itemName[map][i]);
                        world.entities.items[map][i].pos.x = data.itemX[map][i];
                        world.entities.items[map][i].pos.y = data.itemY[map][i];
                        if (data.loot[map][i] != null && !data.empty[map][i])
                            //world.entities.items[map][i].loot = game.system.itemGenerator.generate(data.loot[map][i]);
                            world.entities.items[map][i].opened = data.opened[map][i];
                        world.entities.items[map][i].empty = data.empty[map][i];
                        if (world.entities.items[map][i].opened)
                            world.entities.items[map][i].sheet.frame = world.entities.items[map][i].sheet.item[1];
                    }
                }
            } */

            // Cargar items en el mapa
            for (MapID mapId : MapID.values()) {
                // Limpiar items existentes en el mapa
                world.entitySystem.clearItems(mapId);

                // Cargar items guardados
                for (ItemData2 itemData : data.itemLists[mapId.ordinal()]) {
                    if (!itemData.name.equals("NA")) {
                        Item item = world.entitySystem.createItem(
                                ItemID.valueOf(itemData.name.toUpperCase()),
                                mapId,
                                itemData.x,
                                itemData.y
                        );

                        if (item instanceof Chest chest) {
                            if (itemData.loot != null && !itemData.empty) {
                                chest.setLoot(world.entitySystem.createItem(ItemID.valueOf(itemData.loot.toUpperCase()), mapId));
                            }
                            chest.opened = itemData.opened;
                            chest.empty = itemData.empty;
                            if (chest.opened) {
                                chest.sheet.frame = chest.sheet.item[1];
                            }
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error load path " + data, e);
        }
    }

    /**
     * Load the game configuration.
     */
    private void loadConfig() {
        try {
            VolumeData volumeConfig = new VolumeData(
                    3,
                    3,
                    3
            );
            // VolumeLevelConfig volumeConfig = configManager.getConfig("audio.volume", VolumeLevelConfig.class);
            game.getGameSystem().audio.getMusic().volume = volumeConfig.musicVolume();
            game.getGameSystem().audio.getAmbient().volume = volumeConfig.ambientVolume();
            game.getGameSystem().audio.getSound().volume = volumeConfig.soundVolume();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading audio configuration: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        /* try (BufferedReader br = new BufferedReader(new FileReader(config))) {
            // game.fullScreen = ON.equals(br.readLine());
            // TODO Verificar null
            game.system.audio.getMusic().volumeScale = Integer.parseInt(br.readLine());
            game.system.audio.getAmbient().volumeScale = Integer.parseInt(br.readLine());
            game.system.audio.getSound().volumeScale = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } */
    }

}
