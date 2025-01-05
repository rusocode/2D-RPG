package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Boots extends Item {

    public static final String NAME = "Boots";

    public Boots(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.boots", ItemData.class), pos);
        itemType = ItemType.USABLE;
    }

}
