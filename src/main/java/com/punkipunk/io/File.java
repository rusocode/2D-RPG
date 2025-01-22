package com.punkipunk.io;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.item.Chest;
import com.punkipunk.entity.item.Item;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.VolumeData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.Tile;
import com.punkipunk.world.World;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.punkipunk.audio.AudioServiceImpl.DEFAULT_VOLUME;
import static com.punkipunk.utils.Global.*;

public class File {

    private static final String ON = "On";
    private static final String OFF = "Off";
    private final Game game;
    private final World world;
    private final String config = "config.txt";
    private final String data = "data.dat";
    private final String tileData = "maps/tile_data.txt";
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> solids = new ArrayList<>();
    private final JsonLoader configManager;
    public HashMap<Integer, String> maps = new HashMap<>();

    public File(Game game, World world) {
        this.game = game;
        this.world = world;
        this.configManager = JsonLoader.getInstance();
    }

    public void load() {
        // loadConfig();
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
            bw.write(String.valueOf(game.gameSystem.audio.getMusic().volume));
            bw.newLine();
            bw.write(String.valueOf(game.gameSystem.audio.getAmbient().volume));
            bw.newLine();
            bw.write(String.valueOf(game.gameSystem.audio.getSound().volume));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load the game configuration.
     */
    private void loadConfig() {
        try {
            VolumeData volumeConfig = new VolumeData(
                    DEFAULT_VOLUME,
                    DEFAULT_VOLUME,
                    DEFAULT_VOLUME
            );
            // VolumeLevelConfig volumeConfig = configManager.getConfig("audio.volume", VolumeLevelConfig.class);
            game.gameSystem.audio.getMusic().volume = volumeConfig.musicVolume();
            game.gameSystem.audio.getAmbient().volume = volumeConfig.ambientVolume();
            game.gameSystem.audio.getSound().volume = volumeConfig.soundVolume();
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

    /**
     * Save game data.
     * <p>
     * TODO No guarda la posicino de los mobs
     */
    public void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(data))) {
            Data data = new Data();

            // Player stats
            data.map = world.map.num;
            data.zone = world.map.zone;
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
            data.itemLists = new ArrayList[MAPS];
            for (int map = 0; map < MAPS; map++) {
                data.itemLists[map] = new ArrayList<>();
                for (Item item : world.entitySystem.getItems(map)) {
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
                    data.itemLists[map].add(itemData);
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
            world.map.num = data.map;
            world.map.zone = data.zone;
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
            for (int map = 0; map < MAPS; map++) {
                // Limpiar items existentes en el mapa
                world.entitySystem.clearItems(map);

                // Cargar items guardados
                for (ItemData2 itemData : data.itemLists[map]) {
                    if (!itemData.name.equals("NA")) {
                        Item item = world.entitySystem.createItem(
                                ItemType.valueOf(itemData.name.toUpperCase()),
                                map,
                                itemData.x,
                                itemData.y
                        );

                        if (item instanceof Chest chest) {
                            if (itemData.loot != null && !itemData.empty) {
                                chest.setLoot(world.entitySystem.createItem(ItemType.valueOf(itemData.loot.toUpperCase()), map));
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
     * Reads the data of each tile (name and solid state) from the "tile_data.txt" path and adds them to their respective lists.
     * It then uses that data to load all the tiles into an array.
     */
    private void loadTiles() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/" + tileData))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            world.map.tileData = new Tile[names.size()];
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading path tile_data.txt", e);
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
        world.map.tileData[i].texture = Utils.scaleTexture(Utils.loadTexture("textures/tiles/" + name), tile, tile);
        world.map.tileData[i].solid = solid;
    }

    /**
     * Loads the map using the specified path and stores each tile read from the path in the array.
     *
     * @param path path of the resource.
     * @param map  map number as key.
     * @param name map name as value.
     */
    public void loadMap(String path, int map, String name) {
        maps.put(map, name);
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getResourceAsStream("/" + path)))))) {
            for (row = 0; row < MAX_MAP_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_MAP_COL; col++)
                    world.map.tileIndex[map][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading path " + path + " on the line " + (row + 1), e);
        }
    }

}
