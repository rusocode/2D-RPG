package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Orc extends Mob {

    public Orc(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Orc";
        type = Type.HOSTILE;
        soundHit = sound_hit_orc;
        soundDeath = sound_orc_death;
        speed = defaultSpeed = 1;
        hp = maxHp = 10;
        exp = 2;
        attack = 8;
        defense = 2;
        hitbox.x = 4;
        hitbox.y = 15;
        hitbox.width = tile - hitbox.x - 4;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 44;
        attackbox.height = 48;
        motion1 = 25;
        motion2 = 30;
        ss.loadMovementFrames(orc_movement, 16, 16, 1);
        ss.loadAttackFrames(orc_attack, 16, 16, 1);
        image = ss.movement[0];
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.player, 10);
            game.aStar.searchPath(this, getGoalRow(world.player), getGoalCol(world.player));
        } else {
            checkFollow(world.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
        if (!flags.hitting) isPlayerWithinAttackRange(tile * 2, tile * 2, 30);
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        int probabilityKeyDrop = 100;
        if (Utils.azar(100) <= probabilityKeyDrop) dropItem(this, new Key(game, world, 1));
    }

}