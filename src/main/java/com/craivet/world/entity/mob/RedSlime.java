package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.world.entity.projectile.StickyBall;
import com.craivet.utils.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class RedSlime extends Mob {

    public RedSlime(Game game, World world, int x, int y) {
        super(game, world, x, y);
        stats.name = "Red Slime";
        stats.type = Type.HOSTILE;
        stats.speed = stats.defaultSpeed = 2;
        stats.hp = stats.maxHp = 5;
        stats.exp = 3;
        stats.attack = 3;
        stats.defense = 1;
        stats.hitbox.x = 3;
        stats.hitbox.y = 18;
        stats.hitbox.width = tile - stats.hitbox.x - 4;
        stats.hitbox.height = tile - stats.hitbox.y;
        stats.hitboxDefaultX = stats.hitbox.x;
        stats.hitboxDefaultY = stats.hitbox.y;
        stats.projectile = new StickyBall(game, world);
        sheet.loadMovementFrames(redslime, 16, 16, 1);
        sheet.frame = sheet.movement[0];
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.player, 17);
            game.aStar.searchPath(this, getGoalRow(world.player), getGoalCol(world.player));
            checkShoot();
        } else {
            checkFollow(world.player, 6, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILITY_GOLD_DROP) dropItem(this, new Gold(game, world));
    }

    private void checkShoot() {
        if (Utils.azar(100) == 1 && !stats.projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            stats.projectile.set(pos.x + 8, pos.y + 17, stats.direction, true, this);
            for (int i = 0; i < world.projectiles[1].length; i++) {
                if (world.projectiles[world.map][i] == null) {
                    world.projectiles[world.map][i] = stats.projectile;
                    break;
                }
            }
            timer.projectileCounter = 0;
        }
    }

}
