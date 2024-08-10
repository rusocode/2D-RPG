package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.assets.TextureAssets;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;

import static com.craivet.utils.Global.tile;

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
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.POTION_BLUE), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.mana != entity.stats.maxMana) {
            game.playSound(Assets.getAudio(AudioAssets.Type.DRINK_POTION));
            entity.stats.mana += points;
            if (entity.stats.mana > entity.stats.maxMana) entity.stats.mana = entity.stats.maxMana;
            return true;
        } else {
            game.ui.addMessageToConsole("You have a full mana");
            return false;
        }
    }

}
