package com.punkipunk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Proporciona funcionalidad para cargar y parsear archivos JSON a objetos Java.
 * <p>
 * Los archivos JSON se cargan al inicio y se mantienen en memoria como arboles de nodos para un acceso rapido. Los datos son
 * accesibles mediante una ruta con notacion de puntos (por ejemplo: "audio.music.main") y se convierten automaticamente al tipo
 * Java especificado.
 * <p>
 * Caracteristicas principales:
 * <ul>
 * <li>Deserializacion tipada de JSON a objetos Java
 * <li>Carga y parseo automatico de archivos al inicio
 * <li>Patron Singleton para acceso global
 * <li>Manejo de errores robusto con excepciones descriptivas
 * </ul>
 * <p>
 * Ejemplo de uso:
 * <p>
 * {@code AudioData audioData = jsonLoader.deserialize("audio.music.main", AudioData.class);}
 * <p>
 * Los archivos JSON se cargan desde el directorio {@code data/} en los recursos.
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

public class JsonLoader {

    private static JsonLoader instance;
    /** Mapper principal de la API Jackson para la conversion entre JSON y objetos Java. */
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * Estructura que almacena los arboles de nodos JSON parseados, indexados por el nombre del archivo.
     * <p>
     * Cada entrada contiene la representacion en arbol de un documento JSON completo.
     */
    private final Map<String, JsonNode> nodeTree = new HashMap<>();

    private JsonLoader() {
        loadFiles();
    }

    public static JsonLoader getInstance() {
        if (instance == null) instance = new JsonLoader();
        return instance;
    }

    /**
     * Deserializa un nodo del arbol JSON a un objeto Java del tipo especificado.
     * <p>
     * El metodo navega por el arbol JSON usando una ruta con notacion de puntos y deserializa el nodo encontrado al tipo
     * solicitado. Por ejemplo:
     * <pre>{@code
     * // Teniendo un record Java:
     * public record AudioData(String path, boolean loop) {}
     *
     * // Y un arbol JSON que contiene estos datos:
     * {
     *   "music": {
     *     "main": {
     *       "path": "audio/music/main.wav",
     *       "loop": true
     *     }
     *   }
     * }
     *
     * // Podemos deserializar los datos asi:
     * AudioData audioData = jsonLoader.deserialize("audio.music.main", AudioData.class);
     * }</pre>
     *
     * @param path ruta en notacion de puntos al nodo JSON deseado
     * @param type clase destino para la deserializacion
     * @param <T>  tipo de objeto Java resultante
     * @return objeto Java del tipo especificado con los datos deserializados
     * @throws RuntimeException si la ruta no existe o la deserializacion falla
     */
    public <T> T deserialize(String path, Class<T> type) {
        try {
            String[] parts = path.split("\\.");

            // Obtiene el valor de la clave (ej. de clave "audio")
            JsonNode node = nodeTree.get(parts[0]);

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
     * Carga y parsea todos los archivos JSON necesarios para el juego.
     * <p>
     * Los archivos se cargan desde el directorio "data/" en los recursos.
     *
     * @throws RuntimeException si hay errores al cargar o parsear los archivos
     */
    private void loadFiles() {
        try {
            nodeTree.put("items", parseFile("data/items.json"));
            nodeTree.put("audio", parseFile("data/audio.json"));
            nodeTree.put("mobs", parseFile("data/mobs.json"));
            nodeTree.put("spells", parseFile("data/spells.json"));
            nodeTree.put("interactive", parseFile("data/interactive.json"));
            nodeTree.put("player", parseFile("data/player.json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configurations", e);
        }
    }

    /**
     * Parsea un archivo JSON y crea su representacion en arbol de nodos.
     * <p>
     * Cada nodo del arbol puede contener datos primitivos o mas nodos, permitiendo navegar estructuras JSON anidadas. Por
     * ejemplo:
     * <pre>{@code
     * // Para un JSON como:
     * {
     *   "music": {
     *     "main": {
     *       "path": "audio/music/main.wav",
     *       "loop": true
     *     }
     *   }
     * }
     *
     * // Se crea un arbol donde podemos navegar:
     * JsonNode root = parseFile("data/audio.json");
     * JsonNode mainNode = root.get("music").get("main");
     * String path = mainNode.get("path").asText();
     * }</pre>
     *
     * @param path ruta al archivo JSON en el classpath
     * @return arbol de nodos representando la estructura del JSON
     * @throws IOException si hay errores al leer o parsear el archivo
     */
    private JsonNode parseFile(String path) throws IOException {
        try (InputStream is = JsonLoader.class.getResourceAsStream("/" + path)) {
            return mapper.readTree(is);
        }
    }

}
