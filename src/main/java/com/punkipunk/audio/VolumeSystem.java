package com.punkipunk.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkipunk.json.model.VolumeData;
import com.punkipunk.utils.ConfigPaths;

import java.io.File;
import java.io.IOException;

import static com.punkipunk.audio.AudioSource.DEFAULT_VOLUME;

/**
 * <p>
 * Sistema que gestiona los niveles de volumen y su persistencia.
 * <p>
 * Esta clase maneja:
 * <ul>
 * <li>Carga y guardado de configuracion de volumen en un archivo JSON</li>
 * <li>Mantenimiento de niveles de volumen para musica, ambiente y efectos de sonido</li>
 * <li>Actualizacion individual de volumen por canal</li>
 * </ul>
 * <p>
 * El sistema usa un archivo JSON para persistir los niveles de volumen entre sesiones.
 * <p>
 * El metodo {@code writerWithDefaultPrettyPrinter()} configura el escritor para que genere JSON formateado con saltos de linea,
 * sangria (indentacion) y espaciado consistente, en lugar de escribir todo en una sola linea. Finalmente {@code writeValue()}
 * toma dos parametros: {@code volumeFile} que es el archivo JSON donde se guardara el volumen y {@code defaultVolume} que es el
 * objeto VolumeData que contiene los volumenes predeterminados para musica, ambiente y efectos de sonido. Por ejemplo, el archivo
 * resultante se vera asi:
 * <pre>{@code
 * {
 *   "musicVolume" : 5,
 *   "ambientVolume" : 3,
 *   "soundVolume" : 3
 * }
 * }</pre>
 * <p>
 * En lugar de:
 * <pre>{@code
 * {"musicVolume":3,"ambientVolume":2,"soundVolume":4}
 * }</pre>
 * <p>
 * Esto hace que el archivo de datos sea mas facil de leer y editar manualmente si fuera necesario, aunque ocupe un poco mas
 * de espacio en disco.
 */

public class VolumeSystem {

    /** Archivo que almacena la configuracion de volumen */
    private final File volumeFile = ConfigPaths.getConfigPath("volume.json").toFile();
    /** Objeto para serializar/deserializar datos de volumen en formato JSON */
    private final ObjectMapper mapper = new ObjectMapper();
    /** Estado actual de los volumenes */
    private VolumeData volumeData;

    public VolumeSystem() {
        volumeData = loadOrCreateDefault();
    }

    /**
     * Obtiene el nivel de volumen actual para un canal especifico.
     *
     * @param channel canal de audio del cual obtener el volumen
     * @return nivel de volumen actual (0-5) para el canal especificado
     */
    public int getVolume(AudioChannel channel) {
        return switch (channel) {
            case MUSIC -> volumeData.musicVolume();
            case AMBIENT -> volumeData.ambientVolume();
            case SOUND -> volumeData.soundVolume();
        };
    }

    /**
     * Carga la configuracion de volumen existente o crea una nueva por defecto.
     *
     * @return datos de volumen cargados o creados
     */
    private VolumeData loadOrCreateDefault() {
        try {
            if (volumeFile.exists())
                return mapper.readValue(volumeFile, VolumeData.class); // Sale del metodo una vez cargado el archivo de volumen para evitar sobreescribir los valores por los predeterminados
            return createDefaultVolume();
        } catch (IOException e) {
            return createDefaultVolume();
        }
    }

    /**
     * Crea y guarda una configuracion de volumen con valores por defecto.
     *
     * @return configuracion de volumen por defecto
     */
    private VolumeData createDefaultVolume() {
        VolumeData defaultVolume = new VolumeData(DEFAULT_VOLUME, DEFAULT_VOLUME, DEFAULT_VOLUME);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, defaultVolume);
            System.out.println("Created default volume settings");
        } catch (IOException e) {
            System.out.println("Failed to save default volume settings " + e.getMessage());
        }
        return defaultVolume;
    }

    /**
     * Guarda la configuracion actual de volumen en el archivo.
     */
    public void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, volumeData);
            System.out.println("Volume settings saved successfully");
        } catch (IOException e) {
            System.out.println("Failed to save volume settings " + e.getMessage());
        }
    }

    /**
     * Actualiza el nivel de volumen para un canal especifico.
     * <p>
     * Crea una nueva instancia de VolumeData con el valor actualizado para el canal especificado, manteniendo los valores
     * existentes para los otros canales. El cambio se persiste automaticamente en el archivo de configuracion.
     *
     * @param channel canal de audio a actualizar
     * @param volume  nuevo nivel de volumen (0-5)
     */
    public void update(AudioChannel channel, int volume) {
        volumeData = switch (channel) {
            case MUSIC -> new VolumeData(volume, volumeData.ambientVolume(), volumeData.soundVolume());
            case AMBIENT -> new VolumeData(volumeData.musicVolume(), volume, volumeData.soundVolume());
            case SOUND -> new VolumeData(volumeData.musicVolume(), volumeData.ambientVolume(), volume);
        };
        save();
    }

}
