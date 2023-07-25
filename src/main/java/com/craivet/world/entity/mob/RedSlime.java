package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.item.Gold;
import com.craivet.world.entity.projectile.StickyBall;
import com.craivet.util.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

public class RedSlime extends Mob {

    public RedSlime(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Red Slime";
        type = Type.HOSTILE;
        speed = defaultSpeed = 2;
        hp = maxHp = 5;
        exp = 3;
        attack = 3;
        defense = 1;
        hitbox.x = 3;
        hitbox.y = 18;
        hitbox.width = 42;
        hitbox.height = 30;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        projectile = new StickyBall(game, world);
        animation.loadMovementFrames(redslime, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        mobImage = animation.movement[0];
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
        if (Utils.azar(100) == 1 && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            projectile.set(x + 8, y + 17, direction, true, this);
            for (int i = 0; i < world.projectiles[1].length; i++) {
                if (world.projectiles[world.map][i] == null) {
                    world.projectiles[world.map][i] = projectile;
                    break;
                }
            }
            timer.projectileCounter = 0;
        }
    }

}
