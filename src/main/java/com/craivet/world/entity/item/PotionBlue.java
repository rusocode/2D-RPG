package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.tile;

public class PotionBlue extends Item {

    public static final String NAME = "Blue Potion";

    public PotionBlue(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        value = 2;
        description = "[" + stats.name + "]\nIncrease your mana \nby " + value + ".";
        price = 30;
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleImage(potion_blue, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        if (entity.stats.mana != entity.stats.maxMana) {
            game.playSound(sound_drink_potion);
            entity.stats.mana += value;
            if (entity.stats.mana > entity.stats.maxMana) entity.stats.mana = entity.stats.maxMana;
            return true;
        } else {
            game.ui.addMessageToConsole("You have a full mana");
            return false;
        }
    }

}
