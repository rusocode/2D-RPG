package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneAxe extends Item {

    public StoneAxe(Game game, World world, int... pos) {
        super(game, world, pos);
        soundSwing = AudioID.Sound.SWING_AXE;
    }

    @Override
    public ItemID getID() {
        return ItemID.STONE_AXE;
    }

}
