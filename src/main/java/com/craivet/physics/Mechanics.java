package com.craivet.physics;

import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.mob.BigRock;
import com.craivet.world.entity.mob.Player;

import static com.craivet.util.Global.*;

/**
 * Controla la velocidad del Player cuando colisiona con un Mob en movimiento en la misma direccion.
 *
 * @author Juan Debenedetti
 */

public class Mechanics {

    private final Player player;

    // Indica cuando el Player esta "unido" al Mob
    private boolean united;

    public Mechanics(Player player) {
        this.player = player;
    }

    /**
     * Comprueba la velocidad de la direccion del Player cuando colisiona con un Mob en movimiento en la misma
     * direccion. Esto se hace para evitar un "tartamudeo" en la animacion de movimiento del Player. En ese caso, "une"
     * el Player al Mob. En caso contrario, "desune" el Player del Mob.
     *
     * @param mob Mob actual.
     */
    public void checkDirectionSpeed(Entity mob) {
        if (checkConditionsForUnion(mob)) {
            System.out.println("asd");
            unite(mob);
        }
        else disunite();
    }

    /**
     * Comprueba si el Mob es distinto a null, y si el Mob es un Npc, y si el Player esta colisionando con el Mob, y si
     * el Player esta en la misma direccion que el Mob, y si el Player no tiene distancia con el Mob y si el Mob no
     * colisiono.
     *
     * @param mob Mob actual.
     * @return true si se cumplen todas las condiciones especificadas o false.
     */
    private boolean checkConditionsForUnion(Entity mob) {
        return mob != null && mob.type == Type.NPC && player.flags.collidingOnMob
                && player.direction == mob.direction && !isDistanceWithMob(mob) && !mob.flags.colliding;
    }

    /**
     * Comprueba si hay distancia con el Mob.
     * <p>
     * <h3>¿Para que hace esto?</h3>
     * Cuando sigue (siempre colisionando) al Mob pero en algun momento lo deja de seguir y este se mantiene en la misma
     * direccion, la velocidad va a seguir siendo la misma a la del Mob. Entonces para solucionar ese problema se
     * comprueba la distancia, y si hay distancia entre el Player y el Mob, vuelve a la velocidad por defecto.
     *
     * @param mob Mob actual.
     * @return true si hay distancia o false.
     */
    private boolean isDistanceWithMob(Entity mob) {
        switch (mob.direction) {
            case DOWN -> {
                if (player.y + player.hitbox.y + player.hitbox.height + mob.speed < mob.y + mob.hitbox.y)
                    return true;
            }
            case UP -> {
                if (player.y + player.hitbox.y - mob.speed > mob.y + mob.hitbox.y + mob.hitbox.height)
                    return true;
            }
            case LEFT -> {
                if (player.x + player.hitbox.x - mob.speed > mob.x + mob.hitbox.x + mob.hitbox.width)
                    return true;
            }
            case RIGHT -> {
                if (player.x + player.hitbox.x + player.hitbox.width + mob.speed < mob.x + mob.hitbox.x)
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
     * @param mob Mob actual.
     */
    private void unite(Entity mob) {
        player.speed = mob.speed;
        united = true;
        if (!(mob instanceof BigRock)) {
            switch (player.direction) {
                case DOWN -> player.y++;
                case UP -> player.y--;
                case LEFT -> player.x--;
                case RIGHT -> player.x++;
            }
        }
    }

    /**
     * Desune el Player del Mob.
     * <p>
     * Vuelve a la velocidad por defecto y verifica si estan unidos para "destrabar" ambas entidades restando o sumando
     * un pixel.
     */
    private void disunite() {
        player.speed = player.defaultSpeed;
        player.flags.collidingOnMob = false;
        if (united) {
            switch (player.direction) {
                case DOWN -> player.y--;
                case UP -> player.y++;
                case LEFT -> player.x++;
                case RIGHT -> player.x--;
            }
            united = false;
        }
    }

}
