package com.punkipunk.world;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.map.MapData;
import com.punkipunk.json.model.map.MapsConfig;
import com.punkipunk.utils.Global;
import com.punkipunk.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.punkipunk.utils.Global.MAX_MAP_COL;
import static com.punkipunk.utils.Global.MAX_MAP_ROW;

public class Map {

    private final List<String> names = new ArrayList<>();
    private final List<String> solids = new ArrayList<>();

    public Tile[] tile;
    public int[][][] tileIndex = new int[MapID.values().length][MAX_MAP_ROW][MAX_MAP_COL]; // Matriz tridimensional que almecena mapa, fila y columna del tile

    public MapID id;

    public Map() {
        loadTiles();
        loadMaps();
    }

    /**
     * Lee los datos de cada tile (nombre y estado solido) de la ruta "tile_data.txt" y los agrega a sus respectivas listas.
     * Luego, utiliza esos datos para cargar todos los tiles en una matriz.
     */
    private void loadTiles() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/" + "maps/tile_data.txt"))))) {
            while ((line = br.readLine()) != null) {
                names.add(line);
                solids.add(br.readLine());
            }
            tile = new Tile[names.size()];
            for (int i = 0; i < names.size(); i++)
                loadTile(i, names.get(i), Boolean.parseBoolean(solids.get(i)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading path tile_data.txt", e);
        }
    }

    private void loadTile(int i, String name, boolean solid) {
        tile[i] = new Tile();
        tile[i].texture = Utils.scaleTexture(Utils.loadTexture("textures/tiles/" + name), Global.tile, Global.tile);
        tile[i].solid = solid;
    }

    private void loadMaps() {
        MapsConfig config = JsonLoader.getInstance().deserialize("maps", MapsConfig.class);
        config.maps().forEach((key, mapData) -> loadMap(mapData));
    }

    private void loadMap(MapData mapData) {
        int row = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader((Objects.requireNonNull(getClass().getResourceAsStream("/" + mapData.path())))))) {
            for (row = 0; row < MAX_MAP_ROW; row++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < MAX_MAP_COL; col++)
                    tileIndex[mapData.id().ordinal()][row][col] = Integer.parseInt(numbers[col]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading path " + mapData.path() + " on the line " + (row + 1), e);
        }
    }

}
