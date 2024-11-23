package com.punkipunk.engine.core;

import com.punkipunk.Game;
import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.ai.AStar;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.audio.AudioController;
import com.punkipunk.input.keyboard.Keyboard;
import com.punkipunk.io.File;
import com.punkipunk.physics.CollisionChecker;
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

    public final World world;
    public final UI ui;
    public final Minimap minimap;
    public final Keyboard keyboard;
    public final CollisionChecker collisionChecker;
    public final AStar aStar;
    public final Event event;
    public final ItemGenerator itemGenerator;
    public final AudioController audio;
    public final File file;
    public final Updater updater;
    public final Renderer renderer;
    public final OldGameLoop oldGameLoop;

    private System(Builder builder) {
        this.world = builder.world;
        this.ui = builder.ui;
        this.minimap = builder.minimap;
        this.keyboard = builder.keyboard;
        this.collisionChecker = builder.collisionChecker;
        this.aStar = builder.aStar;
        this.event = builder.event;
        this.itemGenerator = builder.itemGenerator;
        this.audio = builder.audio;
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
                .withKeyboard()
                .withCollisionChecker()
                .withAStar()
                .withEvent()
                .withItemGenerator()
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
    }

    public void reset(boolean fullReset) {
        ui.console.clear();
        world.entities.player.reset(fullReset);
        world.entities.factory.createMobs();
        world.entities.removeTempEntities();
        world.entities.player.bossBattleOn = false;
        audio.playAmbient(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
        if (fullReset) {
            audio.playMusic(Assets.getAudio(AudioAssets.MUSIC_MAIN));
            world.entities.factory.createEntities();
            world.environment.lighting.resetDay();
            keyboard.resetToggledKeys();
        }
    }

    public static class Builder {
        private final Game game;
        private final World world;
        private UI ui;
        private Minimap minimap;
        private Keyboard keyboard;
        private CollisionChecker collisionChecker;
        private AStar aStar;
        private Event event;
        private ItemGenerator itemGenerator;
        private AudioController audio;
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

        public Builder withKeyboard() {
            this.keyboard = new Keyboard(game);
            return this;
        }

        public Builder withCollisionChecker() {
            this.collisionChecker = new CollisionChecker(world);
            return this;
        }

        public Builder withAStar() { // TODO Deberia llamarse Pathfinding?
            this.aStar = new AStar(world);
            return this;
        }

        public Builder withEvent() {
            this.event = new Event(game, world);
            return this;
        }

        public Builder withItemGenerator() {
            this.itemGenerator = new ItemGenerator(game, world);
            return this;
        }

        public Builder withAudio() {
            this.audio = new AudioController(game);
            return this;
        }

        public Builder withFile() {
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