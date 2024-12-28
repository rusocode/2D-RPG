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
     * Parsea un archivo de configuracion en formato JSON y lo convierte en una estructura de arbol. El metodo lee el archivo JSON
     * especificado usando Jackson y crea una representacion jerarquica en memoria. Esta estructura de arbol permite acceder y
     * navegar por los datos JSON usando notacion de puntos. Por ejemplo, asi es la estructura de audio.json:
     * <pre>{@code
     * {
     *   "music": {
     *     "main": {
     *       "file": "audio/music/main.wav",
     *       "loop": true
     *     }
     *   }
     * }
     * }</pre>
     * <p>
     * El arbol resultante permite acceder a los nodos mediante encadenamiento de get():
     * root.get("audio").get("music").get("main).
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

    /**
     * <p>
     * Extrae y convierte un valor del JSON a un objeto Java del tipo especificado. El metodo navega por el arbol JSON usando una
     * ruta con notacion de puntos y convierte el nodo encontrado al tipo solicitado. Por ejemplo:
     * <pre>{@code
     * {
     *   "music": {
     *     "main": {
     *       "file": "audio/music/main.wav",
     *       "loop": true
     *     }
     *   }
     * }
     * // Podemos obtener la configuracion asi:
     * AudioConfig config = getJsonValue("audio.music.main", AudioConfig.class);
     * // Resultando en: AudioConfig("audio/music/main.wav", true)
     * }</pre>
     * <p>
     * La conversion utiliza Jackson para mapear automaticamente los campos JSON a las propiedades del objeto Java.
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

}
