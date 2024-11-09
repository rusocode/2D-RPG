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

public class PotionRed extends Item {

    public static final String NAME = "Red Potion";

    /**
     * Create the item in the inventory or in the World (use varargs to specify the position).
     */
    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        points = 2;
        description = "[" + stats.name + "]\nHeals your life by " + points + ".";
        price = 2;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.POTION_RED), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.hp != entity.stats.maxHp) {
            game.playSound(Assets.getAudio(AudioAssets.DRINK_POTION));
            entity.stats.hp += points;
            if (entity.stats.hp > entity.stats.maxHp) entity.stats.hp = entity.stats.maxHp;
            return true;
        } else {
            game.system.ui.addMessageToConsole("You have a full life");
            return false;
        }
    }

}
