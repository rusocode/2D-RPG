package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneSword extends Item {

    public StoneSword(Game game, World world, int... pos) {
        super(game, world, pos);
        soundDraw = AudioID.Sound.DRAW_SWORD;
        soundSwing = AudioID.Sound.SWING_WEAPON;
    }

    @Override
    public ItemType getType() {
        return ItemType.STONE_SWORD;
    }

}
