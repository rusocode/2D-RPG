package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class PotionBlue extends Item {

    public PotionBlue(Game game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.mana < entity.stats.maxMana) {
            game.gameSystem.audio.playSound(AudioID.Sound.DRINK_POTION);
            entity.stats.increaseMana(points);
            return true;
        } else {
            game.gameSystem.ui.addMessageToConsole("You have a full mana!");
            return false;
        }
    }

    @Override
    public ItemID getID() {
        return ItemID.POTION_BLUE;
    }

}
