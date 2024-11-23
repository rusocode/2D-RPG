package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

public class PotionBlue extends Item {

    public static final String NAME = "Blue Potion";

    public PotionBlue(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        points = 2;
        description = "[" + stats.name + "]\nIncrease your mana \nby " + points + ".";
        price = 4;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.POTION_BLUE), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.mana != entity.stats.maxMana) {
            game.system.audio.playSound(Assets.getAudio(AudioAssets.DRINK_POTION));
            entity.stats.mana += points;
            if (entity.stats.mana > entity.stats.maxMana) entity.stats.mana = entity.stats.maxMana;
            return true;
        } else {
            game.system.ui.addMessageToConsole("You have a full mana");
            return false;
        }
    }

}
