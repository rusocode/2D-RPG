package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Type;
import com.craivet.entity.item.Gold;
import com.craivet.entity.projectile.StickyBall;
import com.craivet.utils.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

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
        loadMovementImages(entity_redslime, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        mobImage = movementDown1;
    }

    public void setAction() {
        if (flags.onPath) {
            checkUnfollow(world.player, 17);
            searchPath(getGoalRow(world.player), getGoalCol(world.player));
            checkShoot();
        } else {
            checkFollow(world.player, 6, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
    }

    public void damageReaction() {
        timer.directionCounter = 0;
        flags.onPath = true;
    }

    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILIDAD_DROP_GOLD) dropItem(this, new Gold(game, world));
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
