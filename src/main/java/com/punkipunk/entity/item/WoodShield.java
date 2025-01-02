package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class WoodShield extends Item {

    public static final String NAME = "Wood Shield";

    public WoodShield(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.woodShield", ItemConfig.class), pos);
    }

}
