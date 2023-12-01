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
     * Create the item in the inventory or in the World (use varargs to specify the position).
     */
    public PotionRed(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        points = 2;
        description = "[" + stats.name + "]\nHeals your life by " + points + ".";
        price = 2;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleImage(potion_red, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.hp != entity.stats.maxHp) {
            game.playSound(sound_drink_potion);
            entity.stats.hp += points;
            if (entity.stats.hp > entity.stats.maxHp) entity.stats.hp = entity.stats.maxHp;
            return true;
        } else {
            game.ui.addMessageToConsole("You have a full life");
            return false;
        }
    }

}
