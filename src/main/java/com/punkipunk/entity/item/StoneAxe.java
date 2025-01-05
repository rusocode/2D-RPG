package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneAxe extends Item {

    public static final String NAME = "Stone Axe";

    public StoneAxe(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.stoneAxe", ItemData.class), pos);
        itemType = ItemType.AXE;
    }

}
