package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneAxe extends Item {

    public static final String NAME = "Stone Axe";

    public StoneAxe(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.stoneAxe", ItemConfig.class), pos);
    }

}
