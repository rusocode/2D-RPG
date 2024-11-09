package com.punkipunk.engine.core;

import com.punkipunk.Game;
import com.punkipunk.engine.core.api.Updatable;
import com.punkipunk.world.World;

public class Updater implements Updatable {

    private final Game game;
    private final World world;

    public Updater(World world, Game game) {
        this.game = game;
        this.world = world;
    }

    @Override
    public void update() {
        world.update();
        updateUI();
    }

    private void updateUI() {
        updateStats();
    }

    private void updateStats() {
        game.getGameController().getStatsViewController().update(game.system.renderer.world.entities.player.stats);
    }

}
