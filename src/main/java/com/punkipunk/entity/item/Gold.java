package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Gold extends Item {

    public Gold(IGame game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        game.getGameSystem().audio.playSound(AudioID.Sound.GOLD_PICKUP);
        game.getGameSystem().ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

    @Override
    public ItemID getID() {
        return ItemID.GOLD;
    }

}
