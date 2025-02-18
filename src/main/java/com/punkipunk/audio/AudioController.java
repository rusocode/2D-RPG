package com.punkipunk.audio;

/**
 * Fachada de alto nivel que proporciona una interfaz simplificada para el sistema de audio.
 * <p>
 * Esta clase actua como punto de entrada principal para todas las operaciones de audio, incluyendo:
 * <ul>
 * <li>Reproduccion de musica de fondo</li>
 * <li>Reproduccion de sonidos ambientales</li>
 * <li>Reproduccion de efectos de sonido</li>
 * <li>Control de volumen por canal</li>
 * <li>Gestion del ciclo de vida de recursos de audio</li>
 * </ul>
 * <p>
 * El controlador delega todas las operaciones al servicio de audio subyacente, manteniendo una clara separacion de
 * responsabilidades y ocultando los detalles de implementacion.
 */

public class AudioController {

    /** Servicio que implementa la logica de audio */
    private final AudioService audioService;

    public AudioController() {
        this.audioService = new AudioServiceImpl();
    }

    /**
     * Reproduce un efecto de sonido.
     * <p>
     * Los identificadores de sonido deben corresponder a las entradas definidas en la seccion "sound" del archivo
     * {@code audio.json}.
     *
     * @param id identificador del efecto de sonido
     */
    public void playSound(String id) {
        audioService.play(AudioChannel.SOUND, id);
    }

    /**
     * Reproduce un sonido ambiental.
     * <p>
     * Los identificadores ambientales deben corresponder a las entradas definidas en la seccion "ambient" del archivo
     * {@code audio.json}.
     *
     * @param id identificador del sonido ambiental
     */
    public void playAmbient(String id) {
        audioService.play(AudioChannel.AMBIENT, id);
    }

    /**
     * Reproduce una pista musical.
     * <p>
     * Los identificadores de musica deben corresponder a las entradas definidas en la seccion "music" del archivo
     * {@code audio.json}.
     *
     * @param id identificador de la pista musical
     */
    public void playMusic(String id) {
        audioService.play(AudioChannel.MUSIC, id);
    }

    /**
     * Detiene toda la reproduccion de audio.
     */
    public void stopPlayback() {
        audioService.stopPlayback();
    }

    /**
     * Persiste la configuracion actual de volumen.
     * <p>
     * Guarda los niveles de volumen de todos los canales en el archivo {@code volume.json}.
     */
    public void saveVolume() {
        audioService.saveVolume();
    }

    /**
     * Obtiene la source de audio para el canal de musica.
     *
     * @return source de audio del canal de musica
     */
    public AudioSource getMusic() {
        return audioService.getAudioSource(AudioChannel.MUSIC);
    }

    /**
     * Obtiene la source de audio para el canal ambiental.
     *
     * @return source de audio del canal ambiental
     */
    public AudioSource getAmbient() {
        return audioService.getAudioSource(AudioChannel.AMBIENT);
    }

    /**
     * Obtiene la source de audio para el canal de efectos de sonido.
     *
     * @return source de audio del canal de sonido
     */
    public AudioSource getSound() {
        return audioService.getAudioSource(AudioChannel.SOUND);
    }

    /**
     * Cierra el sistema de audio.
     */
    public void shutdown() {
        audioService.shutdown();
    }

}
