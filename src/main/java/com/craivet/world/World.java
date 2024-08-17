package com.craivet.world;

import com.craivet.Game;
import com.craivet.states.State;
import com.craivet.world.management.*;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * The World class represents the scenery of the game. Each part of the world is divided into 50x50 tile maps that are
 * composed of tiles, entities and an environment (weather).
 */

public class World {

    private final Game game;

    public Map map;

    private final TileManager tiles;
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
        if (State.isState(State.MAIN)) environment.update();
    }

    /**
     * Renders the tiles, entities and the environment.
     *
     * @param g2 graphic component.
     */
    public void render(Graphics2D g2) {
        // Render tiles, entities, and weather only if the game is other than MAIN_STATE
        if (!State.isState(State.MAIN)) {
            tiles.render(g2);
            entities.render(g2);
            environment.render(g2);
            cutscene.render();
        }
        if (State.isState(State.MAIN)) {
            // TODO Replace with a background with an image
            g2.setColor(Color.black);
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }

        // Dibuja el recorrido del pathfinding
        if (false) {
            g2.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < game.aStar.pathList.size(); i++) {
                int x = game.aStar.pathList.get(i).col * tile;
                int y = game.aStar.pathList.get(i).row * tile;
                int screenX = x - entities.player.pos.x + entities.player.screen.xOffset;
                int screenY = y - entities.player.pos.y + entities.player.screen.yOffset;
                g2.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

}
