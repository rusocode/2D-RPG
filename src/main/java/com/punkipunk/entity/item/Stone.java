package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class Stone extends Item {

    public static final String NAME = "Stone";

    public Stone(Game game, World world, int amount, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.stone", ItemData.class), pos);
        itemType = ItemType.USABLE;
        this.amount = amount;
    }

}
