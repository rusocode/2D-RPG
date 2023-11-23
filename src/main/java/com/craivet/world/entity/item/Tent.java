package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Tent extends Item {

    public static final String NAME = "Tent";

    public Tent(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" +stats. name + "]\nYou can sleep until\nnext morning.";
        price = 1200;
        sheet.frame = Utils.scaleImage(tent, tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.state = SLEEP_STATE;
        game.playSound(sound_sleep);
        world.entities.player.stats.hp = world.entities.player.stats.maxHp;
        world.entities.player.stats.mana = world.entities.player.stats.maxMana;
        world.entities.player.initSleepImage(sheet.frame);
        return true;
    }

}
