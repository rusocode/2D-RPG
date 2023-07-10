package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.gfx.Assets.*;

public class Gold extends Item {

    public static final String NAME = "Gold";

    public Gold(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.PICKUP;
        image = Utils.scaleImage(item_gold, 32, 32);
        value = 1;
    }

    public boolean use(Entity entity) {
        game.playSound(sound_gold);
        game.ui.addMessage("Gold +" + value);
        entity.gold += value;
        return true;
    }

}
