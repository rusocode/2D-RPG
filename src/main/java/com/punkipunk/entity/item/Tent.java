package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

public class Tent extends Item {

    public Tent(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        State.setState(State.SLEEP);
        game.gameSystem.audio.playSound(AudioID.Sound.SLEEP);
        entity.stats.fullHp();
        entity.stats.fullMana();
        world.entitySystem.player.initSleepImage(sheet.frame);
        return true;
    }

    @Override
    public ItemID getID() {
        return ItemID.TENT;
    }

}
