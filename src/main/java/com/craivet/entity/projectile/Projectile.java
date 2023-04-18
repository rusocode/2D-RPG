package com.craivet.entity.projectile;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Player;
import com.craivet.World;

import static com.craivet.utils.Global.*;

public class Projectile extends Entity {

    private Entity entity;
    protected int useCost;

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
        this.alive = alive;
        this.entity = entity;
        this.life = this.maxLife; // Resetea la vida al valor maximo cada vez que lanza un proyectil
    }

    /**
     * Actualiza la posicion del proyectil si no colisiona con un mob o si no se termina la vida de este. En caso
     * contrario, deja de vivir.
     */
    @Override
    public void update() {

        // Si el player lanza un proyectil
        if (entity instanceof Player) {
            int mobIndex = game.collider.checkEntity(this, world.mobs);
            /* Cuando el proyectil colisiona con un mob, establece el estado collisionOn en true. Por lo tanto, cuando
             * se vuelva a dibujar el proyectil, este se va a mantener en el frame de movimiento 1 ya que en el operador
             * ternario, la condicion se mantiene en true y nunca cambia a false para poder mostrar el frame de
             * movimiento 2. La siguiente linea soluciona este problema. */
            collision = false;
            if (mobIndex != -1 && !world.mobs[world.map][mobIndex].invincible) {
                world.player.damageMob(mobIndex, this, knockbackValue, attack);
                // En este caso, el generador de particulas es la bola de fuego cuando el player la lanza contra un mob
                generateParticle(entity.projectile, world.mobs[world.map][mobIndex]);
                alive = false;
            }
        }

        // Si el mob lanza un proyectil
        if (!(entity instanceof Player)) {
            boolean contact = game.collider.checkPlayer(this);
            if (contact && !world.player.invincible) {
                damagePlayer(true, attack);
                generateParticle(entity.projectile, world.player);
                alive = false;
            }
        }

        if (life-- <= 0) alive = false;

        if (alive) {
            switch (direction) {
                case DOWN:
                    y += speed;
                    break;
                case UP:
                    y -= speed;
                    break;
                case LEFT:
                    x -= speed;
                    break;
                case RIGHT:
                    x += speed;
                    break;
            }
            timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
        }
    }

}
