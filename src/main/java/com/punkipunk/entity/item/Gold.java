package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Gold extends Item {

    public Gold(Game game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        game.gameSystem.audio.playSound(AudioID.Sound.GOLD_PICKUP);
        game.gameSystem.ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

    @Override
    public ItemID getID() {
        return ItemID.GOLD;
    }

}
