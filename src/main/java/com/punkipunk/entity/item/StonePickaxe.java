package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StonePickaxe extends Item {

    public static final String NAME = "Stone Pickaxe";

    public StonePickaxe(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.stonePickaxe", ItemData.class), pos);
        itemType = ItemType.PICKAXE;
    }

}
