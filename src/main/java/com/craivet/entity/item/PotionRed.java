package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class PotionRed extends Item {

    public static final String item_name = "Red Potion";

    public PotionRed(Game game, World world, int amount) {
        super(game, world);
        name = item_name;
        type = TYPE_CONSUMABLE;
        image = Utils.scaleImage(item_potion_red, tile_size, tile_size);
        value = 5;
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;
        this.amount = amount;
        stackable = true;
    }

    public PotionRed(Game game, World world, int x, int y, int amount) {
        super(game, world, x, y);
        name = item_name;
        type = TYPE_CONSUMABLE;
        image = Utils.scaleImage(item_potion_red, tile_size, tile_size);
        value = 5;
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;
        this.amount = amount;
        stackable = true;
    }

    public boolean use(Entity entity) {
        game.playSound(sound_potion_red);
        game.ui.addMessage("+" + value + " hp");
        entity.life += value;
        if (entity.life > entity.maxLife) entity.life = entity.maxLife;
        return true;
    }

}
