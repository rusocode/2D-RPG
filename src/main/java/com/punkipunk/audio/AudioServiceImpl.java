package com.punkipunk.audio;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.AudioData;

/**
 * <p>
 * Implementacion por defecto del servicio de audio que maneja la reproduccion y control del sistema de audio.
 * <p>
 * Esta clase actua como intermediaria entre el controlador de audio y el motor de audio, proporcionando una capa de abstraccion
 * que:
 * <ul>
 * <li>Carga y gestiona la configuracion de audio desde archivos JSON</li>
 * <li>Coordina la reproduccion entre diferentes canales de audio</li>
 * <li>Maneja la persistencia de configuracion de volumen</li>
 * <li>Gestiona el ciclo de vida de los recursos de audio</li>
 * </ul>
 * <p>
 * La configuracion de audio se carga desde el archivo {@code audio.json} con la siguiente estructura:
 * <pre>{@code
 * {
 *   "music": {
 *     "main": { "path": "audio/music/main.ogg" }
 *   },
 *   "ambient": {
 *     "forest": { "path": "audio/ambient/forest.ogg" }
 *   },
 *   "sound": {
 *     "click": { "path": "audio/sound/click.ogg" }
 *   }
 * }
 * }</pre>
 */

public class AudioServiceImpl implements AudioService {

    /** Motor de audio que maneja las operaciones de bajo nivel con OpenAL */
    private final AudioEngine audioEngine = new AudioEngine();
    /** Cargador de configuracion de audio desde archivos JSON */
    private final JsonLoader jsonLoader = JsonLoader.getInstance();

    @Override
    public void play(AudioChannel channel, String id) {
        String path = String.format("audio.%s.%s", channel.name().toLowerCase(), id);
        AudioData audioData = jsonLoader.deserialize(path, AudioData.class);
        audioEngine.play(channel, audioData.path());
    }

    @Override
    public void stopPlayback() {
        audioEngine.stopPlayback();
    }

    @Override
    public AudioSource getAudioSource(AudioChannel channel) {
        return audioEngine.getAudioSource(channel);
    }

    @Override
    public void saveVolume() {
        audioEngine.saveVolume();
    }

    @Override
    public void shutdown() {
        audioEngine.shutdown();
    }

}