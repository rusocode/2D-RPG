package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class PotionBlue extends Item {

    public static final String NAME = "Blue Potion";

    public PotionBlue(Game game, World world, int amount, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.potionBlue", ItemData.class), pos);
        itemType = ItemType.USABLE;
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.mana != entity.stats.maxMana) {
            game.system.audio.playSound(AudioID.Sound.DRINK_POTION);
            entity.stats.mana += points;
            if (entity.stats.mana > entity.stats.maxMana) entity.stats.mana = entity.stats.maxMana;
            return true;
        } else {
            game.system.ui.addMessageToConsole("You have a full mana");
            return false;
        }
    }

}
