package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneSword extends Item {

    public static final String NAME = "Stone Sword";

    public StoneSword(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.stoneSword", ItemConfig.class), pos);
    }

}
