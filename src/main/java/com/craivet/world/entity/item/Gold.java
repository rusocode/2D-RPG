package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * One gold coin costs one gold coin XD
 */

public class Gold extends Item {

    public static final String NAME = "Gold";

    public Gold(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.PICKUP;
        stats.name = NAME;
        this.amount = amount;
        sheet.frame = Utils.scaleImage(Assets.gold, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.playSound(sound_gold_pickup);
        game.ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

}
