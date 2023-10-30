package com.craivet.world.entity.projectile;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import static com.craivet.utils.Global.*;

public class Projectile extends Entity {

    private Entity entity;
    protected int cost;

    public Projectile(Game game, World world) {
        super(game, world);
    }

    /**
     * Updates the position of the projectile if it does not collide with a mob or if its life does not end. Otherwise,
     * stop living.
     */
    @Override
    public void update() {

        // If the player shooting a projectile
        if (entity instanceof Player) {
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            /* When the projectile collides with a mob, set the colliding state to true. Therefore, when the projectilew
             * is redrawn, it will remain in motion frame 1 since in the ternary operator, the condition remains true
             * and never changes to false in order to display motion frame 2. The following line solves this problem. */
            flags.colliding = false;
            if (mobIndex != -1 && !world.mobs[world.map][mobIndex].flags.invincible && world.mobs[world.map][mobIndex].type != Type.NPC) {
                world.player.hitMob(mobIndex, this, stats.knockbackValue, stats.attack * (entity.stats.lvl / 2));
                // In this case, the particle generator is the fireball when the player throws it against a mob
                generateParticle(entity.projectile, world.mobs[world.map][mobIndex]);
                flags.alive = false;
            }
        }

        // If the mob shooting a projectile
        if (!(entity instanceof Player)) {
            boolean contact = game.collision.checkPlayer(this);
            if (contact && !world.player.flags.invincible) {
                hitPlayer(true, stats.attack);
                generateParticle(entity.projectile, world.player);
                flags.alive = false;
            }
        }

        if (stats.hp-- <= 0) flags.alive = false;

        if (flags.alive) {
            pos.update(this, direction);
            timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
        }
    }

    public void set(int x, int y, Direction direction, boolean alive, Entity entity) {
        pos.x = x;
        pos.y = y;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        /* Once the projectile dies (alive=false) the hp remains at 0, therefore to launch the next one you need to set
         * the life to maximum again. */
        stats.hp = stats.maxHp;
    }

    public boolean haveResource(Entity entity) {
        return false;
    }

    public void subtractResource(Entity entity) {
    }

}
