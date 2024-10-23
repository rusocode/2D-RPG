package com.punkipunk.engine.core;

import com.punkipunk.Game;
import com.punkipunk.controllers.StatsController;
import com.punkipunk.engine.core.api.Updatable;
import com.punkipunk.world.World;

public class Updater implements Updatable {

    private final Game game;
    private final World world;

    private StatsController statsController;

    public Updater(World world, Game game) {
        this.game = game;
        this.world = world;
    }

    @Override
    public void update() {
        world.update();
        updateStats();
    }

    public void setStatsController(StatsController statsController) {
        this.statsController = statsController;
    }

    // TODO Tendria que ir aca?
    private void updateStats() {
        statsController.update(game.systems.renderer.world.entities.player.stats);
    }

}
