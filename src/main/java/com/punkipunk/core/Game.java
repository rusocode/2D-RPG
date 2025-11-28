package com.punkipunk.core;

import com.punkipunk.audio.AudioID;
import com.punkipunk.controllers.GameController;
import com.punkipunk.utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * <h1>Sistemas de Renderizado</h1>
 * <h2>Canvas</h2>
 * <p>
 * El Canvas es un elemento digital rectangular que funciona como lienzo para dibujar graficos 2D mediante programacion. Permite
 * crear y manipular formas geometricas, texto y figuras complejas, usando metodos de dibujo que controlan cada pixel. Ofrece
 * funciones para manejar colores, gradientes, transparencias y transformaciones geometricas. Es util para visualizar datos, crear
 * graficos personalizados, firmas digitales y juegos 2D. Ademas, el Canvas permite la interaccion con eventos del mouse y del
 * teclado, lo que posibilita la creacion de aplicaciones interactivas donde los usuarios pueden dibujar o modificar el contenido
 * del lienzo en tiempo real. Una caracteristica importante del Canvas es que mantiene un sistema de coordenadas cartesianas donde
 * el origen (0,0) se encuentra en la esquina superior izquierda, y los valores positivos de x se extienden hacia la derecha y los
 * valores positivos de y hacia abajo, lo que facilita el posicionamiento preciso de elementos graficos en el lienzo.
 * <h2>Aceleracion por Hardware</h2>
 * <p>
 * La aceleracion por hardware es cuando un programa utiliza componentes fisicos especializados del computador (como la GPU) para
 * realizar ciertas tareas en lugar de usar solo el procesador principal (CPU). Podemos entenderlo con una analogia: imagina que
 * necesitas cortar 1000 piezas de madera, usar solo la CPU seria como cortarlas manualmente con un serrucho, mientras que usar
 * aceleracion por hardware seria como usar una sierra electrica especializada.
 * <p>
 * En el contexto de este juego:
 * <p>
 * <b>Sin aceleracion por hardware (CPU):</b>
 * <ul>
 * <li>El procesador principal maneja todos los calculos graficos
 * <li>Tiene que procesar cada pixel individualmente
 * <li>Mas lento para operaciones graficas complejas
 * <li>Mayor carga en la CPU
 * </ul>
 * <p>
 * <b>Con aceleracion por hardware (GPU):</b>
 * <ul>
 * <li>La tarjeta grafica maneja el renderizado
 * <li>Esta diseñada especificamente para procesar graficos
 * <li>Puede manejar miles de pixeles en paralelo
 * <li>Libera la CPU para otras tareas
 * </ul>
 * <p>
 * Para saber que tipo de renderizado esta utilizando JavaFX, se agrega el parametro {@code -Dprism.verbose=true} a la VM. Esto
 * muestra informacion detallada sobre que pipeline grafico esta usando. Los posibles pipelines que puede usar JavaFX son:
 * <ul>
 * <li>D3D (DirectX) - Windows
 * <li>ES2 (OpenGL) - Linux/Mac/Windows
 * <li>SW (Software) - Fallback cuando no hay aceleracion por hardware
 * </ul>
 * <p>
 * El pipeline (o tuberia de renderizado en español) es una secuencia de pasos que transforman los datos de un juego en imagenes
 * en pantalla. Es como una linea de ensamblaje donde cada etapa realiza una tarea especifica para producir el resultado final.
 * <h2>Diferencia entre JavaFX y la API tradicional de Java (AWT/Swing)</h2>
 * <h3>JavaFX</h3>
 * <p>
 * Framework moderno para interfaces graficas con las siguientes caracteristicas:
 * <ul>
 * <li><b>Scene Graph:</b> Estructura jerarquica de nodos para UI
 * <li><b>Aceleracion por Hardware:</b> Renderizado por hardware mediante el motor <b>Prism</b>
 * <li><b>Styling:</b> Soporte nativo para CSS y FXML
 * <li><b>Data Binding:</b> Sistema de propiedades y bindings
 * <li><b>Efectos:</b> Animaciones y efectos visuales avanzados
 * </ul>
 * <p>
 * {@code AnimationTimer}, que es la clase que se encarga de las animacion, proporciona soluciones automaticas para problemas
 * comunes:
 * <ol>
 * <li>Fixed Timestep:
 * <ul>
 * <li>Sincronizacion automatica con vsync
 * <li>Llamadas estables a handle()
 * <li>Sin necesidad de implementar paso de tiempo fijo manual
 * </ul>
 * <li>Spiral of Death:
 * <ul>
 * <li>Gestion interna de eventos y frames
 * <li>Ajuste automatico en caso de retrasos
 * <li>Prevencion de acumulacion de actualizaciones pendientes
 * </ul>
 * <li>Independencia de Frame Rate:
 * <ul>
 * <li>Sincronizacion con el hardware
 * <li>Movimiento suave sin deltaTime
 * <li>Tasa de actualizacion estable
 * </ul>
 * </ol>
 * Ejemplo:
 * <pre>{@code
 * public class MainApp extends Application {
 *     @Override
 *     public void start(Stage primaryStage) {
 *         Button btn = new Button("Hola JavaFX");
 *         Scene scene = new Scene(btn, 200, 100);
 *         primaryStage.setScene(scene);
 *         primaryStage.show();
 *     }
 * }
 *  }</pre>
 * <p>
 * Ventajas:
 * <ul>
 * <li>Rendimiento optimizado con aceleracion por GPU
 * <li>API moderna y declarativa
 * <li>Mejor manejo de animaciones
 * <li>Separacion de logica y presentacion
 * <li>Herramientas modernas como Scene Builder
 * </ul>
 * <h3>API Java (AWT/Swing)</h3>
 * <p>
 * Sistema tradicional de interfaces graficas con:
 * <ul>
 * <li><b>Componentes:</b> Elementos pesados (AWT) y ligeros (Swing)
 * <li><b>Renderizado:</b> Basado en software
 * <li><b>Eventos:</b> Sistema tradicional de listeners
 * <li><b>Estilos:</b> Look and Feel personalizable
 * </ul>
 * Ejemplo:
 * <pre>{@code
 * public class MainWindow extends JFrame {
 *     public MainWindow() {
 *         JButton btn = new JButton("Hola Swing");
 *         add(btn);
 *         setSize(200, 100);
 *         setVisible(true);
 *     }
 * }
 * }</pre>
 * <p>
 * Ventajas:
 * <ul>
 * <li>Mayor compatibilidad con sistemas legacy
 * <li>API madura y bien documentada
 * <li>Menor curva de aprendizaje
 * <li>Mas liviano en recursos
 * </ul>
 * <h2>Casos de Uso Recomendados:</h2>
 * <h3>JavaFX:</h3>
 * <ul>
 * <li>Aplicaciones modernas
 * <li>Interfaces complejas
 * <li>Animaciones y efectos visuales
 * <li>Aplicaciones multimedia
 * <li>Visualizacion de datos
 * </ul>
 * <h3>API Java:</h3>
 * <ul>
 * <li>Aplicaciones legacy
 * <li>Interfaces simples
 * <li>Formularios basicos
 * <li>Compatibilidad con sistemas antiguos
 * </ul>
 * <p>
 * Links:
 * <a href="https://foojay.io/today/high-performance-rendering-in-javafx/">High Performance Rendering in JavaFX</a>
 * <a href="https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-architecture.htm">Understanding the JavaFX Architecture</a>
 * <a href="https://docs.oracle.com/javafx/2/system_requirements_2-2-3/jfxpub-system_requirements_2-2-3.htm">JavaFX 2.2.3 System Requirements</a>
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html">Passive vs. Active Rendering</a>
 * <a href="https://www.oracle.com/java/technologies/painting.html">Painting in AWT and Swing</a>
 * <a href="https://stackoverflow.com/questions/71471546/how-to-make-a-smooth-time-based-animation-with-javafx-animationtimer">How to make a smooth time-based animation with JavaFx AnimationTimer?</a>
 */

public class Game extends AnimationTimer implements IGame {

    private final Scene scene;
    private final GameController gameController;
    private final Canvas canvas;
    /**
     * Esta clase se utiliza para emitir llamadas de dibujo a un Canvas usando un buffer.
     * <p>
     * Cada llamada envia los parametros necesarios al buffer donde luego seran renderizados en la imagen del nodo Canvas por el
     * hilo de renderizado al final de un pulso.
     */
    private final GraphicsContext context;
    public GameSystem gameSystem;
    long lastTime = System.nanoTime();
    private int frames, fps;
    private boolean paused, running;

    public Game(Scene scene, GameController gameController, Canvas canvas, GraphicsContext context) {
        this.scene = scene;
        this.gameController = gameController;
        this.canvas = canvas;
        this.context = context;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        paused = false;
        // Agrega listeners para eventos de ventana
        scene.getWindow().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) pause(); // Si la ventana pierde el foco
            else play(); // Si la ventana recupera el foco
        });
        /* Inicia el AnimationTimer. Una vez iniciado, se llamara al metodo handle(long) de este AnimationTimer en cada frame. */
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
        paused = false;
        gameSystem.audio.shutdown();
    }

    public void pause() {
        if (!paused) paused = true;
    }

    public void play() {
        if (paused) paused = false;
    }

    public void reset(boolean fullReset) {
        gameSystem.reset(fullReset);
    }

    public void setup() {
        gameSystem = GameSystem.createDefault(this);
        gameSystem.audio.playMusic(AudioID.Music.MAIN);
        // Al cargar la fuente todo el tiempo desde el render de la UI se generaba lag por lo tanto se carga una sola vez desde aca
        context.setFont(Utils.loadFont("font/BlackPearl.ttf", 18));
        context.setFill(Color.WHITE);
        gameSystem.initialize();
        gameController.initialize(this);
    }

    /**
     * Ciclo del juego.
     * <p>
     * Un ciclo completo del juego (game loop) incluye tanto la logica (tick) como el renderizado.
     * <p>
     * Este metodo se llamara en cada frame mientras AnimationTimer este activo.
     * <p>
     * FIXME Al principio se muestra 0 FPS, siempre y cuando se active el debug al mismo tiempo que se inicia el juego
     *
     * @param now tiempo actual en nanosegundos (proporciona JavaFX).
     */
    @Override
    public void handle(long now) {

        if (!paused) {

            long cycleStart = System.nanoTime();

            prepare();
            update();
            render();

            double cycleTime = (double) (System.nanoTime() - cycleStart) / 1_000_000;

            frames++;

            // Calcula la cantidad de frames por segundo
            if (now - lastTime >= 1_000_000_000) {
                fps = frames;
                frames = 0;
                lastTime = now;
            }

            gameController.showDebugInfo(fps, cycleTime);

        }

    }

    @Override
    public GameSystem getGameSystem() {
        return gameSystem;
    }

    @Override
    public GraphicsContext getContext() {
        return context;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public GameController getGameController() {
        return gameController;
    }

    private void prepare() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void update() {
        gameSystem.updater.update();
    }

    /**
     * El rendering se sincroniza con la tasa de refresco del monitor (60Hz, 144Hz, etc.).
     */
    private void render() {
        gameSystem.renderer.render(context);
    }

}
