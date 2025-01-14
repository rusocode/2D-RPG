package com.punkipunk.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.AudioData;
import com.punkipunk.json.model.VolumeData;
import com.punkipunk.utils.ConfigPaths;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Implementacion del servicio de audio que gestiona la reproduccion, detencion y acceso a los diferentes canales de audio del
 * juego.
 * <p>
 * El sistema utiliza <i>lazy loading</i>, cargando los sonidos bajo demanda cuando se necesitan a traves del metodo
 * {@code play()}.
 */

public class AudioServiceImpl implements AudioService {

    /** Volumen por defecto para todos los canales */
    public static final int DEFAULT_VOLUME = 3;
    /** Nombre del archivo que almacena el volumen de cada canal */
    private static final String VOLUME_FILE = "volume.json";
    /** Archivo de volumen */
    private final File volumeFile = ConfigPaths.getConfigPath(VOLUME_FILE).toFile();
    /** Mapa que relaciona cada canal con su instancia de Audio */
    private final Map<AudioChannel, Audio> channels = new EnumMap<>(AudioChannel.class);
    /** Cargador Json para obtener los datos de audio */
    private final JsonLoader jsonLoader = JsonLoader.getInstance();
    /** Objeto para manejar la serializacion y deserializacion de JSON */
    private final ObjectMapper mapper = new ObjectMapper();

    public AudioServiceImpl() {
        initAudioChannels();
        loadVolume();
    }

    @Override
    public void play(AudioChannel channel, String id) {
        try {
            // Construye la ruta de datos de audio con notacion de puntos
            String path = String.format("audio.%s.%s", channel.name().toLowerCase(), id); // TODO Por que hay que especificar el objeto "audio" si ya forma parte del nombre del archivo json?
            AudioData audioData = jsonLoader.deserialize(path, AudioData.class);
            play(get(channel), audioData);
        } catch (Exception e) {
            System.err.println("Failed to play audio " + id + "\n" + e.getMessage());
        }
    }

    @Override
    public void stop(AudioChannel channel) {
        get(channel).stop();
    }

    @Override
    public void stopAll() {
        channels.values().forEach(Audio::stop);
    }

    @Override
    public Audio get(AudioChannel channel) {
        return channels.get(channel);
    }

    @Override
    public void save() {
        try {
            // Crea un objeto VolumeData con el volumen de cada canal
            VolumeData volumeData = new VolumeData(get(AudioChannel.MUSIC).volume, get(AudioChannel.AMBIENT).volume, get(AudioChannel.SOUND).volume);
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, volumeData);
        } catch (IOException e) {
            System.err.println("Failed to save volume data\n" + e.getMessage());
        }
    }

    /**
     * Carga los datos de volumen desde el archivo.
     * <p>
     * Intenta cargar los datos de volumen desde un archivo JSON. Si el archivo existe, lee y aplica la los datos. Si no existe,
     * crea un nuevo archivo con valores por defecto. En caso de error durante la carga, establece el volumen por defecto para
     * todos los canales.
     * <p>
     * El metodo {@code writerWithDefaultPrettyPrinter()} configura el escritor para que genere JSON formateado con saltos de
     * linea, sangria (indentacion) y espaciado consistente, en lugar de escribir todo el JSON en una sola linea. Finalmente
     * {@code writeValue()} toma dos parametros: {@code volumeFile} que es el archivo de destino donde se guardara el JSON, y
     * {@code volumeData} que es el objeto VolumeData que contiene los volumenes actuales para musica, ambiente y efectos de
     * sonido. Por ejemplo, el archivo resultante se vera asi:
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
    private void loadVolume() {
        try {
            if (volumeFile.exists()) {
                VolumeData volumeData = mapper.readValue(volumeFile, VolumeData.class);
                setVolumens(volumeData);
                return; // Sale del metodo una vez cargado el archivo de volumen para evitar sobreescribir los valores
            }
            // Si no existe el archivo de volumen, crea los datos de volumen con los valores por defecto
            VolumeData defaultVolumeData = new VolumeData(DEFAULT_VOLUME, DEFAULT_VOLUME, DEFAULT_VOLUME);
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, defaultVolumeData);
            setVolumens(defaultVolumeData);
        } catch (IOException e) {
            System.err.println("Error loading volume path, using default volume\n" + e.getMessage());
            Arrays.stream(AudioChannel.values()).forEach(channel -> get(channel).volume = DEFAULT_VOLUME);
        }
    }

    /**
     * Establece los volumenes a todos los canales de audio.
     *
     * @param volumeData datos de volumenes de cada canal
     */
    private void setVolumens(VolumeData volumeData) {
        get(AudioChannel.MUSIC).volume = volumeData.musicVolume();
        get(AudioChannel.AMBIENT).volume = volumeData.ambientVolume();
        get(AudioChannel.SOUND).volume = volumeData.soundVolume();
    }

    /**
     * Inicializa los canales de audio creando una instancia de Audio para cada canal.
     */
    private void initAudioChannels() {
        for (AudioChannel channel : AudioChannel.values())
            channels.put(channel, new Audio());
    }

    /**
     * Reproduce un audio.
     *
     * @param audio     instancia de Audio donde se reproducira el sonido
     * @param audioData datos de audio
     */
    private void play(Audio audio, AudioData audioData) {
        Optional.ofNullable(getClass().getResource("/" + audioData.path()))
                .ifPresentOrElse(
                        url -> {
                            audio.play(url);
                            if (audioData.loop()) audio.loop();
                        },
                        () -> System.err.println("Audio resource not found: " + audioData.path())
                );
    }

}
