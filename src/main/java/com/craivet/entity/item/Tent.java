package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Tent extends Item {

    public static final String item_name = "Tent";

    public Tent(Game game, World world) {
        super(game, world);
        type = TYPE_CONSUMABLE;
        name = item_name;
        image = Utils.scaleImage(item_tent, tile_size, tile_size);
        description = "[" + name + "]\nYou can sleep until\nnext morning.";
        price = 1200;
    }

    public Tent(Game game, World world, int x, int y) {
        super(game, world);
        this.x = x * tile_size;
        this.y = y * tile_size;
        type = TYPE_CONSUMABLE;
        name = item_name;
        image = Utils.scaleImage(item_tent, tile_size, tile_size);
        description = "[" + name + "]\nYou can sleep until\nnext morning.";
        price = 1200;
    }

    @Override
    public boolean use(Entity entity) {
        game.state = SLEEP_STATE;
        game.playSound(sound_sleep);
        world.player.life = world.player.maxLife;
        world.player.mana = world.player.maxMana;
        world.player.initSleepImage(image);
        return true;
    }

}
