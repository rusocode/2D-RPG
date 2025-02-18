package com.punkipunk.audio;

import java.util.*;

import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenSources;

/**
 * Administra un pool de {@link AudioSource} para optimizar el uso de recursos de OpenAL.
 * <p>
 * Esta clase implementa el patron Object Pool para gestionar un conjunto limitado de sources de audio que pueden ser
 * reutilizadas, evitando la creacion y destruccion frecuente de recursos OpenAL.
 * <p>
 * El pool mantiene dos colecciones:
 * <ul>
 * <li>Una cola de sources disponibles para usar
 * <li>Un conjunto de sources actualmente en uso
 * </ul>
 * Cuando una source termina de reproducirse, es devuelta automaticamente al pool de disponibles.
 * <p>
 * Si en un futuro el juego es multiplayer, entonces seria recomendable usar colecciones concurrentes
 * como ConcurrentLinkedQueue y ConcurrentHashMap.newKeySet().
 */

public class AudioSourcePool {

    /** Cola de sources disponibles */
    private final Queue<AudioSource> availableSources = new LinkedList<>();
    /** Conjunto de sources activas */
    private final Set<AudioSource> activeSources = new HashSet<>();

    public AudioSourcePool(int size, VolumeSystem volumeSystem, AudioEngine audioEngine) {
        for (int i = 0; i < size; i++)
            availableSources.offer(new AudioSource(alGenSources(), false, false, audioEngine, volumeSystem, AudioChannel.SOUND));
    }

    /**
     * Obtiene una source disponible.
     * <p>
     * Si hay sources disponibles, remueve una de la cola de disponibles y la agrega al conjunto de activas.
     *
     * @return Optional conteniendo la source si hay disponible, empty si el pool esta agotado
     */
    public Optional<AudioSource> getSource() {
        AudioSource source = availableSources.poll();
        if (source != null) {
            activeSources.add(source);
            return Optional.of(source);
        }
        return Optional.empty();
    }

    /**
     * Recicla las sources que han terminado de reproducirse.
     * <p>
     * Verifica cada source activa y si ha terminado de reproducir:
     * <ul>
     * <li>La remueve del conjunto de activas
     * <li>La devuelve a la cola de disponibles
     * </ul>
     * Usa un Iterator para permitir la modificacion segura de la coleccion mientras se itera sobre ella.
     */
    public void recycleFinishedSources() {
        Iterator<AudioSource> iterator = activeSources.iterator();
        while (iterator.hasNext()) {
            AudioSource source = iterator.next();
            if (!source.isPlaying()) {
                iterator.remove();
                availableSources.offer(source);
            }
        }
    }

    /**
     * Establece el nivel de volumen para todas las sources.
     * <p>
     * Actualiza el volumen tanto de las sources activas como de las disponibles para mantener la consistencia cuando las sources
     * sean reutilizadas.
     *
     * @param level nivel de volumen a establecer
     */
    public void setVolume(int level) {
        activeSources.forEach(source -> source.setVolume(level, false)); // Le pasa false para evitar llamadas recursivas
        availableSources.forEach(source -> source.setVolume(level, false));
    }

    /**
     * Detiene todas las sources.
     */
    public void stopSources() {
        activeSources.forEach(AudioSource::stop);
        availableSources.forEach(AudioSource::stop);
    }

    /**
     * Elimina todas las sources.
     */
    public void deleteSources() {
        availableSources.forEach(audio -> alDeleteSources(audio.source));
        activeSources.forEach(audio -> alDeleteSources(audio.source));
    }

    /**
     * Detiene la reproduccion y recicla todas las sources activas.
     * <p>
     * Este metodo:
     * <ul>
     * <li>Detiene todas las sources activas
     * <li>Las devuelve al pool de disponibles
     * <li>Limpia el conjunto de activas
     * </ul>
     */
    public void stopPlayback() {
        activeSources.forEach(AudioSource::stop);
        availableSources.addAll(activeSources);
        activeSources.clear();
    }

}
