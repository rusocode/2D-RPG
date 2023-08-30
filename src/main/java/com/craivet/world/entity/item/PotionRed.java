package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class PotionRed extends Item {

    public static final String NAME = "Red Potion";

    /**
     * Crea el objeto en el inventario o en el World (utiliza varargs para especificar la posicion).
     */
    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.CONSUMABLE;
        image = Utils.scaleImage(potion_red, tile, tile);
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;
        value = 2;
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
