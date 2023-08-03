package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.mob.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.CONSUMABLE;
        // Si se especifica una posicion entonces la imagen es mas pequenia
        if (pos.length > 0) image = Utils.scaleImage(key, 32, 32);
        else image = Utils.scaleImage(key, tile_size, tile_size);
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

    @Override
    public boolean use(Entity entity) {
        // Si detecta una puerta, entonces puede usar la llave
        int i = detect(entity, world.items, Door.NAME);
        if (i != -1) {
            game.playSound(sound_door_opening);
            world.items[world.map][i] = null;
            return true;
        }
        return false;
    }

}
