package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class StoneSword extends Item {

    public StoneSword(IGame game, World world, int... pos) {
        super(game, world, pos);
        soundDraw = AudioID.Sound.DRAW_SWORD;
        soundSwing = AudioID.Sound.SWING_SWORD;
    }

    @Override
    public ItemID getID() {
        return ItemID.STONE_SWORD;
    }

}
