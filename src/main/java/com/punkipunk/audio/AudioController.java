package com.punkipunk.audio;

import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.*;

/**
 * Controlador que gestiona la reproduccion de audio en el juego.
 * <p>
 * Actua como una capa de abstraccion entre el mundo del juego y el servicio de audio, facilitando la reproduccion de efectos de
 * sonido, musica y sonidos ambientales.
 */

public class AudioController {

    /** Servicio que maneja las operaciones de audio de bajo nivel */
    private final AudioService audioService;
    private final World world;

    /**
     * Constructor que inicializa el controlador de audio.
     *
     * @param world referencia al mundo del juego
     */
    public AudioController(World world) {
        this.audioService = new AudioServiceImpl();
        this.world = world;
    }

    /**
     * Reproduce el audio ambiental correspondiente a la zona actual del mapa.
     * <p>
     * Cada zona tiene su propia musica o sonido ambiental caracteristico.
     */
    public void playZoneAmbient() {
        switch (world.map.zone) {
            case OVERWORLD -> playAmbient(AudioID.Ambient.OVERWORLD);
            case DUNGEON -> playAmbient(AudioID.Ambient.DUNGEON);
            case BOSS -> playMusic(AudioID.Music.BOSS);
        }
    }

    /**
     * Reproduce un efecto de sonido.
     *
     * @param audioId identificador del sonido a reproducir
     */
    public void playSound(String audioId) {
        audioService.play(AudioChannel.SOUND, audioId);
    }

    /**
     * Reproduce un sonido ambiental.
     * <p>
     * Detiene cualquier sonido ambiental que este reproduciendose actualmente.
     *
     * @param audioId identificador del sonido ambiental a reproducir
     */
    public void playAmbient(String audioId) {
        audioService.stop(AudioChannel.AMBIENT);
        audioService.play(AudioChannel.AMBIENT, audioId);
    }


    /**
     * Reproduce una pista musical.
     * <p>
     * Detiene cualquier musica que este reproduciendose actualmente.
     *
     * @param audioId identificador de la musica a reproducir
     */
    public void playMusic(String audioId) {
        audioService.stop(AudioChannel.MUSIC);
        audioService.play(AudioChannel.MUSIC, audioId);
    }

    /**
     * Detiene toda la reproduccion de audio en todos los canales.
     */
    public void stopAll() {
        audioService.stopAll();
    }

    /**
     * Obtiene el controlador de audio ambiental.
     *
     * @return instancia de Audio para el canal ambiental
     */
    public Audio getAmbient() {
        return audioService.getAudio(AudioChannel.AMBIENT);
    }

    /**
     * Obtiene el controlador de musica.
     *
     * @return instancia de Audio para el canal de musica
     */
    public Audio getMusic() {
        return audioService.getAudio(AudioChannel.MUSIC);
    }

    /**
     * Obtiene el controlador de efectos de sonido.
     *
     * @return instancia de Audio para el canal de sonido
     */
    public Audio getSound() {
        return audioService.getAudio(AudioChannel.SOUND);
    }

    /**
     * Guarda la configuracion actual de volumen.
     */
    public void saveVolumeSettings() {
        audioService.saveVolumeSettings();
    }

}
