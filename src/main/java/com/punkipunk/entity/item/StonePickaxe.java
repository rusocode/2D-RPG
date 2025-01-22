package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StonePickaxe extends Item {

    public StonePickaxe(Game game, World world, int... pos) {
        super(game, world, pos);
        soundDraw = AudioID.Sound.DRAW_PICKAXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.STONE_PICKAXE;
    }

}
