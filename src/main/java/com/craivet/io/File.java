package com.craivet.io;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.item.Item;
import com.craivet.world.tile.Tile;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static com.craivet.utils.Global.*;

/**
 * Archivos del juego.
 *
 * @author Juan Debenedetti
 */

public class File {

    private final Game game;
    private final World world;
    private final String config = "config.txt";
    private final String data = "data.dat";
    private final String tileData = "maps/tile_data.txt";

    private static final String ON = "On";
    private static final String OFF = "Off";

    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> solids = new ArrayList<>();

    public File(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Guarda la configuracion del juego.
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
     * Carga la configuracion del juego.
     */
    public void loadConfig() {
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
     * Guarda los datos del juego.
     */
    public void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(data))) {
            Data data = new Data();

            // Player status
            data.map = world.map;
            data.zone = world.zone;
            data.x = world.player.pos.x;
            data.y = world.player.pos.y;
            data.direction = world.player.direction;
            data.life = world.player.stats.hp;
            data.maxlife = world.player.stats.maxHp;
            data.mana = world.player.stats.mana;
            data.maxMana = world.player.stats.maxMana;
            data.strength = world.player.stats.strength;
            data.dexterity = world.player.stats.dexterity;
            data.lvl = world.player.stats.lvl;
            data.exp = world.player.stats.exp;
            data.nextLvlExp = world.player.stats.nextLvlExp;
            data.gold = world.player.stats.gold;

            // Player inventory
            for (int i = 0; i < world.player.inventory.size(); i++) {
                data.names.add(world.player.inventory.get(i).stats.name);
                data.amounts.add(world.player.inventory.get(i).amount);
            }

            // Player equipment
            data.currentWeaponSlot = world.player.getCurrentWeaponSlot();
            data.currentShieldSlot = world.player.getCurrentShieldSlot();
            data.currentLightSlot = world.player.getCurrentLightSlot();

            // Items on map
            data.itemName = new String[MAPS][world.items[1].length];
            data.itemX = new int[MAPS][world.items[1].length];
            data.itemY = new int[MAPS][world.items[1].length];
            data.loot = new String[MAPS][world.items[1].length];
            data.opened = new boolean[MAPS][world.items[1].length];
            data.empty = new boolean[MAPS][world.items[1].length];
            for (int map = 0; map < MAPS; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    Item item = world.items[map][i];
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
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos del juego.
     */
    public void loadData() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(data))) {
            // Lee los bytes desde el flujo de entrada y los deserializa en un objeto Data
            Data data = (Data) input.readObject();
            world.map = data.map;
            world.zone = data.zone;
            world.player.pos.x = data.x;
            world.player.pos.y = data.y;
            switch (data.direction) {
                case DOWN -> world.player.currentFrame = world.player.down.getFirstFrame();
                case UP -> world.player.currentFrame = world.player.up.getFirstFrame();
                case LEFT -> world.player.currentFrame = world.player.left.getFirstFrame();
                case RIGHT -> world.player.currentFrame = world.player.right.getFirstFrame();
            }
            world.player.direction = data.direction;
            world.player.stats.hp = data.life;
            world.player.stats.maxHp = data.maxlife;
            world.player.stats.mana = data.mana;
            world.player.stats.maxMana = data.maxMana;
            world.player.stats.strength = data.strength;
            world.player.stats.dexterity = data.dexterity;
            world.player.stats.lvl = data.lvl;
            world.player.stats.exp = data.exp;
            world.player.stats.nextLvlExp = data.nextLvlExp;
            world.player.stats.gold = data.gold;

            world.player.inventory.clear();
            for (int i = 0; i < data.names.size(); i++) {
                world.player.inventory.add(game.itemGenerator.generate(data.names.get(i)));
                world.player.inventory.get(i).amount = data.amounts.get(i);
            }
            world.player.weapon = world.player.inventory.get(data.currentWeaponSlot);
            world.player.shield = world.player.inventory.get(data.currentShieldSlot);
            world.player.light = world.player.inventory.get(data.currentLightSlot);
            world.player.getAttack();
            world.player.getDefense();
            // world.player.frame.loadWeaponFrames(world.player.weapon.type == Type.SWORD ? player_sword : player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAPS; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    if (data.itemName[map][i].equals("NA")) world.items[map][i] = null;
                    else {
                        world.items[map][i] = game.itemGenerator.generate(data.itemName[map][i]);
                        world.items[map][i].pos.x = data.itemX[map][i];
                        world.items[map][i].pos.y = data.itemY[map][i];
                        if (data.loot[map][i] != null && !data.empty[map][i])
                            world.items[map][i].loot = game.itemGenerator.generate(data.loot[map][i]);
                        world.items[map][i].opened = data.opened[map][i];
                        world.items[map][i].empty = data.empty[map][i];
                        if (world.items[map][i].opened) world.items[map][i].sheet.frame = world.items[map][i].sheet.item[1];
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee los datos de cada tile (nombre y estado solido) desde el archivo "tile_data.txt" y los agrega a sus
     * respectivas listas. Luego utiliza esos datos para cargar todos los tiles dentro de un array.
     */
    public void loadTiles() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(tileData))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            world.tileData = new Tile[names.size()];
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file tile_data.txt", e);
        }
    }

    /**
     * Carga todos los mapas que componen al mundo.
     */
    public void loadMaps() {
        loadMap("maps/nashe.txt", NASHE, "Nashe");
        loadMap("maps/nashe_indoor01.txt", NASHE_INDOOR_01, "Nashe Indoor 01");
        loadMap("maps/dungeon01.txt", DUNGEON_01, "Dungeon 01");
        loadMap("maps/dungeon02.txt", DUNGEON_02, "Dungeon 02");
    }

    /**
     * Carga el tile.
     *
     * @param i     indice del tile.
     * @param name  nombre del tile.
     * @param solid si es solido o no.
     */
    private void loadTile(int i, String name, boolean solid) {
        world.tileData[i] = new Tile();
        world.tileData[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), tile, tile);
        world.tileData[i].solid = solid;
    }

    /**
     * Carga el mapa utilizando la ruta especificada y almacena cada valor (tile) leido del archivo en la matriz.
     *
     * @param path ruta del recurso.
     * @param map  numero del mapa como clave.
     * @param name nombre del mapa como valor.
     */
    public void loadMap(String path, int map, String name) {
        world.maps.put(map, name);
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))))) {
            for (row = 0; row < MAX_MAP_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_MAP_COL; col++)
                    world.tileIndex[map][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path + " on the line " + (row + 1), e);
        }
    }

}
