package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.Key;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Bat extends Mob {

    public Bat(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Bat";
        type = Type.HOSTILE;
        soundHit = sound_hit_bat;
        soundDeath = sound_bat_death;
        speed = defaultSpeed = 3;
        hp = maxHp = 7;
        exp = 7;
        attack = 1;
        defense = 1;
        hitbox.x = 3;
        hitbox.y = 15;
        hitbox.width = tile - hitbox.x;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        ss.loadMovementFrames(bat, 16, 16, 1);
        image = ss.movement[0];
    }

    @Override
    public void doActions() {
        timer.timeDirection(this, INTERVAL_DIRECTION_BAT);
    }

    @Override
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILITY_KEY_DROP) dropItem(this, new Key(game, world, 1));
    }

}