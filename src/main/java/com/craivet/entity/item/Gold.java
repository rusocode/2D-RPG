package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Gold extends Item {

    public static final String item_name = "Gold";

    public Gold(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = item_name;
        type = TYPE_PICKUP_ONLY;
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
