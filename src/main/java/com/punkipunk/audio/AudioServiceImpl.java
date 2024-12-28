package com.punkipunk.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkipunk.config.Config;
import com.punkipunk.config.json.AudioConfig;
import com.punkipunk.config.json.VolumeConfig;
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
    /** Gestor de configuraciones para obtener las rutas de los archivos de audio */
    private final Config config = Config.getInstance();
    /** Objeto para manejar la serializacion y deserializacion de JSON */
    private final ObjectMapper mapper = new ObjectMapper();

    public AudioServiceImpl() {
        initAudioChannels();
        loadVolumeConfig();
    }

    @Override
    public void play(AudioChannel channel, String id) {
        try {
            String configPath = buildConfigPath(channel, id);
            AudioConfig audioConfig = config.getJsonValue(configPath, AudioConfig.class);
            play(get(channel), audioConfig);
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
            // Crea un objeto VolumeConfig con el volumen de cada canal
            VolumeConfig volumeConfig = new VolumeConfig(get(AudioChannel.MUSIC).volume, get(AudioChannel.AMBIENT).volume, get(AudioChannel.SOUND).volume);
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, volumeConfig);
        } catch (IOException e) {
            System.err.println("Failed to save volume config\n" + e.getMessage());
        }
    }

    /**
     * Carga la configuracion de volumen desde el archivo.
     * <p>
     * Intenta cargar la configuracion de volumen desde un archivo JSON. Si el archivo existe, lee y aplica la configuracion
     * almacenada. Si no existe, crea un nuevo archivo con valores por defecto. En caso de error durante la carga, establece el
     * volumen por defecto para todos los canales.
     * <p>
     * El metodo {@code writerWithDefaultPrettyPrinter()} configura el escritor para que genere JSON formateado con saltos de
     * linea, sangria (indentacion) y espaciado consistente, en lugar de escribir todo el JSON en una sola linea. Finalmente
     * {@code writeValue()} toma dos parametros: {@code volumeFile} que es el archivo de destino donde se guardara el JSON, y
     * {@code volumeConfig} que es el objeto VolumeConfig que contiene los volumenes actuales para musica, ambiente y efectos de
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
     * Esto hace que el archivo de configuracion sea mas facil de leer y editar manualmente si fuera necesario, aunque ocupe un
     * poco mas de espacio en disco.
     */
    private void loadVolumeConfig() {
        try {
            if (volumeFile.exists()) {
                VolumeConfig volumeConfig = mapper.readValue(volumeFile, VolumeConfig.class);
                setVolumens(volumeConfig);
                return; // Sale del metodo una vez cargado el archivo de volumen para evitar sobreescribir los valores
            }
            // Si no existe el archivo de volumen, crea una nueva configuracion de volumen con los valores por defecto
            VolumeConfig defaultVolumeConfig = new VolumeConfig(DEFAULT_VOLUME, DEFAULT_VOLUME, DEFAULT_VOLUME);
            mapper.writerWithDefaultPrettyPrinter().writeValue(volumeFile, defaultVolumeConfig);
            setVolumens(defaultVolumeConfig);
        } catch (IOException e) {
            System.err.println("Error loading volume file, using defaults\n" + e.getMessage());
            Arrays.stream(AudioChannel.values()).forEach(channel -> get(channel).volume = DEFAULT_VOLUME);
        }
    }

    /**
     * Establece los volumenes a todos los canales de audio.
     *
     * @param config VolumeConfig que contiene los volumenes de cada canal
     */
    private void setVolumens(VolumeConfig config) {
        get(AudioChannel.MUSIC).volume = config.musicVolume();
        get(AudioChannel.AMBIENT).volume = config.ambientVolume();
        get(AudioChannel.SOUND).volume = config.soundVolume();
    }

    /**
     * Inicializa los canales de audio creando una instancia de Audio para cada canal.
     */
    private void initAudioChannels() {
        for (AudioChannel channel : AudioChannel.values())
            channels.put(channel, new Audio());
    }

    /**
     * Construye la ruta de configuracion para un audio especifico.
     *
     * @param channel canal de audio
     * @param id      identificador del audio
     * @return la ruta completa en el formato requerido por el ConfigManager
     */
    private String buildConfigPath(AudioChannel channel, String id) {
        return String.format("audio.%s.%s", channel.name().toLowerCase(), id);
    }

    /**
     * Reproduce un audio usando la configuracion especificada.
     *
     * @param audio  instancia de Audio donde se reproducira el sonido
     * @param config configuracion que contiene la ruta del archivo y si debe reproducirse en bucle
     */
    private void play(Audio audio, AudioConfig config) {
        Optional.ofNullable(getClass().getResource("/" + config.file()))
                .ifPresentOrElse(
                        url -> {
                            audio.play(url);
                            if (config.loop()) audio.loop();
                        },
                        () -> System.err.println("Audio resource not found: " + config.file())
                );
    }

}
