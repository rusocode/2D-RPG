package com.punkipunk.world;

import com.punkipunk.core.Game;
import com.punkipunk.entity.EntityFactory;
import com.punkipunk.entity.EntityManager;
import com.punkipunk.states.State;
import com.punkipunk.world.management.CutsceneManager;
import com.punkipunk.world.management.EnvironmentManager;
import com.punkipunk.world.management.TileManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * Representa el escenario del juego. Cada parte del mundo esta dividida en mapas de 50x50 tiles que se componen de tiles,
 * entidades y un entorno (clima).
 */

public class World {

    private final Game game;
    private final TileManager tiles;
    public Map map;
    public EntityManager entities;
    public EntityFactory entityFactory;
    public EnvironmentManager environment;
    public CutsceneManager cutscene;

    public World(Game game) {
        this.game = game;
        map = new Map(game);
        tiles = new TileManager(this);

        entities = new EntityManager(game, this);
        entityFactory = new EntityFactory(game, this, entities);
        entityFactory.createEntities();

        environment = new EnvironmentManager(this);
        cutscene = new CutsceneManager(game, this);
    }

    /**
     * Updates the entities and environment.
     */
    public void update() {
        if (State.isState(State.PLAY)) entities.update();
        if (State.isState(State.INVENTORY) || State.isState(State.PLAY)) environment.update();
    }

    /**
     * Renders the tiles, entities and the environment.
     *
     * @param context graphic context.
     */
    public void render(GraphicsContext context) {
        // Render tiles, entities, and weather only if the game is other than MAIN_STATE
        if (!State.isState(State.MAIN)) {
            tiles.render(context);
            entities.render(context);
            environment.render(context);
            cutscene.render();
        }
        if (State.isState(State.MAIN)) {
            // TODO Replace with a background with an image
            context.setFill(Color.BLACK);
            context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        // Dibuja el recorrido del pathfinding
        if (false) {
            context.setFill(new Color(1, 0, 0, 0.3));
            for (int i = 0; i < game.system.aStar.pathList.size(); i++) {
                int x = game.system.aStar.pathList.get(i).col * tile;
                int y = game.system.aStar.pathList.get(i).row * tile;
                int screenX = x - entities.player.position.x + X_OFFSET;
                int screenY = y - entities.player.position.y + Y_OFFSET;
                context.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

}
