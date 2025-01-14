package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneSword extends Item {

    public static final String NAME = "Stone Sword";

    public StoneSword(Game game, World world, int... pos) {
        super(game, world, pos);
        sound = AudioID.Sound.DRAW_SWORD;
    }

    @Override
    protected ItemType getType() {
        return ItemType.STONE_SWORD;
    }

}
