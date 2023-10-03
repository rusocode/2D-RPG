package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * To specify the texture of the item on the ground with a smaller size:
 * <pre>{@code
 * sheet.frame = pos.length > 0 ? Utils.scaleImage(key, tile / 2, tile / 2) : Utils.scaleImage(key, tile, tile);
 * }</pre>
 */

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIt opens a door.";
        price = 100;
        this.amount = amount;
        stackable = true;
        hitbox = new Rectangle(2, 0, 27, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.frame = Utils.scaleImage(key, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        // If the entity detects a door, then it can use the key
        int i = detect(entity, world.items, Door.NAME);
        if (i != -1) {
            game.playSound(sound_door_opening);
            world.items[world.map][i] = null;
            return true;
        }
        return false;
    }

}
