package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Stone extends Item {

    public static final String NAME = "Stone";

    public Stone(Game game, World world, int amount, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.stone", ItemConfig.class), pos);
        this.amount = amount;
    }

}
