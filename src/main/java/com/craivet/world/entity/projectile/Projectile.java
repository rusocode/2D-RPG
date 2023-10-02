package com.craivet.world.entity.projectile;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Particle;
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
                world.player.hitMob(mobIndex, this, stats.knockbackValue, stats.attack);
                // En este caso, el generador de particulas es la bola de fuego cuando el player la lanza contra un mob
                generateParticle(entity.projectile, world.mobs[world.map][mobIndex]);
                flags.alive = false;
            }
        }

        // Si el mob lanza un proyectil
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
            switch (direction) {
                case DOWN -> pos.y += stats.speed;
                case UP -> pos.y -= stats.speed;
                case LEFT -> pos.x -= stats.speed;
                case RIGHT -> pos.x += stats.speed;
            }
            timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
        }
    }

    /**
     * Genera 4 particulas en el objetivo.
     *
     * @param generator entidad que va a generar las particulas.
     * @param target    objetivo en donde se van a generar las particulas.
     */
    protected void generateParticle(Entity generator, Entity target) {
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    public boolean haveResource(Entity entity) {
        return false;
    }

    public void subtractResource(Entity entity) {
    }

    public void set(int x, int y, Direction direction, boolean alive, Entity entity) {
        pos.x = x;
        pos.y = y;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        /* Una vez que el projectil muere (alive=false) el hp queda en 0, por lo tanto para lanzar el proximo se
         * necesita establecer la vida al maximo de nuevo. */
        stats.hp = stats.maxHp;
    }

}
