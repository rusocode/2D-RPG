package com.punkipunk.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punkipunk.config.ConfigManager;
import com.punkipunk.config.model.AudioConfig;
import com.punkipunk.config.model.VolumeConfig;
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

    public static final int DEFAULT_VOLUME = 3;
    /** Nombre del archivo que almacena la configuracion de volumen */
    private static final String VOLUME_CONFIG_FILE = "volume_settings.json";
    /** Mapa que relaciona cada canal con su instancia de Audio */
    private final Map<AudioChannel, Audio> audioChannels;
    /** Gestor de configuraciones para obtener las rutas de los archivos de audio */
    private final ConfigManager configManager;
    /** Objeto para manejar la serializacion y deserializacion de JSON */
    private final ObjectMapper mapper;

    /**
     * Constructor que inicializa el servicio de audio.
     * <p>
     * Crea una instancia de Audio para cada canal disponible y carga la configuracion de volumen guardada o los valores por
     * defecto si no existe configuracion previa.
     */
    public AudioServiceImpl() {
        this.configManager = ConfigManager.getInstance();
        this.audioChannels = new EnumMap<>(AudioChannel.class);
        this.mapper = new ObjectMapper();
        initializeAudioChannels();
        loadVolumeSettings();
    }

    @Override
    public void play(AudioChannel channel, String audioId) {
        getAudioForChannel(channel).ifPresent(audio -> {
            try {
                String configPath = buildConfigPath(channel, audioId);
                AudioConfig config = configManager.getConfig(configPath, AudioConfig.class);
                playAudio(audio, config);
            } catch (Exception e) {
                System.err.println("Failed to play audio [" + audioId + "]\n" + e.getMessage());
            }
        });
    }

    @Override
    public void stop(AudioChannel channel) {
        getAudioForChannel(channel).ifPresent(Audio::stop);
    }

    @Override
    public void stopAll() {
        audioChannels.values().forEach(Audio::stop);
    }

    @Override
    public Audio getAudio(AudioChannel channel) {
        return audioChannels.get(channel);
    }

    @Override
    public void saveVolumeSettings() {
        try {
            VolumeConfig currentConfig = createVolumeConfig();
            File configFile = ConfigPaths.getConfigPath(VOLUME_CONFIG_FILE).toFile();
            mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, currentConfig);
        } catch (IOException e) {
            System.err.println("Failed to save volume settings\n" + e.getMessage());
        }
    }

    /**
     * Inicializa los canales de audio creando una instancia de Audio para cada canal disponible.
     */
    private void initializeAudioChannels() {
        for (AudioChannel channel : AudioChannel.values())
            audioChannels.put(channel, new Audio());
    }

    /**
     * Carga la configuracion de volumen desde el archivo de configuracion.
     * <p>
     * Si el archivo no existe o hay errores, utiliza los valores por defecto.
     */
    private void loadVolumeSettings() {
        try {
            File configFile = ConfigPaths.getConfigPath(VOLUME_CONFIG_FILE).toFile();
            VolumeConfig volumeConfig = loadOrCreateVolumeConfig(configFile);
            applyVolumeSettings(volumeConfig);
        } catch (IOException e) {
            System.err.println("Error loading volume settings, using defaults\n" + e.getMessage());
            setDefaultVolumes();
        }
    }

    /**
     * Carga la configuracion de volumen desde un archivo o crea una nueva si no existe.
     *
     * @param configFile archivo de configuracion a leer o crear
     * @return configuracion de volumen cargada o creada con valores por defecto
     * @throws IOException si hay errores al leer o escribir el archivo
     */
    private VolumeConfig loadOrCreateVolumeConfig(File configFile) throws IOException {
        if (configFile.exists()) return mapper.readValue(configFile, VolumeConfig.class);

        // Si no existe el archivo, crear uno nuevo con valores por defecto
        VolumeConfig defaultConfig = new VolumeConfig(
                DEFAULT_VOLUME,
                DEFAULT_VOLUME,
                DEFAULT_VOLUME
        );
        mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, defaultConfig);
        return defaultConfig;
    }

    /**
     * Aplica una configuracion de volumen a todos los canales de audio.
     *
     * @param config configuracion de volumen a aplicar
     */
    private void applyVolumeSettings(VolumeConfig config) {
        setChannelVolume(AudioChannel.MUSIC, config.music());
        setChannelVolume(AudioChannel.AMBIENT, config.ambient());
        setChannelVolume(AudioChannel.SOUND, config.sound());
    }

    /**
     * Establece el volumen para un canal especifico.
     *
     * @param channel canal al cual establecer el volumen
     * @param volume  valor de volumen a establecer
     */
    private void setChannelVolume(AudioChannel channel, int volume) {
        getAudioForChannel(channel).ifPresent(audio -> audio.volumeScale = volume);
    }

    /**
     * Establece los valores de volumen por defecto en todos los canales.
     * <p>
     * Se utiliza cuando hay errores al cargar la configuracion guardada.
     */
    private void setDefaultVolumes() {
        Arrays.stream(AudioChannel.values()).forEach(channel -> setChannelVolume(channel, DEFAULT_VOLUME));
    }

    /**
     * Crea un objeto VolumeConfig con los valores actuales de volumen de cada canal.
     *
     * @return nueva instancia de VolumeConfig con los valores actuales
     */
    private VolumeConfig createVolumeConfig() {
        return new VolumeConfig(
                getChannelVolume(AudioChannel.MUSIC),
                getChannelVolume(AudioChannel.AMBIENT),
                getChannelVolume(AudioChannel.SOUND)
        );
    }

    /**
     * Obtiene el volumen actual para un canal especifico.
     *
     * @param channel canal del cual obtener el volumen
     * @return valor actual del volumen o valor por defecto si el canal no existe
     */
    private int getChannelVolume(AudioChannel channel) {
        return Optional.ofNullable(getAudio(channel))
                .map(audio -> audio.volumeScale)
                .orElse(DEFAULT_VOLUME);
    }

    /**
     * Obtiene la instancia de Audio para un canal de forma segura.
     *
     * @param channel canal del cual obtener la instancia de Audio
     * @return optional conteniendo la instancia de Audio si existe
     */
    private Optional<Audio> getAudioForChannel(AudioChannel channel) {
        return Optional.ofNullable(audioChannels.get(channel));
    }

    /**
     * Construye la ruta de configuracion para un audio especifico.
     *
     * @param channel canal de audio
     * @param audioId identificador del audio
     * @return ruta completa en el formato requerido por el ConfigManager
     */
    private String buildConfigPath(AudioChannel channel, String audioId) {
        return String.format("audio.%s.%s", channel.name().toLowerCase(), audioId);
    }

    /**
     * Reproduce un audio usando la configuracion especificada.
     *
     * @param audio  instancia de Audio donde se reproducira el sonido
     * @param config configuracion que contiene la ruta del archivo y si debe reproducirse en bucle
     */
    private void playAudio(Audio audio, AudioConfig config) {
        Optional.ofNullable(getClass().getResource("/" + config.file()))
                .ifPresentOrElse(
                        url -> {
                            audio.play(url);
                            if (config.loop()) {
                                audio.loop();
                            }
                        },
                        () -> System.out.println("Audio resource not found: " + config.file())
                );
    }

}
