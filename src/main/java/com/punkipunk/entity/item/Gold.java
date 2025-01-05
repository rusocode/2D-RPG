package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Gold extends Item {

    public static final String NAME = "Gold";

    public Gold(Game game, World world, int amount, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.gold", ItemData.class), pos);
        itemType = ItemType.PICKUP;
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        game.system.audio.playSound(AudioID.Sound.GOLD_PICKUP);
        game.system.ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

}
