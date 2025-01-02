package com.punkipunk.entity.item;

import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StonePickaxe extends Item {

    public static final String NAME = "Stone Pickaxe";

    public StonePickaxe(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.stonePickaxe", ItemConfig.class), pos);
    }

}
