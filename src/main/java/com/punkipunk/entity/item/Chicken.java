package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Chicken extends Item {

    public static final String NAME = "Chicken";

    public Chicken(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.chicken", ItemData.class), pos);
        itemType = ItemType.USABLE;
    }

    @Override
    public boolean use(Entity entity) {
        game.system.audio.playSound(AudioID.Sound.EAT);
        entity.stats.fullHp();
        entity.stats.fullMana();
        return true;
    }

}


