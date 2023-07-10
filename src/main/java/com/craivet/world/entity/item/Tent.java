package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

public class Tent extends Item {

    public static final String NAME = "Tent";

    public Tent(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.CONSUMABLE;
        image = Utils.scaleImage(item_tent, tile_size, tile_size);
        description = "[" + name + "]\nYou can sleep until\nnext morning.";
        price = 1200;
    }

    @Override
    public boolean use(Entity entity) {
        game.state = SLEEP_STATE;
        game.playSound(sound_sleep);
        world.player.hp = world.player.maxHp;
        world.player.mana = world.player.maxMana;
        world.player.initSleepImage(image);
        return true;
    }

}
