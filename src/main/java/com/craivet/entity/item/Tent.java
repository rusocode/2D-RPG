package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Constants.*;

public class Tent extends Item {

    public Tent(Game game, World world, int x, int y) {
        super(game, world);
        worldX = x * tile_size;
        worldY = y * tile_size;
        type = TYPE_CONSUMABLE;
        name = "Tent";
        image = Utils.scaleImage(item_tent, tile_size, tile_size);
        itemDescription = "[" + name + "]\nYou can sleep until\nnext morning.";
        price = 1200;
    }

    @Override
    public boolean use(Entity entity) {
        game.gameState = SLEEP_STATE;
        game.playSound(sound_sleep);
        world.player.life = world.player.maxLife;
        world.player.mana = world.player.maxMana;
        world.player.initSleepImage(image);
        return true;
    }

}
