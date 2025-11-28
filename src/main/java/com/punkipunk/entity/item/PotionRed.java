package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class PotionRed extends Item {

    /**
     * Usa varargs (int... pos) para especificar la posicion o no, evitando asi crear dos constructores.
     */
    public PotionRed(IGame game, World world, int amount, int... pos) {
        super(game, world, pos);
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.hp < entity.stats.maxHp) {
            game.getGameSystem().audio.playSound(AudioID.Sound.DRINK_POTION);
            entity.stats.increaseHp(points);
            return true;
        } else {
            game.getGameSystem().ui.addMessageToConsole("You have a full life!");
            return false;
        }
    }

    @Override
    public ItemID getID() {
        return ItemID.POTION_RED;
    }

}
