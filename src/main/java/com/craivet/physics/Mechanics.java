package com.craivet.physics;

import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.mob.Rock;

/**
 * Mecanicas del juego.
 *
 * @author Juan Debenedetti
 */

public class Mechanics {

    // Indica cuando el Player esta "unido" al Mob
    private boolean united;

    /**
     * Establece el knockback al objetivo del atacante.
     *
     * @param target         objetivo del atacante.
     * @param attacker       atacante de la entidad.
     * @param knockbackValue valor de knockback.
     */
    public void setKnockback(Entity target, Entity attacker, int knockbackValue) {
        target.knockbackDirection = attacker.direction;
        target.stats.speed += knockbackValue;
        target.flags.knockback = true;
    }

    public void stopKnockback(Entity entity) {
        entity.flags.knockback = false;
        entity.stats.speed = entity.stats.defaultSpeed;
        entity.timer.knockbackCounter = 0;
    }

    /**
     * Comprueba la velocidad de la direccion del Player cuando colisiona con un Mob en movimiento en la misma
     * direccion. Esto se hace para evitar un "tartamudeo" en la animacion de movimiento del Player. En ese caso, "une"
     * el Player al Mob. En caso contrario, "desune" el Player del Mob.
     *
     * @param mob mob actual.
     */
    public void checkDirectionSpeed(Entity player, Entity mob) {
        if (checkConditionsForUnion(player, mob)) unite(player, mob);
        else disunite(player);
    }

    /**
     * Comprueba si el Mob es distinto a null, y si el Mob es un Npc, y si el Player esta colisionando con el Mob, y si
     * el Player esta en la misma direccion que el Mob, y si el Player no tiene distancia con el Mob y si el Mob no
     * colisiono.
     *
     * @param mob mob actual.
     * @return true si se cumplen todas las condiciones especificadas o false.
     */
    private boolean checkConditionsForUnion(Entity player, Entity mob) {
        return mob != null && mob.stats.type == Type.NPC && player.flags.collidingOnMob
                && player.direction == mob.direction && !isDistanceWithMob(player, mob) && !mob.flags.colliding;
    }

    /**
     * Comprueba si hay distancia con el Mob.
     * <p>
     * <h3>¿Para que hace esto?</h3>
     * Cuando sigue (siempre colisionando) al Mob pero en algun momento lo deja de seguir y este se mantiene en la misma
     * direccion, la velocidad va a seguir siendo la misma a la del Mob. Entonces para solucionar ese problema se
     * comprueba la distancia, y si hay distancia entre el Player y el Mob, vuelve a la velocidad por defecto.
     *
     * @param mob mob actual.
     * @return true si hay distancia o false.
     */
    private boolean isDistanceWithMob(Entity player, Entity mob) {
        switch (mob.direction) {
            case DOWN -> {
                if (player.pos.y + player.hitbox.y + player.hitbox.height + mob.stats.speed < mob.pos.y + mob.hitbox.y)
                    return true;
            }
            case UP -> {
                if (player.pos.y + player.hitbox.y - mob.stats.speed > mob.pos.y + mob.hitbox.y + mob.hitbox.height)
                    return true;
            }
            case LEFT -> {
                if (player.pos.x + player.hitbox.x - mob.stats.speed > mob.pos.x + mob.hitbox.x + mob.hitbox.width)
                    return true;
            }
            case RIGHT -> {
                if (player.pos.x + player.hitbox.x + player.hitbox.width + mob.stats.speed < mob.pos.x + mob.hitbox.x)
                    return true;
            }
        }
        return false;
    }

    /**
     * Une el Player al Mob.
     * <p>
     * Iguala la velocidad del Mob a la del Player y dependiendo de la direccion, suma o resta un pixel. Esto ultimo se
     * hace para que el Player pueda dialogar si el Mob es un Npc y este esta en movimiento.
     *
     * @param mob mob actual.
     */
    private void unite(Entity player, Entity mob) {
        player.stats.speed = mob.stats.speed;
        united = true;
        if (!(mob instanceof Rock)) {
            switch (player.direction) {
                case DOWN -> player.pos.y++;
                case UP -> player.pos.y--;
                case LEFT -> player.pos.x--;
                case RIGHT -> player.pos.x++;
            }
        }
    }

    /**
     * Desune el Player del Mob.
     * <p>
     * Vuelve a la velocidad por defecto y verifica si estan unidos para "destrabar" ambas entidades restando o sumando
     * un pixel.
     */
    private void disunite(Entity player) {
        player.stats.speed = player.stats.defaultSpeed;
        player.flags.collidingOnMob = false;
        if (united) {
            switch (player.direction) {
                case DOWN -> player.pos.y--;
                case UP -> player.pos.y++;
                case LEFT -> player.pos.x++;
                case RIGHT -> player.pos.x--;
            }
            united = false;
        }
    }

}
