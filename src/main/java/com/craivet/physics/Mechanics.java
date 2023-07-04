package com.craivet.physics;

import com.craivet.entity.Entity;
import com.craivet.entity.npc.BigRock;
import com.craivet.entity.npc.Npc;

import static com.craivet.utils.Global.*;

/**
 * Controla la velocidad del Player cuando colisiona con un Npc en movimiento en la misma direccion.
 *
 * @author Juan Debenedetti
 */

public class Mechanics {

    private final Entity entity;

    // Indica cuando esta "unido" a una entidad en movimiento
    private boolean united;

    public Mechanics(Entity entity) {
        this.entity = entity;
    }

    /**
     * Comprueba la velocidad de la direccion cuando colisiona con una entidad en movimiento en la misma direccion. Esto
     * se hace para evitar un "tartamudeo" en la animacion de movimiento del Player. En ese caso, "une" el Player a la
     * entidad. En caso contrario, "desune" el Player de la entidad.
     *
     * @param currentEntity entidad actual.
     */
    public void checkDirectionSpeed(Entity currentEntity) {
        if (checkSomeConditionsForUnion(currentEntity)) unite(currentEntity);
        else disunite();
    }

    /**
     * Comprueba si la entidad actual es distinta a null, y si la entidad actual es un Npc, y si el Player colisiono con
     * la entidad, y si el Player esta en la misma direccion que la entidad actual, y si el Player no tiene distancia
     * con la entidad y si la entidad actual no colisiono.
     *
     * @param currentEntity entidad actual.
     * @return true si se cumplen todas las condiciones especificadas o false.
     */
    private boolean checkSomeConditionsForUnion(Entity currentEntity) {
        return currentEntity != null && currentEntity instanceof Npc &&
                entity.flags.collidingOnEntity && entity.direction == currentEntity.direction &&
                !isDistanceWithEntity(currentEntity) && !currentEntity.flags.colliding;
    }

    /**
     * Comprueba si hay distancia con la entidad.
     * <p>
     * <h3>¿Para que hace esto?</h3>
     * Cuando sigue (siempre colisionando) a la entidad pero en algun momento la deja de seguir y esta se mantiene en la
     * misma direccion, la velocidad va a seguir siendo la misma a la de la entidad. Entonces para solucionar ese
     * problema se comprueba la distancia, y si hay distancia entre el Player y la entidad, vuelve a la velocidad por
     * defecto.
     *
     * @param currentEntity entidad actual.
     * @return true si hay distancia o false.
     */
    private boolean isDistanceWithEntity(Entity currentEntity) {
        switch (currentEntity.direction) {
            case DOWN -> {
                if (entity.y + entity.hitbox.y + entity.hitbox.height + currentEntity.speed < currentEntity.y + currentEntity.hitbox.y)
                    return true;
            }
            case UP -> {
                if (entity.y + entity.hitbox.y - currentEntity.speed > currentEntity.y + currentEntity.hitbox.y + currentEntity.hitbox.height)
                    return true;
            }
            case LEFT -> {
                if (entity.x + entity.hitbox.x - currentEntity.speed > currentEntity.x + currentEntity.hitbox.x + currentEntity.hitbox.width)
                    return true;
            }
            case RIGHT -> {
                if (entity.x + entity.hitbox.x + entity.hitbox.width + currentEntity.speed < currentEntity.x + currentEntity.hitbox.x)
                    return true;
            }
        }
        return false;
    }

    /**
     * Une el Player a la entidad.
     * <p>
     * Iguala la velocidad de la entidad a la del Player y dependiendo de la direccion, suma o resta un pixel. Esto
     * ultimo se hace para poder hablar con el Npc si este esta en movimiento.
     *
     * @param currentEntity entidad actual.
     */
    private void unite(Entity currentEntity) {
        entity.speed = currentEntity.speed;
        united = true;
        if (!(currentEntity instanceof BigRock)) {
            switch (entity.direction) {
                case DOWN -> entity.y++;
                case UP -> entity.y--;
                case LEFT -> entity.x--;
                case RIGHT -> entity.x++;
            }
        }
    }

    /**
     * Desune el Player de la entidad.
     * <p>
     * Vuelve a la velocidad por defecto y verifica si estan unidos para "destrabar" ambas entidades restando o sumando
     * un pixel.
     */
    private void disunite() {
        entity.speed = entity.defaultSpeed;
        entity.flags.collidingOnEntity = false;
        if (united) {
            switch (entity.direction) {
                case DOWN -> entity.y--;
                case UP -> entity.y++;
                case LEFT -> entity.x++;
                case RIGHT -> entity.x--;
            }
            united = false;
        }
    }

}
