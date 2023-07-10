package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.mob.Player;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import static com.craivet.util.Global.*;

public class Projectile extends Entity {

    private Entity entity;
    protected int cost;

    public Projectile(Game game, World world) {
        super(game, world);
    }

    public boolean haveResource(Entity entity) {
        return false;
    }

    public void subtractResource(Entity entity) {
    }

    public void set(int worldX, int worldY, int direction, boolean alive, Entity entity) {
        this.x = worldX;
        this.y = worldY;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        this.hp = this.maxHp; // Resetea la vida al valor maximo cada vez que lanza un proyectil
    }

    /**
     * Actualiza la posicion del proyectil si no colisiona con un mob o si no se termina la vida de este. En caso
     * contrario, deja de vivir.
     */
    @Override
    public void update() {

        // Si el player lanza un proyectil
        if (entity instanceof Player) {
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            /* Cuando el proyectil colisiona con un mob, establece el estado colliding en true. Por lo tanto, cuando
             * se vuelva a dibujar el proyectil, este se va a mantener en el frame de movimiento 1 ya que en el operador
             * ternario, la condicion se mantiene en true y nunca cambia a false para poder mostrar el frame de
             * movimiento 2. La siguiente linea soluciona este problema. */
            flags.colliding = false;
            if (mobIndex != -1 && !world.mobs[world.map][mobIndex].flags.invincible && world.mobs[world.map][mobIndex].type != Type.NPC) {
                world.player.hitMob(mobIndex, this, knockbackValue, attack);
                // En este caso, el generador de particulas es la bola de fuego cuando el player la lanza contra un mob
                generateParticle(entity.projectile, world.mobs[world.map][mobIndex]);
                flags.alive = false;
            }
        }

        // Si el mob lanza un proyectil
        if (!(entity instanceof Player)) {
            boolean contact = game.collision.checkPlayer(this);
            if (contact && !world.player.flags.invincible) {
                hitPlayer(true, attack);
                generateParticle(entity.projectile, world.player);
                flags.alive = false;
            }
        }

        if (hp-- <= 0) flags.alive = false;

        if (flags.alive) {
            switch (direction) {
                case DOWN -> y += speed;
                case UP -> y -= speed;
                case LEFT -> x -= speed;
                case RIGHT -> x += speed;
            }
            timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
        }
    }

}
