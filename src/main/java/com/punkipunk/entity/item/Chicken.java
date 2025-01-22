package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Chicken extends Item {

    public Chicken(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        game.gameSystem.audio.playSound(AudioID.Sound.EAT);
        entity.stats.fullHp();
        entity.stats.fullMana();
        return true;
    }

    @Override
    public ItemType getType() {
        return ItemType.CHICKEN;
    }

}


