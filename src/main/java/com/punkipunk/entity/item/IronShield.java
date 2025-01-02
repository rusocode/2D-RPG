package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class IronShield extends Item {

    public static final String NAME = "Iron Shield";

    public IronShield(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.ironShield", ItemConfig.class), pos);
    }

}
