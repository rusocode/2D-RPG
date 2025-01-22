package com.punkipunk.core;

import com.punkipunk.core.api.Updatable;
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
        updateStats(); // TODO Hace falta llamar a cada rato a esto?
    }

    private void updateStats() {
        game.getGameController().getStatsViewController().update(game.gameSystem.world.entitySystem.player.stats);
    }

}
