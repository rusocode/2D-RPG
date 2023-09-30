package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.Key;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Bat extends Mob {

    public Bat(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Bat";
        stats.speed = stats.defaultSpeed = 3;
        stats.hp = stats.maxHp = 7;
        stats.exp = 7;
        stats.attack = 1;
        stats.defense = 1;
        soundHit = sound_hit_bat;
        soundDeath = sound_bat_death;
        hitbox = new Rectangle(0, 0, tile, tile);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.loadMovementFrames(bat, 16, 16, 1);
    }

    @Override
    public void doActions() {
        timer.timeDirection(this, INTERVAL_DIRECTION_BAT);
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_KEY_DROP) drop(this, new Key(game, world, 1));
    }

}