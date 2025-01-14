package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

public class Tent extends Item {

    public static final String NAME = "Tent";

    public Tent(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        State.setState(State.SLEEP);
        game.system.audio.playSound(AudioID.Sound.SLEEP);
        entity.stats.fullHp();
        entity.stats.fullMana();
        world.entities.player.initSleepImage(sheet.frame);
        return true;
    }

    @Override
    protected ItemType getType() {
        return ItemType.TENT;
    }

}
