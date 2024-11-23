package com.punkipunk.world;

import com.punkipunk.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.management.CutsceneManager;
import com.punkipunk.world.management.EntityManager;
import com.punkipunk.world.management.EnvironmentManager;
import com.punkipunk.world.management.TileManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

/**
 * The World class represents the scenery of the game. Each part of the world is divided into 50x50 tile maps that are composed of
 * tiles, entities and an environment (weather).
 */

public class World {

    private final Game game;
    private final TileManager tiles;
    public Map map;
    public EntityManager entities;
    public EnvironmentManager environment;
    public CutsceneManager cutscene;

    public World(Game game) {
        this.game = game;
        map = new Map(game, this);
        tiles = new TileManager(this);
        entities = new EntityManager(game, this);
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
                int screenX = x - entities.player.pos.x + X_OFFSET;
                int screenY = y - entities.player.pos.y + Y_OFFSET;
                context.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

}
