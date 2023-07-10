package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Type;
import com.craivet.entity.item.Gold;
import com.craivet.entity.projectile.StickyBall;
import com.craivet.utils.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * El Slime usa dos frames para todos los movimientos.
 */

public class Slime extends Mob {

    public Slime(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Slime";
        type = Type.HOSTILE;
        speed = defaultSpeed = 1;
        hp = maxHp = 4;
        exp = 2;
        attack = 2;
        defense = 1;
        hitbox.x = 3;
        hitbox.y = 18;
        hitbox.width = 42;
        hitbox.height = 30;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        projectile = new StickyBall(game, world);
        loadMovementImages(entity_slime, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        mobImage = movementDown1;
    }

    public void setAction() {
        // Si es agresivo
        if (flags.onPath) {
            checkUnfollow(world.player, 15);
            searchPath(getGoalRow(world.player), getGoalCol(world.player));
            // checkShoot();
        } else {
            checkFollow(world.player, 5, 100);
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
