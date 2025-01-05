package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class WoodShield extends Item {

    public static final String NAME = "Wood Shield";

    public WoodShield(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.woodShield", ItemData.class), pos);
        itemType = ItemType.SHIELD;
    }

}
