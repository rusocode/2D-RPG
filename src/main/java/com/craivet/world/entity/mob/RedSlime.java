package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.world.entity.projectile.StickyBall;
import com.craivet.utils.*;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class RedSlime extends Mob {

    public RedSlime(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Red Slime";
        stats.speed = stats.defaultSpeed = 2;
        stats.hp = stats.maxHp = 5;
        stats.exp = 3;
        stats.attack = 3;
        stats.defense = 1;
        hitbox = new Rectangle(3, 18, tile - 7, tile - 18);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        projectile = new StickyBall(game, world);
        sheet.loadMovementFrames(redslime, 16, 16, 1);
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
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world));
    }

    private void checkShoot() {
        if (Utils.random(100) == 1 && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            projectile.set(pos.x + 8, pos.y + 17, direction, true, this);
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
