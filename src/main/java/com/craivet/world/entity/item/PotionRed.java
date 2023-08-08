package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class PotionRed extends Item {

    public static final String NAME = "Red Potion";

    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.CONSUMABLE;
        if (pos.length > 0) image = Utils.scaleImage(potion_red, tile_size, tile_size);
        else image = Utils.scaleImage(potion_red, tile_size, tile_size);
        value = 5;
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;
        this.amount = amount;
        stackable = true;
        hitbox.x = 3;
        hitbox.y = 0;
        hitbox.width = 25;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    @Override
    public boolean use(Entity entity) {
        game.playSound(sound_drink_potion);
        entity.hp += value;
        if (entity.hp > entity.maxHp) entity.hp = entity.maxHp;
        return true;
    }

}
