package com.craivet.utils;

import com.craivet.Direction;
import com.craivet.world.entity.Entity;

import java.awt.*;

/**
 * Temporiza las acciones del juego.
 *
 * @author Juan Debenedetti
 */

public class Timer {

    // Contadores
    public int attackAnimationCounter;
    public int attackCounter;
    public int deadCounter;
    public int directionCounter;
    public int hpBarCounter;
    public int invincibleCounter;
    public int knockbackCounter;
    public int movementCounter;
    public int stopMovementCounter;
    public int projectileCounter;

    /**
     * Temporiza el movimiento.
     * <p>
     * Si se completo el intervalo y si la entidad esta en el frame de movimiento 1, cambia al frame de movimiento 2 y
     * resetea el contador. Si se completo el intervalo y si la entidad esta en el frame de movimiento 2, cambia al frame
     * de movimiento 1 y resetea el contador.
     */
    public void timeMovement(Entity entity, final int interval) {
        if (++movementCounter >= interval - entity.speed) {
            if (entity.ss.movementNum == 1) entity.ss.movementNum = 2;
            else if (entity.ss.movementNum == 2) entity.ss.movementNum = 1;
            movementCounter = 0;
        }
    }

    public void timeStopMovement(Entity entity, final int interval) {
        if (++stopMovementCounter >= interval) {
            entity.ss.movementNum = 1;
            stopMovementCounter = 0;
        }
    }

    /**
     * Temporiza la invencibilidad.
     * <p>
     * Si se completo el intervalo, deja de ser invencible y resetea el contador.
     */
    public void timeInvincible(Entity entity, final int interval) {
        if (++invincibleCounter >= interval) {
            entity.flags.invincible = false;
            invincibleCounter = 0;
        }
    }

    /**
     * Temporiza la direccion.
     * <p>
     * Si se completo el intervalo, calcula un numero aleatorio entre 1 y 100 para determinar la nueva direccion. Si
     * el numero es menor o igual a 25, cambia a "down" y resetea el contador. Si el numero esta entre 26 y 50, ambos
     * incluidos, cambia a "up" y resetea el contador. Si el numero esta entre 51 y 75, ambos incluidos, cambia a "left"
     * y resetea el contador. Si el numero es mayor a 75, cambia a "right" y resetea el contador.
     */
    public void timeDirection(Entity entity, final int interval) {
        if (++directionCounter >= interval) {
            int i = Utils.azar(100);
            if (i <= 25) entity.direction = Direction.DOWN;
            if (i > 25 && i <= 50) entity.direction = Direction.UP;
            if (i > 50 && i <= 75) entity.direction = Direction.LEFT;
            if (i > 75) entity.direction = Direction.RIGHT;
            directionCounter = 0;
        }
    }

    /**
     * Temporiza la animacion de muerte.
     * <p>
     * Calcula la transparencia basandose en el residuo de la division del contador por el intervalo.
     */
    public void timeDeadAnimation(Entity entity, final int interval, Graphics2D g2) {
        int alpha = (++deadCounter / interval) % 2 == 0 ? 0 : 1;
        Utils.changeAlpha(g2, alpha);
        if (deadCounter > interval * 8) entity.flags.alive = false;
    }

    /**
     * Temporiza la barra de vida.
     * <p>
     * Si se completo el intervalo, desactiva la barra de vida y resetea el contador.
     */
    public void timeHpBar(Entity entity, final int interval) {
        if (++hpBarCounter >= interval) {
            entity.hpBar = false;
            hpBarCounter = 0;
        }
    }

    public void timerKnockback(Entity entity, final int interval) {
        if (++knockbackCounter >= interval) {
            entity.flags.knockback = false;
            entity.speed = entity.defaultSpeed;
            knockbackCounter = 0;
        }
    }

    /**
     * Reinicia los contadores.
     */
    public void resetCounter() {
        attackAnimationCounter = 0;
        attackCounter = 0;
        deadCounter = 0;
        directionCounter = 0;
        hpBarCounter = 0;
        invincibleCounter = 0;
        knockbackCounter = 0;
        movementCounter = 0;
        stopMovementCounter = 0;
        projectileCounter = 0;
    }

}
