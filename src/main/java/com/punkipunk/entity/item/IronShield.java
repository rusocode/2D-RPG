package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class IronShield extends Item {

    public static final String NAME = "Iron Shield";

    public IronShield(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.ironShield", ItemData.class), pos);
        itemType = ItemType.SHIELD;
    }

}
