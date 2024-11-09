package com.punkipunk.engine.core;

import com.punkipunk.Game;
import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.ai.AStar;
import com.punkipunk.audio.Audio;
import com.punkipunk.audio.AudioManager;
import com.punkipunk.input.keyboard.KeyboardHandler;
import com.punkipunk.io.File;
import com.punkipunk.physics.Collision;
import com.punkipunk.physics.Event;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.item.ItemGenerator;

/**
 * <p>
 * Sistemas del juego.
 * <p>
 * Con el <b>Builder Pattern</b> se puede elegir que sistemas crear y en que orden, teniendo la posibilidad de una construccion
 * mas flexible:
 * <pre>{@code
 * system = new System.Builder(this)
 *             .withWorld()
 *             .withFile()
 *             .withSound()
 *             .withCollision()
 *             .build();
 * }</pre>
 * <p>
 * Los campos en System son finales y solo pueden ser establecidos durante la construccion (inmutabilidad). Se proporciona un
 * metodo {@code createDefault()} como metodo de fabrica para casos de uso comun, manteniendo la flexibilidad del builder para
 * casos especiales. El constructor de System es privado, forzando el uso del builder.
 */

public class System {

    // Input Systems
    public final KeyboardHandler keyboard;

    // Core Systems
    public final World world;
    public final Collision collision;
    public final AStar aStar;

    // UI Systems
    public final UI ui;
    public final Minimap minimap;

    // Game Systems
    public final Event event;
    public final ItemGenerator itemGenerator;

    // Audio Systems
    public final Audio music;
    public final Audio ambient;
    public final Audio sound;
    public final AudioManager audioManager;

    // File System
    public final File file;

    // Game Loop Systems
    public final Updater updater;
    public final Renderer renderer;
    public final OldGameLoop oldGameLoop;

    private System(Builder builder) {
        this.keyboard = builder.keyboard;
        this.world = builder.world;
        this.collision = builder.collision;
        this.aStar = builder.aStar;
        this.ui = builder.ui;
        this.minimap = builder.minimap;
        this.event = builder.event;
        this.itemGenerator = builder.itemGenerator;
        this.music = builder.music;
        this.ambient = builder.ambient;
        this.sound = builder.sound;
        this.audioManager = builder.audioManager;
        this.file = builder.file;
        this.updater = builder.updater;
        this.renderer = builder.renderer;
        this.oldGameLoop = builder.oldGameLoop;
    }

    /**
     * Metodo de fabrica para crear el sistema con la configuracion completa por defecto.
     *
     * @param game juego.
     * @return el sistema con la configuracion completa por defecto.
     */
    public static System createDefault(Game game) {
        return new Builder(game)
                .withUI()
                .withMinimap()
                .withCollision()
                .withAStar()
                .withEvent()
                .withItemGenerator()
                .withInputHanlder()
                .withAudio()
                .withFile()
                .withUpdater()
                .withRenderer()
                .withOldGameLoop()
                .build();
    }

    public void initialize() {
        if (file != null) file.load();
        if (minimap != null) minimap.create();
        if (event != null) event.create();
    }

    public void reset(boolean fullReset) {
        ui.console.clear();
        world.entities.player.reset(fullReset);
        world.entities.factory.createMobs();
        world.entities.removeTempEntities();
        world.entities.player.bossBattleOn = false;
        if (fullReset) {
            world.entities.factory.createEntities();
            world.environment.lighting.resetDay();
            keyboard.resetToggledKeys();
        }
    }

    public static class Builder {
        private final Game game;
        private final World world;
        // All systems are private in the builder
        private KeyboardHandler keyboard;
        private Collision collision;
        private AStar aStar;
        private UI ui;
        private Minimap minimap;
        private Event event;
        private ItemGenerator itemGenerator;
        private Audio music;
        private Audio ambient;
        private Audio sound;
        private AudioManager audioManager;
        private File file;
        private Updater updater;
        private Renderer renderer;
        private OldGameLoop oldGameLoop;

        public Builder(Game game) {
            this.game = game;
            this.world = new World(game);
        }

        public Builder withUI() {
            this.ui = new UI(game, world);
            return this;
        }

        public Builder withMinimap() {
            this.minimap = new Minimap(game, world);
            return this;
        }

        public Builder withCollision() { // TODO Deberia llamarse Collider?
            this.collision = new Collision(world);
            return this;
        }

        public Builder withAStar() { // TODO Deberia llamarse Pathfinding?
            this.aStar = new AStar(world);
            return this;
        }

        public Builder withEvent() { // TODO Deberia llamarse EventHandler?
            this.event = new Event(game, world);
            return this;
        }

        public Builder withItemGenerator() { // TODO Deberia llamarse EventHandler?
            this.itemGenerator = new ItemGenerator(game, world);
            return this;
        }

        public Builder withInputHanlder() {
            this.keyboard = new KeyboardHandler(game);
            return this;
        }

        public Builder withAudio() {
            // TODO No es mejor mover music, ambient y sound a AudioManager?
            this.music = new Audio();
            this.ambient = new Audio();
            this.sound = new Audio();
            this.audioManager = new AudioManager(game);
            return this;
        }

        public Builder withFile() { // TODO FileHandler?
            this.file = new File(game, world);
            return this;
        }

        public Builder withUpdater() {
            this.updater = new Updater(world, game);
            return this;
        }

        public Builder withRenderer() {
            if (ui == null || minimap == null)
                throw new IllegalStateException("UI systems must be initialized first using withUISystem()");
            this.renderer = new Renderer(game, world, ui, minimap);
            return this;
        }

        public Builder withOldGameLoop() {
            this.oldGameLoop = new OldGameLoop();
            return this;
        }

        public System build() {
            return new System(this);
        }

    }
}