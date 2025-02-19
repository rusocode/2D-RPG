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
    private int musicVolume = DEFAULT_VOLUME, ambientVolume = DEFAULT_VOLUME, soundVolume = DEFAULT_VOLUME;

    private VolumeData volumeData;

    public VolumeSystem() {
        loadOrCreateDefault();
    }

    private void loadOrCreateDefault() {
        try {
            if (volumeFile.exists()) {
                volumeData = mapper.readValue(volumeFile, VolumeData.class);
                musicVolume = volumeData.musicVolume();
                ambientVolume = volumeData.ambientVolume();
                soundVolume = volumeData.soundVolume();
                return;
            }
            createDefault();
        } catch (IOException e) {
            createDefault();
        }
    }

    private void createDefault() {
        volumeData = new VolumeData(DEFAULT_VOLUME, DEFAULT_VOLUME, DEFAULT_VOLUME);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, volumeData);
        } catch (IOException e) {
            System.out.println("Failed to save default volume settings " + e.getMessage());
        }
    }

    public void update(AudioChannel channel, int volume) {
        switch (channel) {
            case MUSIC -> musicVolume = volume;
            case AMBIENT -> ambientVolume = volume;
            case SOUND -> soundVolume = volume;
        }
    }

    public void save() {
        volumeData = new VolumeData(musicVolume, ambientVolume, soundVolume); // TODO No entiendo porque se crea un nuevo VolumeData cada vez que guarda
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, volumeData);
            System.out.println("Volume settings saved successfully");
        } catch (IOException e) {
            System.out.println("Failed to save volume settings " + e.getMessage());
        }
    }

    public int get(AudioChannel channel) {
        return switch (channel) {
            case MUSIC -> musicVolume;
            case AMBIENT -> ambientVolume;
            case SOUND -> soundVolume;
        };
    }

}
