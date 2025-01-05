package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Lantern extends Item {

    public static final String NAME = "Lantern";

    public Lantern(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.latern", ItemData.class), pos);
        itemType = ItemType.LIGHT;
    }

}
