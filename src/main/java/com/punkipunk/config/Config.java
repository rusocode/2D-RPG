package com.punkipunk.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Proporciona funcionalidad para cargar archivos de configuracion y acceder a sus datos de manera segura.
 * <p>
 * Los archivos de configuracion se cargan al inicio y se mantienen en memoria para un acceso rapido. Cada configuracion es
 * accesible mediante una ruta con notacion de puntos (por ejemplo: "audio.music.main").
 * <p>
 * Caracteristicas principales:
 * <ul>
 * <li>Acceso tipado a las configuraciones
 * <li>Carga automatica de archivos al inicio
 * <li>Patron Singleton para acceso global
 * <li>Manejo de errores robusto
 * </ul>
 * <p>
 * Ejemplo de uso:
 * <p>
 * {@code AudioConfig audioConfig = config.getJsonValue("audio.music.main", AudioConfig.class);}
 * <p>
 * Las configuraciones se cargan desde los archivos JSON ubicados en el directorio de recursos.
 * <p>
 * Esta clase es thread-safe debido a su naturaleza inmutable una vez inicializada.
 * <p>
 * El metodo {@code readTree()} permite convertir un JSON a un arbol de nodos, mientras que el metodo {@code treeToValue()}
 * convierte un nodo del arbol a un objeto Java. Estos metodos generalmente se utilizan juntos cuando necesitas primero leer y
 * navegar por un JSON complejo usando readTree() y luego convertir partes especificas de ese JSON a objetos Java usando
 * treeToValue().
 * <p>
 * En resumen:
 * <ul>
 * <li>{@code readTree()}: JSON → Arbol de nodos
 * <li>{@code treeToValue()}: Nodo del arbol → Objeto Java
 * </ul>
 */

public class Config {

    private static Config instance;
    /** Es la clase principal de Jackson que permite convertir JSON a objetos Java y viceversa. */
    private final ObjectMapper mapper = new ObjectMapper();
    /** Es una representacion en arbol de un documento JSON, que permite navegar y acceder a los datos. */
    private final Map<String, JsonNode> configs = new HashMap<>();

    private Config() {
        loadConfigs();
    }

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    /**
     * <p>
     * Convierte un nodo del arbol JSON (JsonNode) a un objeto Java de una clase especifica. El metodo navega por el arbol JSON
     * usando una ruta con notacion de puntos y convierte el nodo encontrado al tipo solicitado. Por ejemplo:
     * <pre>{@code
     * // Teniendo una clase Java:
     * public record AudioConfig(String file, boolean loop) {}
     *
     * // Y un nodo del arbol JSON que contiene "file" y "loop":
     * JsonNode mainNode = root.get("music").get("main");
     *
     * // Podemos convertirlo a un objeto AudioConfig:
     * AudioConfig config = mapper.treeToValue(mainNode, AudioConfig.class);
     * // Ahora tenemos un objeto con:
     * // config.file() = "audio/music/main.wav"
     * // config.loop() = true
     * }</pre>
     *
     * @param path ruta en notacion de puntos al valor JSON deseado
     * @param type clase destino para la conversion del valor JSON
     * @param <T>  tipo de objeto Java al que se convertira el valor JSON
     * @return el objeto Java del tipo especificado con los datos del JSON
     * @throws RuntimeException si la ruta no existe o la conversion falla
     */
    public <T> T getJsonValue(String path, Class<T> type) {
        try {
            String[] parts = path.split("\\.");

            // Obtiene el valor de la clave (ej. de clave "audio")
            JsonNode node = configs.get(parts[0]);

            if (node == null) throw new RuntimeException("Config not found: " + parts[0]);

            for (int i = 1; i < parts.length; i++) {
                node = node.get(parts[i]);
                if (node == null) throw new RuntimeException("Config path not found: " + path);
            }

            return mapper.treeToValue(node, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse config: " + path, e);
        }
    }

    /**
     * Carga todos los archivos de configuracion en memoria.
     *
     * @throws RuntimeException si las configuraciones no pueden ser cargadas
     */
    private void loadConfigs() {
        try {
            // Carga las configuraciones al inicio
            // configs.put("entities", loadConfig("config/entities.json"));
            // configs.put("items", loadConfig("config/items.json"));
            configs.put("audio", parseJsonConfig("config/audio.json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configurations", e);
        }
    }

    /**
     * <p>
     * Parsea un archivo de configuracion en formato JSON y lo convierte a un arbol de nodos ({@code JsonNode}). Es como construir
     * un arbol donde cada elemento del JSON es un nodo que puede contener mas nodos. Ejemplo:
     * <pre>{@code
     * {
     *   "music": {
     *     "main": {
     *       "file": "audio/music/main.wav",
     *       "loop": true
     *     }
     *   }
     * }
     * // Se convierte en un arbol de nodos:
     * JsonNode root = mapper.readTree(InputStream);
     * JsonNode musicNode = root.get("music");
     * JsonNode mainNode = musicNode.get("main");
     * String file = mainNode.get("file").asText(); // "audio/music/main.wav"
     * boolean loop = mainNode.get("loop").asBoolean(); // true
     * }</pre>
     *
     * @param path ruta al archivo JSON dentro del classpath
     * @return el JsonNode representando el arbol de datos JSON
     * @throws IOException si hay errores al leer o parsear el JSON
     */
    private JsonNode parseJsonConfig(String path) throws IOException {
        try (InputStream is = Config.class.getResourceAsStream("/" + path)) {
            return mapper.readTree(is);
        }
    }

}
