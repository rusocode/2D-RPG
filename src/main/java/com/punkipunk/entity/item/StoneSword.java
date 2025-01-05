package com.punkipunk.entity.item;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class StoneSword extends Item {

    public static final String NAME = "Stone Sword";

    public StoneSword(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.stoneSword", ItemData.class), pos);
        itemType = ItemType.SWORD;
    }

}
