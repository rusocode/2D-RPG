package com.punkipunk.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC.createCapabilities;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Motor de audio de bajo nivel que gestiona el audio usando OpenAL.
 * <p>
 * Esta clase proporciona una capa de abstraccion sobre OpenAL para gestionar:
 * <ul>
 * <li>Sources dedicadas para musica y ambiente</li>
 * <li>Pool de sources reutilizables para efectos de sonido</li>
 * <li>Cache de buffers para optimizar el uso de memoria</li>
 * <li>Control de volumen por canal</li>
 * <li>Ciclo de vida de recursos OpenAL</li>
 * </ul>
 * <p>
 * El motor utiliza distintas estrategias para cada tipo de canal:
 * <ul>
 * <li>MUSIC y AMBIENT: Sources dedicadas para control individual</li>
 * <li>SOUND: Pool de sources que permite hasta {@value #MAX_SOUND_SOURCES} sonidos simultaneos</li>
 * </ul>
 * <p>
 * El motor tiene soporte para reproducir archivos Ogg Vorbis.
 * <h2>Ogg Vorbis</h2>
 * <p>
 * Ogg Vorbis es un formato de compresion de audio digital de codigo abierto, desarrollado por la
 * <a href="https://xiph.org/vorbis/">Fundacion Xiph.Org</a>. Se compone de dos partes principales:
 * <ol>
 * <li>Ogg: Es el formato contenedor (como MP4 o AVI para video)
 * <li>Vorbis: Es el codec de compresion de audio
 * </ol>
 * <p>
 * Caracteristicas principales:
 * <ul>
 * <li>Es completamente libre de patentes y royalties (a diferencia de MP3)
 * <li>Ofrece mejor calidad que MP3 a tasas de bits similares
 * <li>Soporta streaming y es ampliamente usado en videojuegos
 * <li>Es de codigo abierto
 * <li>Soporta multiples canales (mono, estereo, 5.1, etc.)
 * </ul>
 * <p>
 * La principal ventaja de Ogg Vorbis sobre otros formatos es su naturaleza libre y abierta, combinada con una excelente calidad
 * de audio y eficiencia en la compresion.
 * <p>
 * El WAV es un formato de audio sin comprimir que ofrece maxima calidad pero ocupa mucho espacio, mientras que el Ogg es un
 * formato comprimido que ofrece muy buena calidad ocupando mucho menos espacio. WAV se usa principalmente en entornos
 * profesionales de audio, mientras que Ogg es mas comun en videojuegos y streaming por su buen balance entre calidad y tamaño.
 * Al reemplazar los .wav por los .ogg de este proyecto, se ahorraron casi 87 MB!
 * <p>
 * La clase {@link org.lwjgl.stb.STBVorbis STBVorbis} tiene la funcionalidad de decodificar archivos de audio Ogg Vorbis en
 * memoria antes de reproducirlos con el metodo {@link org.lwjgl.stb.STBVorbis#stb_vorbis_decode_memory stb_vorbis_decode_memory}.
 * Este metodo es comunmente usado en conjunto con OpenAL. Es importante aclarar que la clase {@code STBVorbis} es un enlace
 * (binding) de LWJGL a la libreria <a href="https://github.com/nothings/stb/blob/master/stb_vorbis.c">stb_vorbis.c</a> que forma
 * parte de la coleccion de librerias <a href="https://github.com/nothings/stb">stb</a> creada por Sean Barrett y no una funcion
 * nativa de OpenAL.
 * <h2>Cache de buffers</h2>
 * <p>
 * El buffer de cache almacena los datos de audio decodificados por ID, donde la clave es el ID del sonido (ej: "batDeath",
 * "batHit") dentro de {@code audio.json} y el valor es el ID del buffer de OpenAL que contiene los datos decodificados. Esta
 * estructura evita decodificar el mismo archivo Ogg multiples veces, lo que ahorra memoria y mejora el rendimiento.
 * <p>
 * Por ejemplo:
 * <pre>{@code
 * // Primera vez: crea y almacena
 * play("batDeath") -> bufferCache.get("batDeath") -> null -> getOrCreateBuffer() -> buffer123
 *
 * // Segunda vez: reutiliza el buffer existente
 * play("batDeath") -> bufferCache.get("batDeath") -> buffer123
 * }</pre>
 * <h2>Monitoreo de sources de sonidos</h2>
 * <p>
 * El monitoreo es importante para el manejo eficiente del pool de sources de sonidos. El sistema utiliza un enfoque centralizado
 * con un unico hilo que verifica periodicamente el estado de todas las sources activas.
 * <p>
 * A traves del metodo {@code checkAndRecycleSoundSources()}, el sistema:
 * <ul>
 * <li>Identifica sources que han completado su reproduccion
 * <li>Las remueve del conjunto {@code activeSources}
 * <li>Las devuelve a la cola {@code availableSources} para su reutilizacion
 * </ul>
 * <p>
 * Sin este monitoreo, las sources se mantendrian en {@code activeSources} incluso despues de terminar, el conjunto creceria
 * innecesariamente y eventualmente podrias quedarte sin sources disponibles. Si funciona sin el monitoreo, probablemente:
 * <ul>
 * <li>No estas reproduciendo muchos sonidos simultaneos
 * <li>El juego no ha corrido suficiente tiempo para agotar las sources
 * <li>OpenAL podria estar liberando sources automaticamente (no recomendado depender de esto)
 * </ul>
 * <p>
 * Por ejemplo:
 * <pre>{@code
 * // Sin monitoreo:
 * play() -> source1 al activeSources
 * play() -> source2 al activeSources
 * play() -> source3 al activeSources
 * // Las sources nunca vuelven al availableSources aunque terminen
 *
 * // Con monitoreo centralizado:
 * play() -> source1 al activeSources
 * checkAndRecycleSoundSources() detecta source1 terminada -> la devuelve al availableSources
 * play() -> reutiliza source1 del availableSources
 * }</pre>
 * <p>
 * Actualmente esta clase no esta agotando realmente el pool de sources de sonido porque cuando se llama a {@code play()} sin
 * monitoreo, la source nunca regresa al pool disponible. Las sources permanecen en el pool activo indefinidamente, incluso
 * despues de terminar la reproduccion. El pool disponible se vacia pero las sources siguen existiendo y funcionando.
 * <p>
 * Por lo tanto, sin el monitoreo simplemente estas usando las mismas sources de manera ineficiente (no las reciclas) pero no
 * estas agotando realmente los recursos. Para un juego pequeño con pocos efectos de sonido simultaneos, podrias incluso no notar
 * la diferencia.
 * <p>
 * El monitoreo es mas una cuestion de eficiencia y buenas practicas que de prevencion de agotamiento de recursos en este caso.
 *
 * @see <a href="https://javadoc.lwjgl.org/org/lwjgl/stb/package-summary.html">org.lwjgl.stb</a>
 * <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">OpenAL Programmer's Guide</a>
 * <a
 * href="https://riunet.upv.es/bitstream/handle/10251/105383/Agust%C3%AD%20-%20Extendiendo%20OpenAL%20con%20SDL.%20Caso%20de%20estudio%20MP3.pdf?sequence=1">Extendiendo
 * OpenAL con SDL</a>
 */

public class AudioEngine {

    /** Numero maximo de sources de sonido que pueden reproducirse simultaneamente */
    private static final int MAX_SOUND_SOURCES = 16;
    /** Pool de sources de sonidos */
    public final AudioSourcePool soundPool;
    /** Cache que mapea rutas de archivo a IDs de buffers OpenAL */
    private final Map<String, Integer> buffers = new HashMap<>();
    /** Mapa de sources dedicadas para los canales MUSIC y AMBIENT */
    private final Map<AudioChannel, AudioSource> dedicateSources = new HashMap<>();
    /** Sistema de volumen */
    private final VolumeSystem volumeSystem = new VolumeSystem();
    /** Ejecutor para el monitoreo del pool de sonidos */
    private ScheduledExecutorService soundPoolMonitor;
    /** Dispositivo OpenAL */
    private long device;
    /** Contexto OpenAL */
    private long context;

    public AudioEngine() {
        initializeOpenAL();
        soundPool = new AudioSourcePool(MAX_SOUND_SOURCES, volumeSystem, this);
        initializeDedicatedSources();
        startSoundPoolMonitoring();
    }

    /**
     * Inicializa el sistema OpenAL.
     * <p>
     * Crea un dispositivo y contexto OpenAL por defecto y establece las capacidades necesarias.
     */
    private void initializeOpenAL() {
        // Obtiene el dispositivo por defecto
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        if (device == 0L) throw new IllegalStateException("Failed to open OpenAL device");

        /* Array de atributos de configuracion para el contexto OpenAL. El 0 final actua como terminador para indicar el fin de la
         * lista de atributos. En este caso, al estar vacia (solo el terminador), se usan los valores por defecto. */
        int[] contextAttributes = {ALC_ATTRIBUTES_SIZE, 0};
        context = alcCreateContext(device, contextAttributes);
        if (context == 0L) {
            alcCloseDevice(device);
            throw new IllegalStateException("Failed to create OpenAL context");
        }

        if (!alcMakeContextCurrent(context)) {
            alcDestroyContext(context);
            alcCloseDevice(device);
            throw new IllegalStateException("Failed to make OpenAL context current");
        }

        AL.createCapabilities(createCapabilities(device));

    }

    /**
     * Inicia el monitoreo del pool de sonidos.
     * <p>
     * Crea un hilo que verifica periodicamente el estado de las sources de sonido activas en el pool usando un
     * {@code ScheduledExecutorService} reciclando aquellas que han terminado de reproducirse. Este enfoque centralizado es mas
     * eficiente que monitorear cada source de sonido individualmente ya que:
     * <ul>
     * <li>Reduce la sobrecarga de hilos al usar un unico hilo de monitoreo
     * <li>Mantiene una frecuencia de verificacion consistente
     * <li>Simplifica el control del ciclo de vida de las sources de sonido
     * </ul>
     * <p>
     * El intervalo de verificacion esta fijado en 50ms, lo que proporciona un buen balance entre responsividad y uso de recursos.
     * Es decir que el hilo {@code soundPoolMonitor} se ejecuta cada 50ms para verificar las sources de sonido activas, reciclar
     * las sources de sonido que han terminado de reproducirse y mantener el pool optimizado.
     */
    private void startSoundPoolMonitoring() {
        // Crea un ScheduledExecutorService de un solo hilo
        soundPoolMonitor = Executors.newSingleThreadScheduledExecutor();
        // Programa la tarea (recycleFinishedSources) para ejecutarse cada 50ms
        soundPoolMonitor.scheduleAtFixedRate(soundPool::recycleFinishedSources, 50, 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Inicializa las sources dedicadas.
     */
    private void initializeDedicatedSources() {
        dedicateSources.put(AudioChannel.MUSIC, new AudioSource(alGenSources(), true, false, this, volumeSystem, AudioChannel.MUSIC));
        dedicateSources.put(AudioChannel.AMBIENT, new AudioSource(alGenSources(), true, false, this, volumeSystem, AudioChannel.AMBIENT));
    }

    /**
     * Reproduce un audio.
     *
     * @param channel canal donde reproducir el audio
     * @param path    ruta al archivo de audio
     */
    public void play(AudioChannel channel, String path) {
        int buffer = getOrCreateBuffer(path);
        if (channel == AudioChannel.SOUND) soundPool.getSource().ifPresent(source -> source.play(buffer));
        else dedicateSources.get(channel).play(buffer);
    }

    /**
     * Obtiene un buffer existente de la cache o crea uno nuevo.
     *
     * @param path ruta al archivo de audio
     * @return ID del buffer
     */
    private int getOrCreateBuffer(String path) {
        return buffers.computeIfAbsent(path, this::createBuffer);
    }

    /**
     * Crea un buffer a partir de un archivo de audio.
     * <p>
     * Carga el archivo, lo decodifica a PCM y crea un buffer con los datos. El proceso implica:
     * <ol>
     * <li>Cargar el archivo de audio en memoria
     * <li>Decodificar el audio a formato PCM
     * <li>Crear y configurar el buffer
     * </ol>
     *
     * @param path ruta al archivo de audio
     * @return ID del buffer
     */
    private int createBuffer(String path) {
        /* El metodo stackPush() obtiene una referencia al stack de memoria de uso temporal. El try-with-resources asegura que el
         * stack se libera automaticamente al terminar el bloque try. Si hubiera una excepcion, el stack se liberaria igualmente. */
        try (MemoryStack stack = stackPush()) {

            ByteBuffer audioData = loadAudioFile(path);

            /* El metodo {@code mallocInt(1)} reserva espacio para un entero (4 bytes) en el stack. Devuelve un IntBuffer que
             * apunta a ese espacio reservado. El "1" indica que queremos espacio para un solo entero en donde ese espacio es
             * temporal y se libera cuando termina el try. */
            IntBuffer channelCount = stack.mallocInt(1); // Espacio para guardar la cantidad de canales
            IntBuffer sampleRate = stack.mallocInt(1); // Espacio para guardar frecuencia de muestreo
            // Convierte el audio comprimido a PCM crudo (sin comprimir)
            ShortBuffer decodedAudio = stb_vorbis_decode_memory(audioData, channelCount, sampleRate);
            /* Crea un nuevo buffer para almacenar los datos de audio (canal, decodedAudio y frecuencia). Al final, el buffer de
             * audio estara identificado por un entero que es como un puntero a los datos que contiene. */
            int buffer = alGenBuffers();
            // Carga el PCM en un buffer de OpenAL con el formato apropiado para poder asociarlo a una source y poder trabajar con las funciones de OpenAL
            alBufferData(buffer, channelCount.get() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, decodedAudio, sampleRate.get()); // Es increible como se escucha el audio usando el formato AL_FORMAT_MONO16

            return buffer;
        }
    }

    /**
     * Carga un archivo de audio en memoria.
     *
     * @param path ruta al archivo de audio
     * @return buffer con los datos del archivo
     * @throws RuntimeException si hay error al cargar el archivo
     */
    private ByteBuffer loadAudioFile(String path) {
        try (InputStream is = getClass().getResourceAsStream("/" + path)) {
            byte[] bytes = is.readAllBytes();
            return ByteBuffer.allocateDirect(bytes.length).put(bytes).flip();
        } catch (IOException e) {
            throw new RuntimeException("Error cargando audio: " + path, e);
        }
    }

    /**
     * Obtiene la source de audio asociada a un canal especifico.
     * <p>
     * Para el canal SOUND, devuelve una source virtual que controla el volumen del pool de sonidos.
     *
     * @param channel canal del cual obtener la source
     * @return source de audio asociada al canal
     */
    public AudioSource getAudioSource(AudioChannel channel) {
        if (channel == AudioChannel.SOUND) return new AudioSource(-1, false, true, this, volumeSystem, AudioChannel.SOUND);
        return dedicateSources.get(channel);
    }

    public void saveVolume() {
        volumeSystem.save();
    }

    public void setSoundVolume(int level) {
        soundPool.setVolume(level);
    }

    /**
     * Cierra el sistema de audio liberando todos los recursos OpenAL.
     * <p>
     * El proceso de cierre libera secuencialmente:
     * <ol>
     * <li>Buffers de audio en cache</li>
     * <li>Sources dedicadas (MUSIC y AMBIENT)</li>
     * <li>Sources del pool de sonidos</li>
     * <li>Contexto OpenAL</li>
     * <li>Dispositivo de audio</li>
     * </ol>
     * <p>
     * Es crucial llamar a este metodo antes de cerrar la aplicacion ya que los recursos OpenAL operan fuera del heap de Java y
     * requieren liberacion manual. Sin una limpieza adecuada, pueden ocurrir fugas de memoria y errores de terminacion
     * como {@code 0xC0000409}.
     * <p>
     * Los recursos nativos de OpenAL, como buffers, sources, contexto y dispositivo, operan fuera del heap de Java y no son
     * gestionados automaticamente por el Garbage Collector de la JVM. Esto se debe a que OpenAL interactua directamente con el
     * hardware de audio a traves del sistema operativo, y los identificadores de OpenAL son solo numeros que Java no reconoce
     * como recursos del sistema.
     */
    public void shutdown() {
        soundPoolMonitor.shutdownNow();
        stopAllSources(); // Es importante detener primero todas las sources antes de eliminarlas
        deleteAllResources();
    }

    /**
     * Detiene todas las sources.
     */
    private void stopAllSources() {
        dedicateSources.values().forEach(AudioSource::stop);
        soundPool.stopSources();
    }

    /**
     * Elimina todos los recursos de OpenAL.
     */
    private void deleteAllResources() {
        dedicateSources.values().forEach(audio -> alDeleteSources(audio.source));
        soundPool.deleteSources();
        buffers.values().forEach(AL10::alDeleteBuffers); // El buffer se elimina despues de eliminar las sources ya que las sources podrian estar usando los buffers
        if (context != 0L) alcDestroyContext(context);
        if (device != 0L) alcCloseDevice(device);
    }

    /**
     * Detiene la reproduccion en todas las sources y devuelve las sources activas al conjunto de sources disponibles.
     */
    public void stopPlayback() {
        dedicateSources.values().forEach(AudioSource::stop);
        soundPool.stopPlayback();
    }

}