package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;

import static com.craivet.gfx.Assets.chicken;
import static com.craivet.gfx.Assets.sound_eat;
import static com.craivet.utils.Global.tile;

public class Chicken extends Item {

    public static final String NAME = "Chicken";

    public Chicken(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nYes...\nLizard's treasure.";
        price = 1;
        sheet.frame = Utils.scaleImage(chicken, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.playSound(sound_eat);
        world.entities.player.stats.hp = world.entities.player.stats.maxHp;
        world.entities.player.stats.mana = world.entities.player.stats.maxMana;
        return true;
    }

}


