package com.punkipunk.world;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.map.MapData;
import com.punkipunk.json.model.map.MapsConfig;
import com.punkipunk.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.punkipunk.utils.Global.*;

/**
 * Maneja la carga y almacenamiento de mapas y tiles.
 */

public class Map {

    /** Lista inmutable de tiles */
    public final List<Tile> tiles;

    /**
     * Matriz tridimensional que almacena los indices de tiles para cada mapa.
     * <p>
     * La estructura es [map][row][col]
     */
    public final int[][][] tileIndex;

    /** Identificador del mapa actual */
    public MapID id;

    /**
     * Crea una nueva instancia de Map.
     * <p>
     * Carga los tiles, inicializa la matriz de indices de tiles y carga los mapas.
     */
    public Map() {
        this.tiles = loadTiles();
        this.tileIndex = new int[MapID.values().length][MAX_MAP_ROW][MAX_MAP_COL];
        loadMaps();
    }

    /**
     * Carga los tiles desde el archivo {@code tile.txt}.
     * <p>
     * Cada tile se define en dos lineas consecutivas: <i>texture.png</i> en la primera linea y <i>solid</i> en la segunda linea.
     *
     * @return lista inmutable de tiles cargados
     * @throws RuntimeException si hay error al cargar el archivo
     */
    private List<Tile> loadTiles() {
        List<Tile> loadedTiles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/maps/tile.txt"))))) {
            String name;
            while ((name = br.readLine()) != null) {
                boolean solid = Boolean.parseBoolean(br.readLine());
                loadedTiles.add(createTile(name, solid));
            }
            return Collections.unmodifiableList(loadedTiles);
        } catch (IOException e) {
            throw new RuntimeException("Error loading tile.txt", e);
        }
    }

    /**
     * Crea un tile.
     *
     * @param name  nombre del archivo de textura
     * @param solid true si el tile es solido o false en caso contrario
     * @return el nuevo tile
     */
    private Tile createTile(String name, boolean solid) {
        return new Tile(Utils.scaleTexture(Utils.loadTexture("textures/tiles/" + name), tile, tile), solid);
    }

    /**
     * Carga todos los mapas definidos en {@code maps.json}.
     */
    private void loadMaps() {
        MapsConfig config = JsonLoader.getInstance().deserialize("maps", MapsConfig.class);
        config.maps().forEach((key, mapData) -> loadMap(mapData));
    }

    /**
     * Carga un mapa especifico desde su archivo de texto.
     * <p>
     * Cada linea del archivo representa una fila de tiles.
     * <p>
     * Los parametros de {@code System.arraycopy(tileIndices, 0, tileIndex[mapData.id().ordinal()][row], 0, MAX_MAP_COL);} son:
     * <ol>
     * <li>{@code tileIndices}: array fuente (array de indices obtenido de parseLine)
     * <li>{@code 0}: posicion inicial en el array fuente
     * <li>{@code tileIndex[mapId][row]}: array destino (la fila actual del mapa)
     * <li>{@code 0}: posicion inicial en el array destino
     * <li>{@code MAX_MAP_COL}: cantidad de elementos a copiar
     * </ol>
     * <p>
     * Es mas eficiente que un bucle for porque:
     * <ul>
     * <li>Es un metodo nativo optimizado a bajo nivel
     * <li>Copia bloques de memoria directamente
     * <li>Evita la sobrecarga de iteracion en Java
     * </ul>
     *
     * @param mapData datos del mapa
     * @throws RuntimeException si hay error al leer el archivo o el formato es invalido
     */
    private void loadMap(MapData mapData) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/" + mapData.path()))))) {

            int row = 0;
            String line;

            while ((line = br.readLine()) != null && row < MAX_MAP_ROW) {
                int[] tileIndices = parseLine(line);
                System.arraycopy(tileIndices, 0, tileIndex[mapData.id().ordinal()][row], 0, MAX_MAP_COL);
                row++;
            }

            validateMapDimensions(row, mapData.path());

        } catch (IOException e) {
            throw new RuntimeException("Error reading map: " + mapData.path(), e);
        }
    }

    /**
     * Convierte una linea de texto en un array de indices de tiles:
     * <ul>
     * <li>Recibe una linea de texto como "18 18 18 18 18 18..."
     * <li>{@code line.trim()} elimina espacios al inicio y final
     * <li>{@code split("\\s+")} divide la linea en un array usando espacios como separador
     * <li>{@code mapToInt(Integer::parseInt)} convierte cada string a un numero
     * <li>{@code .toArray()} convierte el stream a un array de ints
     * <li>El resultado es un array como: [18, 18, 18, 18, 18, 18...]
     * </ul>
     * <p>
     * El {@code "\\s+"} es una expresion regular (regex) donde:
     * <ul>
     * <li>{@code \s} representa cualquier caracter de espacio en blanco: Espacio normal, Tab, Nueva linea, Retorno de carro y Tabulacion vertical
     * <li>El {@code +} significa "uno o mas" del caracter anterior
     * <li>Los {@code \\} dobles son necesarios porque:
     * <ul>
     * <li>En Java, {@code \} en strings necesita ser escapado con otro {@code \}
     * <li>Por eso {@code \s} se convierte en {@code \\s}
     * </ul>
     * </ul>
     * Ejemplo:
     * <pre>{@code
     * "18   18 18  18".split(" ")     // ["18", "", "", "", "18", "18", "", "18"]
     * "18   18 18  18".split("\\s+")  // ["18", "18", "18", "18"]
     * }</pre>
     *
     * @param line linea de texto con indices separados por espacios
     * @return array de indices de tiles
     * @throws RuntimeException si la linea contiene valores invalidos
     */
    private int[] parseLine(String line) {
        try {
            return Arrays.stream(line.trim().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid tile index in map", e);
        }
    }

    /**
     * Valida que el mapa tenga el numero correcto de filas.
     *
     * @param rowCount numero de filas leidas
     * @param mapPath  ruta del archivo del mapa
     * @throws IllegalStateException si el numero de filas no coincide con MAX_MAP_ROW
     */
    private void validateMapDimensions(int rowCount, String mapPath) {
        if (rowCount != MAX_MAP_ROW)
            throw new IllegalStateException("Invalid number of rows in map " + mapPath + ". Expected: " + MAX_MAP_ROW + ", Found: " + rowCount);
    }

}
