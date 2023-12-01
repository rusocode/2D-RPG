package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.world.entity.projectile.Fireball;
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
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 6;
        stats.exp = 15;
        stats.attack = 3;
        stats.defense = 2;
        soundHit = sound_slime_hit;
        hitbox = new Rectangle(3, 6, tile - 7, tile - 11);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        projectile = new Fireball(game, world);
        sheet.loadMovementFrames(redslime, 16, 16, 2);
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 17);
            game.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
            checkShoot();
        } else {
            checkFollow(world.entities.player, 6, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world, 50));
    }

    private void checkShoot() {
        if (Utils.random(100) == 1 && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            projectile.set(pos.x, pos.y, direction, true, this);
            for (int i = 0; i < world.entities.projectiles[1].length; i++) {
                if (world.entities.projectiles[world.map.num][i] == null) {
                    world.entities.projectiles[world.map.num][i] = projectile;
                    break;
                }
            }
            timer.projectileCounter = 0;
        }
    }

}
