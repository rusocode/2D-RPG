package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Orc extends Mob {

    public Orc(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Orc";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 10;
        stats.exp = 2;
        stats.attack = 8;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 30;
        soundHit = sound_hit_orc;
        soundDeath = sound_orc_death;
        hitbox.x = 4;
        hitbox.y = 15;
        hitbox.width = tile - hitbox.x - 4;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 44;
        attackbox.height = 48;
        sheet.loadMovementFrames(orc_movement, 16, 16, 1); // TODO No tendria que dividir el tile?
        sheet.loadAttackFrames(orc_attack, 16, 16, 1);
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
        if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 2, tile * 2, 30);
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        int probabilityKeyDrop = 100;
        if (Utils.random(100) <= probabilityKeyDrop) drop(this, new Key(game, world, 1));
    }

}