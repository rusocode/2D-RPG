package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Wood extends Item {

    public static final String NAME = "Wood";

    public Wood(Game game, World world, int amount, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.wood", ItemConfig.class), pos);
        this.amount = amount;
    }

}