package com.craivet.io;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.item.Item;
import com.craivet.world.Tile;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.craivet.utils.Global.*;

/**
 * Game files.
 *
 * @author Juan Debenedetti
 */

public class File {

    private final Game game;
    private final World world;
    private final String config = "config.txt";
    private final String data = "data.dat";
    private final String tileData = "maps/tile_data.txt";

    public HashMap<Integer, String> maps = new HashMap<>();

    private static final String ON = "On";
    private static final String OFF = "Off";

    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> solids = new ArrayList<>();

    public File(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void load() {
        loadConfig();
        loadTiles();
        loadMaps();
    }

    /**
     * Save the game settings.
     */
    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(config))) {
            // bw.write(game.fullScreen ? ON : OFF);
            // bw.newLine();
            bw.write(String.valueOf(game.music.volumeScale));
            bw.newLine();
            bw.write(String.valueOf(game.sound.volumeScale));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load the game configuration.
     */
    private void loadConfig() {
        try (BufferedReader br = new BufferedReader(new FileReader(config))) {
            // game.fullScreen = ON.equals(br.readLine());
            // TODO Verificar null
            game.music.volumeScale = Integer.parseInt(br.readLine());
            game.sound.volumeScale = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Save game data.
     */
    public void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(data))) {
            Data data = new Data();

            // Player stats
            data.map = world.map.num;
            data.zone = world.map.zone;
            data.x = world.entities.player.pos.x;
            data.y = world.entities.player.pos.y;
            data.direction = world.entities.player.direction;
            data.life = world.entities.player.stats.hp;
            data.maxlife = world.entities.player.stats.maxHp;
            data.mana = world.entities.player.stats.mana;
            data.maxMana = world.entities.player.stats.maxMana;
            data.strength = world.entities.player.stats.strength;
            data.dexterity = world.entities.player.stats.dexterity;
            data.lvl = world.entities.player.stats.lvl;
            data.exp = world.entities.player.stats.exp;
            data.nextLvlExp = world.entities.player.stats.nextLvlExp;
            data.gold = world.entities.player.stats.gold;

            // Player inventory
            for (int i = 0; i < world.entities.player.inventory.size(); i++) {
                data.names.add(world.entities.player.inventory.get(i).stats.name);
                data.amounts.add(world.entities.player.inventory.get(i).amount);
            }

            // Player equipment
            data.weapon = world.entities.player.inventory.getSlot(world.entities.player.weapon);
            data.shield = world.entities.player.inventory.getSlot(world.entities.player.shield);
            data.light = world.entities.player.inventory.getSlot(world.entities.player.light);

            // Items on map
            data.itemName = new String[MAPS][world.entities.items[1].length];
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
            }

            output.writeObject(data);

        } catch (IOException e) {
            throw new RuntimeException("Error saving file " + data, e);
        }
    }

    /**
     * Load game data.
     */
    public void loadData() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(data))) {
            // Reads the bytes from the input stream and deserializes them into a Data object
            Data data = (Data) input.readObject();
            world.map.num = data.map;
            world.map.zone = data.zone;
            world.entities.player.pos.x = data.x;
            world.entities.player.pos.y = data.y;
            switch (data.direction) {
                case DOWN -> world.entities.player.currentFrame = world.entities.player.down.getFirstFrame();
                case UP -> world.entities.player.currentFrame = world.entities.player.up.getFirstFrame();
                case LEFT -> world.entities.player.currentFrame = world.entities.player.left.getFirstFrame();
                case RIGHT -> world.entities.player.currentFrame = world.entities.player.right.getFirstFrame();
            }
            world.entities.player.direction = data.direction;
            world.entities.player.stats.hp = data.life;
            world.entities.player.stats.maxHp = data.maxlife;
            world.entities.player.stats.mana = data.mana;
            world.entities.player.stats.maxMana = data.maxMana;
            world.entities.player.stats.strength = data.strength;
            world.entities.player.stats.dexterity = data.dexterity;
            world.entities.player.stats.lvl = data.lvl;
            world.entities.player.stats.exp = data.exp;
            world.entities.player.stats.nextLvlExp = data.nextLvlExp;
            world.entities.player.stats.gold = data.gold;

            world.entities.player.inventory.clear();
            for (int i = 0; i < data.names.size(); i++) {
                world.entities.player.inventory.add(game.itemGenerator.generate(data.names.get(i)));
                world.entities.player.inventory.get(i).amount = data.amounts.get(i);
            }
            world.entities.player.weapon = world.entities.player.inventory.get(data.weapon);
            world.entities.player.shield = world.entities.player.inventory.get(data.shield);
            world.entities.player.light = world.entities.player.inventory.get(data.light);
            world.entities.player.getAttack();
            world.entities.player.getDefense();
            // world.entities.player.frame.loadWeaponFrames(world.entities.player.weapon.type == Type.SWORD ? player_sword : player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAPS; map++) {
                for (int i = 0; i < world.entities.items[1].length; i++) {
                    if (data.itemName[map][i].equals("NA")) world.entities.items[map][i] = null;
                    else {
                        world.entities.items[map][i] = game.itemGenerator.generate(data.itemName[map][i]);
                        world.entities.items[map][i].pos.x = data.itemX[map][i];
                        world.entities.items[map][i].pos.y = data.itemY[map][i];
                        if (data.loot[map][i] != null && !data.empty[map][i])
                            world.entities.items[map][i].loot = game.itemGenerator.generate(data.loot[map][i]);
                        world.entities.items[map][i].opened = data.opened[map][i];
                        world.entities.items[map][i].empty = data.empty[map][i];
                        if (world.entities.items[map][i].opened)
                            world.entities.items[map][i].sheet.frame = world.entities.items[map][i].sheet.item[1];
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error load file " + data, e);
        }
    }

    /**
     * Reads the data of each tile (name and solid state) from the "tile_data.txt" file and adds them to their
     * respective lists. It then uses that data to load all the tiles into an array.
     */
    private void loadTiles() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(tileData))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            world.map.tileData = new Tile[names.size()];
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file tile_data.txt", e);
        }
    }

    /**
     * Load all the maps that make up the world.
     */
    private void loadMaps() {
        loadMap("maps/abandoned_island.txt", ABANDONED_ISLAND, "Abandoned Island");
        loadMap("maps/abandoned_island_market.txt", ABANDONED_ISLAND_MARKET, "Abandoned Island Market");
        loadMap("maps/dungeon_breg.txt", DUNGEON_BREG, "Dungeon Breg");
        loadMap("maps/dungeon_breg_sub.txt", DUNGEON_BREG_SUB, "Dungeon Breg Sub");
    }

    /**
     * Load the tile.
     *
     * @param i     index of the tile.
     * @param name  name of the tile.
     * @param solid whether it is solid or not.
     */
    private void loadTile(int i, String name, boolean solid) {
        world.map.tileData[i] = new Tile();
        // if (name.equals("001")) world.tileData[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), 128, 128);
        world.map.tileData[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), tile, tile);
        world.map.tileData[i].solid = solid;
    }

    /**
     * Loads the map using the specified path and stores each tile read from the file in the array.
     *
     * @param path path of the resource.
     * @param map  map number as key.
     * @param name map name as value.
     */
    public void loadMap(String path, int map, String name) {
        maps.put(map, name);
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))))) {
            for (row = 0; row < MAX_MAP_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_MAP_COL; col++)
                    world.map.tileIndex[map][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path + " on the line " + (row + 1), e);
        }
    }

}
