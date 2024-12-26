package com.punkipunk.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Administra la carga y acceso a configuraciones usando el patron Singleton. Proporciona funcionalidad para cargar archivos de
 * configuracion JSON y acceder a sus datos de manera segura respecto a tipos.
 */

public class ConfigManager {

    private static ConfigManager instance;
    /** Es la clase principal de Jackson que permite convertir JSON a objetos Java y viceversa. */
    private final ObjectMapper mapper;
    /** Es una representacion en arbol de un documento JSON, que permite navegar y acceder a los datos. */
    private final Map<String, JsonNode> configs;

    /**
     * Constructor privado para implementar el patron Singleton. Inicializa el mapper y carga las configuraciones.
     */
    private ConfigManager() {
        this.mapper = new ObjectMapper();
        this.configs = new HashMap<>();
        loadConfigs();
    }

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
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
            configs.put("audio", loadConfig("config/audio.json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configurations", e);
        }
    }

    /**
     * Carga un archivo de configuracion JSON desde la ruta especificada.
     *
     * @param path ruta al archivo de configuracion
     * @return JsonNode conteniendo la configuracion parseada
     * @throws IOException si el archivo no puede ser leido o parseado
     */
    private JsonNode loadConfig(String path) throws IOException {
        try (InputStream is = ConfigManager.class.getResourceAsStream("/" + path)) {
            return mapper.readTree(is);
        }
    }

    /**
     * Obtiene un valor de configuracion tipado desde la ruta especificada.
     * <p>
     * La ruta usa notacion de puntos para navegar la estructura JSON (ej. "audio.music.boss").
     *
     * @param path ruta separada por puntos hacia el valor de configuracion
     * @param type clase que representa el tipo esperado del valor de configuracion
     * @param <T>  parametro de tipo para el valor de configuracion
     * @return el valor de configuracion convertido al tipo especificado
     * @throws RuntimeException si la ruta es invalida o el valor no puede ser parseado
     */
    public <T> T getConfig(String path, Class<T> type) {
        try {
            String[] parts = path.split("\\.");

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
