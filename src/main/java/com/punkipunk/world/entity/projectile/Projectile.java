package com.punkipunk.world.entity.projectile;

import com.punkipunk.Direction;
import com.punkipunk.Game;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.mob.Mob;

import java.net.URL;

import static com.punkipunk.utils.Global.INTERVAL_PROJECTILE_ANIMATION;

/**
 * TODO Cambiar nombre a Spell
 */

public class Projectile extends Entity {

    public URL sound;
    protected Entity entity;
    protected int cost;

    public Projectile(Game game, World world) {
        super(game, world);
    }

    /**
     * Updates the position of the projectile if it does not collide with a mob or if its life does not end. Otherwise, stop
     * living.
     */
    @Override
    public void update() {

        // If the player shooting a projectile
        if (entity instanceof Player) {
            int mobIndex = game.system.collisionChecker.checkEntity(this, world.entities.mobs);
            if (mobIndex != -1) {
                Mob mob = world.entities.mobs[world.map.num][mobIndex];
                /* When the projectile collides with a mob, set the colliding state to true. Therefore, when the projectilew
                 * is redrawn, it will remain in motion frame 1 since in the ternary operator, the condition remains true
                 * and never changes to false in order to display motion frame 2. The following line solves this problem. */
                flags.colliding = false;
                if (!mob.flags.invincible && mob.type != Type.NPC) {
                    world.entities.player.hitMob(mobIndex, this, stats.knockbackValue, getAttack());
                    // In this case, the particle generator is the fireball when the player throws it against a mob
                    generateParticle(entity.projectile, mob);
                    flags.alive = false;
                }
            }
        }

        // If the mob shooting a projectile
        if (!(entity instanceof Player)) {
            boolean contact = game.system.collisionChecker.checkPlayer(this);
            if (contact && !world.entities.player.flags.invincible) {
                hitPlayer(true, stats.attack);
                generateParticle(entity.projectile, world.entities.player);
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

    /**
     * Calcula el ataque dependiendo del lvl del player. Para el lvl 1, el ataque disminuye el doble. Osea, si el ataque del
     * hechizo es 4 y el lvl del player es 1, entonces el ataque es de 2. Para el lvl 2, el ataque es el mismo, y para los
     * siguientes niveles el ataque aumenta en 2.
     *
     * @return el valor de ataque.
     */
    protected int getAttack() {
        return stats.attack * (entity.stats.lvl / 2);
    }

}
