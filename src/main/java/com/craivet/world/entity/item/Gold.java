package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class Gold extends Item {

    public static final String NAME = "Gold";

    public Gold(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.PICKUP;
        stats.name = NAME;
        this.amount = amount;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.GOLD), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.playSound(Assets.getAudio(AudioAssets.Type.GOLD_PICKUP));
        game.ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

}
