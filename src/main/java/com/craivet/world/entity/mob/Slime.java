package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.world.entity.projectile.StickyBall;
import com.craivet.utils.*;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Slime extends Mob {

    public Slime(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Slime";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 4;
        stats.exp = 10;
        stats.attack = 2;
        stats.defense = 2;
        soundHit = sound_slime_hit;
        hitbox = new Rectangle(3, 12, tile - 7, tile - 17);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        projectile = new StickyBall(game, world);
        sheet.loadMovementFrames(slime, 16, 16, 2);
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 15);
            game.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
            // checkShoot();
        } else {
            checkFollow(world.entities.player, 5, 100);
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
