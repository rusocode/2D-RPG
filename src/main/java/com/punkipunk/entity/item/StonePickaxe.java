package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.world.World;

public class StonePickaxe extends Item {

    public StonePickaxe(IGame game, World world, int... pos) {
        super(game, world, pos);
        soundSwing = AudioID.Sound.SWING_AXE;
        soundDraw = AudioID.Sound.DRAW_PICKAXE;
    }

    @Override
    public ItemID getID() {
        return ItemID.STONE_PICKAXE;
    }

}
