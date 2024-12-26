package com.punkipunk.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Proporciona utilidades para obtener las rutas de los archivos de configuracion del juego.
 * <p>
 * Los archivos de configuracion se almacenan en directorios especificos del sistema operativo para garantizar que:
 * <ul>
 * <li>Las configuraciones persistan entre sesiones
 * <li>Cada usuario tenga su propia configuracion
 * <li>Se eviten problemas de permisos al escribir los archivos
 * </ul>
 * <p>
 * Las rutas especificas por sistema operativo son:
 * <ul>
 * <li>Windows: C:/Users/<&lt;>usuario&gt;/AppData/Local/2D-RPG
 * <li>Linux: /home/&lt;usuario&gt;/.local/share/2D-RPG
 * <li>Mac: /Users/&lt;usuario&gt;/Library/Application Support/2D-RPG
 * </ul>
 * <p>
 * Esta separacion es necesaria ya que los archivos dentro del JAR del juego no pueden modificarse durante la ejecucion.
 */

public class ConfigPaths {

    /** Nombre de la carpeta donde se almacenan los archivos de configuracion */
    private static final String APP_NAME = "2D-RPG";

    /**
     * Obtiene la ruta completa para un archivo de configuracion.
     *
     * @param filename nombre del archivo de configuracion
     * @return ruta completa al archivo de configuracion
     * @throws RuntimeException si no se puede crear el directorio de configuracion o no hay permisos de escritura
     */
    public static Path getConfigPath(String filename) {
        Path configDir = getUserDataDirectory();
        File dirFile = configDir.toFile();

        if (!dirFile.exists()) {
            boolean created = dirFile.mkdirs();
            if (!created && !dirFile.exists())
                throw new RuntimeException("Failed to create configuration directory: " + configDir);
        } else if (!dirFile.isDirectory()) {
            throw new RuntimeException("The path exists but is not a directory: " + configDir);
        } else if (!dirFile.canWrite()) {
            throw new RuntimeException("There are no write permissions on the directory: " + configDir);
        }

        return configDir.resolve(filename);
    }

    /**
     * Obtiene el directorio de configuracion especifico del sistema operativo actual.
     *
     * @return Ruta al directorio de configuracion
     */
    private static Path getUserDataDirectory() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return Paths.get(userHome, "AppData", "Local", APP_NAME);
        else if (os.contains("mac")) return Paths.get(userHome, "Library", "Application Support", APP_NAME);
        else return Paths.get(userHome, ".local", "share", APP_NAME);
    }

}
