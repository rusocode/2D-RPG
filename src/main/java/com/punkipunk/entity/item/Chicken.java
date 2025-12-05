package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Chicken extends Item {

    public Chicken(IGame game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        game.getGameSystem().audio.playSound(AudioID.Sound.EAT);
        entity.stats.fullHp();
        entity.stats.fullMana();
        return true;
    }

    @Override
    public ItemID getID() {
        return ItemID.CHICKEN;
    }

}


