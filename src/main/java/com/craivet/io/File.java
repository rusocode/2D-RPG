package com.craivet.io;

import com.craivet.Game;
import com.craivet.util.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.mob.Type;
import com.craivet.world.tile.Tile;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

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
            data.area = world.area;
            data.map = world.map;
            data.x = world.player.x;
            data.y = world.player.y;
            data.direction = world.player.direction;
            data.life = world.player.hp;
            data.maxlife = world.player.maxHp;
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
    public void loadData() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(data))) {
            // Lee los bytes desde el flujo de entrada y los deserializa en un objeto Data
            Data data = (Data) input.readObject();
            world.area = data.area;
            world.map = data.map;
            world.player.x = data.x;
            world.player.y = data.y;
            world.player.direction = data.direction;
            world.player.hp = data.life;
            world.player.maxHp = data.maxlife;
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
                world.player.inventory.add(game.itemGenerator.generate(data.names.get(i)));
                world.player.inventory.get(i).amount = data.amounts.get(i);
            }
            world.player.weapon = world.player.inventory.get(data.currentWeaponSlot);
            world.player.shield = world.player.inventory.get(data.currentShieldSlot);
            world.player.light = world.player.inventory.get(data.currentLightSlot);
            world.player.getAttack();
            world.player.getDefense();
            world.player.animation.loadWeaponFrames(world.player.weapon.type == Type.SWORD ? player_sword : player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

            for (int map = 0; map < MAX_MAP; map++) {
                for (int i = 0; i < world.items[1].length; i++) {
                    if (data.itemName[map][i].equals("NA")) world.items[map][i] = null;
                    else {
                        world.items[map][i] = game.itemGenerator.generate(data.itemName[map][i]);
                        world.items[map][i].x = data.itemX[map][i];
                        world.items[map][i].y = data.itemY[map][i];
                        if (data.loot[map][i] != null && !data.empty[map][i])
                            world.items[map][i].loot = game.itemGenerator.generate(data.loot[map][i]);
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
        loadMap("maps/nix.txt", NIX, "Nix");
        loadMap("maps/nix_indoor01.txt", NIX_INDOOR_01, "Nix Indoor 01");
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
        world.tileData[i].texture = Utils.scaleImage(Utils.loadImage("textures/tiles/" + name), tile_size, tile_size);
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
