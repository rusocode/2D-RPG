package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class PotionRed extends Item {

    public static final String NAME = "Red Potion";

    /**
     * Usa varargs (int... pos) para especificar la posicion o no, evitando asi crear dos constructores.
     */
    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.potionRed", ItemConfig.class), pos);
        this.amount = amount;
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.hp != entity.stats.maxHp) {
            game.system.audio.playSound(AudioID.Sound.DRINK_POTION);
            entity.stats.hp += points;
            if (entity.stats.hp > entity.stats.maxHp) entity.stats.hp = entity.stats.maxHp;
            return true;
        } else {
            game.system.ui.addMessageToConsole("You have a full life");
            return false;
        }
    }

}
