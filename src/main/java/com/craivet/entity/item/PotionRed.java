package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;
import com.craivet.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class PotionRed extends Item {

    public static final String NAME = "Red Potion";

    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.CONSUMABLE;
        if (pos.length > 0) image = Utils.scaleImage(item_potion_red, 32, 32);
        else image = Utils.scaleImage(item_potion_red, tile_size, tile_size);
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
        game.playSound(sound_potion_red);
        game.ui.addMessage("+" + value + " hp");
        entity.hp += value;
        if (entity.hp > entity.maxHp) entity.hp = entity.maxHp;
        return true;
    }

}
