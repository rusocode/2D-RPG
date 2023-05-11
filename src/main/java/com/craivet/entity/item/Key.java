package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {

    public static final String item_name = "Key";

    public Key(Game game, World world, int amount) {
        super(game, world);
        name = item_name;
        type = TYPE_CONSUMABLE;
        image = Utils.scaleImage(item_key, tile_size, tile_size);
        description = "[" + name + "]\nIt opens a door.";
        price = 100;
        this.amount = amount;
        stackable = true;
    }

    public Key(Game game, World world, int x, int y, int amount) {
        super(game, world, x , y);
        name = item_name;
        type = TYPE_CONSUMABLE;
        image = Utils.scaleImage(item_key, 32, 32);
        description = "[" + name + "]\nIt opens a door.";
        price = 100;
        this.amount = amount;
        stackable = true;
        hitbox.x = 2;
        hitbox.y = 0;
        hitbox.width = 27;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public boolean use(Entity entity) {
        // Si detecta una puerta, entonces puede usar la llave
        int itemIndex = getDetected(entity, world.items, "Door");
        if (itemIndex != -1) {
            game.playSound(sound_door_opening);
            world.items[world.map][itemIndex] = null;
            return true;
        }
        return false;
    }

}
