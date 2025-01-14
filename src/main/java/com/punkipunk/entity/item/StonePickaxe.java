package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StonePickaxe extends Item {

    public static final String NAME = "Stone Pickaxe";

    public StonePickaxe(Game game, World world, int... pos) {
        super(game, world, pos);
        sound = AudioID.Sound.DRAW_PICKAXE;
    }

    @Override
    protected ItemType getType() {
        return ItemType.STONE_PICKAXE;
    }

}
