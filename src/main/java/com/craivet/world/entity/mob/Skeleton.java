package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Skeleton extends Mob {

    public Skeleton(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Skeleton";
        type = Type.HOSTILE;
        soundHit = sound_hit_mob;
        soundDeath = sound_mob_death;
        speed = defaultSpeed = 1;
        hp = maxHp = 50;
        exp = 50;
        attack = 10;
        defense = 2;

        int scale = 5;
        int size = tile * scale;
        hitbox.x = tile;
        hitbox.y = tile;
        hitbox.width = size - tile * 2;
        hitbox.height = size - tile;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 90;
        attackbox.height = 90;
        motion1 = 25;
        motion2 = 50;
        ss.loadMovementFrames(skeleton_movement, 32, 32, scale);
        ss.loadAttackFrames(skeleton_attack, 32, 32, scale);
        image = ss.movement[0];
    }

    @Override
    public void doActions() {
        if (flags.following) {
            System.out.println();
        } else {
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
        /* TODO Comprobar que este en la misma direccion del player cuando ataque para que no ataque cuando este en
         * distinta direccion. Esto sucede ya que aunque no esten en la misma direccion pero el player se mantenga
         * cerca del mob, este ataca ya que esta dentro de su rango de ataque. */
        if (!flags.hitting) checkAttackOrNot(tile * 10, tile * 5, 60);
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILITY_GOLD_DROP) dropItem(this, new Gold(game, world));
    }


}
