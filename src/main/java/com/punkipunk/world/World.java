package com.punkipunk.world;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.EntitySystem;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.world.system.CutsceneSystem;
import com.punkipunk.world.system.EnvironmentSystem;
import com.punkipunk.world.system.TileSystem;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

public class World {

    private final IGame game;
    private final TileSystem tileSystem;
    public Map map;
    public EntitySystem entitySystem;
    public EnvironmentSystem environmentSystem;
    public CutsceneSystem cutsceneSystem;

    public World(IGame game) {
        this.game = game;
        this.map = new Map();
        this.tileSystem = new TileSystem(this);
        this.entitySystem = new EntitySystem(game, this);
        this.environmentSystem = new EnvironmentSystem(this);
        this.cutsceneSystem = new CutsceneSystem(game, this);
    }

    public void update() {
        entitySystem.update();
        environmentSystem.update();
    }

    public void render(Renderer2D renderer) {
        tileSystem.render(renderer);
        entitySystem.render(renderer);
        environmentSystem.render(renderer);
        cutsceneSystem.render();

        // Dibuja el recorrido del pathfinding
        if (false) {
            renderer.setFill(new Color(1, 0, 0, 0.3));
            for (int i = 0; i < game.getGameSystem().pathfinding.pathList.size(); i++) {
                int x = game.getGameSystem().pathfinding.pathList.get(i).col * tile;
                int y = game.getGameSystem().pathfinding.pathList.get(i).row * tile;
                int screenX = x - entitySystem.player.position.x + X_OFFSET;
                int screenY = y - entitySystem.player.position.y + Y_OFFSET;
                renderer.fillRect(screenX, screenY, tile, tile);
            }
        }

    }

}
