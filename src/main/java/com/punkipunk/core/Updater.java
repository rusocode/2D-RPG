package com.punkipunk.core;

import com.punkipunk.core.api.Updatable;
import com.punkipunk.world.World;

public record Updater(World world, Game game) implements Updatable {

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
