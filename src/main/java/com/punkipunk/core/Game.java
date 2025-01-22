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
 * <h1>Canvas y Sistemas de Renderizado en Java</h1>
 * <h2>Canvas</h2>
 * <p>
 * El Canvas es un componente fundamental en el desarrollo de interfaces graficas que funciona como un lienzo digital rectangular
 * donde los desarrolladores pueden dibujar y manipular graficos 2D de forma programatica. Este elemento proporciona un espacio en
 * blanco versatil que permite la creacion de graficos personalizados mediante una serie de metodos de dibujo, ofreciendo control
 * total sobre cada pixel del area definida. En el Canvas, puedes dibujar formas geometricas basicas como lineas, rectangulos,
 * circulos y curvas, asi como texto y figuras mas complejas. Tambien permite la manipulacion de colores, la aplicacion de
 * gradientes, el manejo de transparencias y la realizacion de transformaciones geometricas como rotaciones, escalados y
 * traslaciones. El Canvas es especialmente util para crear visualizaciones de datos, graficos personalizados, firmas digitales,
 * editores de imagenes simples y juegos 2D. Ademas, el Canvas permite la interaccion con eventos del mouse y del teclado, lo que
 * posibilita la creacion de aplicaciones interactivas donde los usuarios pueden dibujar o modificar el contenido del lienzo en
 * tiempo real. Una caracteristica importante del Canvas es que mantiene un sistema de coordenadas cartesianas donde el origen
 * (0,0) se encuentra en la esquina superior izquierda, y los valores positivos de x se extienden hacia la derecha y los valores
 * positivos de y hacia abajo, lo que facilita el posicionamiento preciso de elementos graficos en el lienzo.
 * <h2>Diferencia entre JavaFX y la API tradicional de Java (AWT/Swing)</h2>
 * <h3>JavaFX</h3>
 * Framework moderno para interfaces graficas con las siguientes caracteristicas:
 * <ul>
 * <li><b>Scene Graph:</b> Estructura jerarquica de nodos para UI
 * <li><b>Aceleracion por Hardware:</b> Renderizado por hardware mediante el motor <b>Prism</b>
 * <li><b>Styling:</b> Soporte nativo para CSS y FXML
 * <li><b>Data Binding:</b> Sistema de propiedades y bindings
 * <li><b>Efectos:</b> Animaciones y efectos visuales avanzados
 * </ul>
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
 * Ventajas:
 * <ul>
 * <li>Rendimiento optimizado con aceleracion por GPU
 * <li>API moderna y declarativa
 * <li>Mejor manejo de animaciones
 * <li>Separacion de logica y presentacion
 * <li>Herramientas modernas como Scene Builder
 * </ul>
 * <h3>API Java (AWT/Swing)</h3>
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
 * TODO ¿Como quedaria el bucle del juego si quiero que el jugador se renderice tanto como sea posible, pero que las
 * actualizaciones de la fisica se mantengan fijas?
 * <p>
 * Links:
 * <a href="https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html">Passive vs. Active Rendering</a>
 * <a href="https://www.oracle.com/java/technologies/painting.html">Painting in AWT and Swing</a>
 * <a href="https://stackoverflow.com/questions/71471546/how-to-make-a-smooth-time-based-animation-with-javafx-animationtimer">How to make a smooth time-based animation with JavaFx AnimationTimer?</a>
 */

public class Game extends AnimationTimer {

    private final Scene scene;
    private final GameController gameController;
    private final Canvas canvas;
    private final GraphicsContext context;
    public GameSystem gameSystem;
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
        // Inicia el bucle del juego
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
        paused = false;
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
     *
     * @param now tiempo actual en nanosegundos (proporciona JavaFX).
     */
    @Override
    public void handle(long now) {
        if (!paused) {
            prepare();
            update();
            render();
        }
    }

    /**
     * No es necesario "limpiar" el canvas antes de renderizar cada frame, porque el {@code TileManager} dibuja todos los tiles
     * del mapa visibles en la camara. El {@code EntityManager} dibuja todas las entidades ordenadas por profundidad.
     * <p>
     * Como en cada frame se redibuja todo el juego completo, no habria diferencia visual si limpiamos o no el canvas. Cada pixel
     * sera sobrescrito de todas formas.
     * <p>
     * Sin embargo, es una buena practica mantener el {@code clearRect()} porque asegura que no queden artefactos visuales si algo
     * falla en el renderizado, hace el codigo mas robusto ante futuros cambios y es mas explicito sobre lo que esta ocurriendo en
     * cada frame.
     */
    private void prepare() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void update() {
        gameSystem.updater.update();
    }

    /**
     * El rendering (frames) se sincroniza con la tasa de refresco del monitor (60Hz, 144Hz, etc.)
     */
    private void render() {
        gameSystem.renderer.render(context);
    }

    public GraphicsContext getContext() {
        return context;
    }

    public Scene getScene() {
        return scene;
    }

    public GameController getGameController() {
        return gameController;
    }

}
