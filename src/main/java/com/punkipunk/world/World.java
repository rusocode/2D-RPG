package com.punkipunk.world;

import com.punkipunk.core.Game;
import com.punkipunk.entity.EntityFactory;
import com.punkipunk.entity.EntitySystem;
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
    public EntitySystem entitySystem;
    public EntityFactory entityFactory;
    public EnvironmentManager environment;
    public CutsceneManager cutscene;

    public World(Game game) {
        this.game = game;
        map = new Map(game);
        tiles = new TileManager(this);

        entitySystem = new EntitySystem(game, this);
        entityFactory = new EntityFactory(game, this, entitySystem);
        entityFactory.createEntities();

        environment = new EnvironmentManager(this);
        cutscene = new CutsceneManager(game, this);
    }

    /**
     * Updates the entities and environment.
     */
    public void update() {
        if (State.isState(State.PLAY)) entitySystem.update();
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
            entitySystem.render(context);
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
            for (int i = 0; i < game.gameSystem.aStar.pathList.size(); i++) {
                int x = game.gameSystem.aStar.pathList.get(i).col * tile;
                int y = game.gameSystem.aStar.pathList.get(i).row * tile;
                int screenX = x - entitySystem.player.position.x + X_OFFSET;
                int screenY = y - entitySystem.player.position.y + Y_OFFSET;
                context.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

}
