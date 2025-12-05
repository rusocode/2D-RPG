package com.punkipunk.entity.spells;

import com.punkipunk.Direction;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.player.Player;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_PROJECTILE_ANIMATION;

public abstract class Spell extends Entity {

    public String sound;
    protected Entity entity;
    protected int cost;

    protected SpellData spellData;

    public Spell(IGame game, World world, SpellData spellData) {
        super(game, world);

        this.spellData = spellData;

        stats.name = spellData.name();
        stats.speed = spellData.speed();
        stats.hp = stats.maxHp = spellData.hp();
        stats.attack = spellData.attack();
        stats.knockback = spellData.knockback();
        cost = spellData.cost();
        flags.alive = spellData.alive();

    }

    @Override
    public void update() {

        // Si el player lanza el hechizo
        if (entity instanceof Player) {
            game.getGameSystem().collisionChecker.checkMob(this).ifPresent(mob -> {
                // flags.colliding = false; // ?
                if (!mob.flags.invincible && mob.mobCategory != MobCategory.NPC) {
                    world.entitySystem.player.hitMob(mob, this, stats.knockback, getAttack());
                    generateParticle(entity.spell, mob);
                    flags.alive = false;
                }
            });
        }

        // Si el mob lanza el hechizo
        if (!(entity instanceof Player)) {
            boolean contact = game.getGameSystem().collisionChecker.checkPlayer(this);
            if (contact && !world.entitySystem.player.flags.invincible) {
                hitPlayer(this, true, stats.attack);
                generateParticle(entity.spell, world.entitySystem.player);
                flags.alive = false;
            }
        }

        if (stats.hp-- <= 0) flags.alive = false;

        if (flags.alive) {
            position.update(this, direction);
            timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
        }


    }

    public void set(int x, int y, Direction direction, boolean alive, Entity entity) {
        position.x = x;
        position.y = y;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        /* Una vez que el proyectil muere (alive=false), el hp se establece en 0, por lo tanto, para lanzar el siguiente debes
         * volver a poner la vida al maximo. */
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
