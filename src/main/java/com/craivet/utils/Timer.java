package com.craivet.utils;

import com.craivet.entity.Entity;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Temporiza las acciones del juego.
 *
 * @author Juan Debenedetti
 */

public class Timer {

    public int attackAnimationCounter;
    public int attackCounter;
    public int deadCounter;
    public int directionCounter;
    public int hpBarCounter;
    public int invincibleCounter;
    public int knockBackCounter;
    public int movementCounter;
    public int naturalStopWalkingCounter;
    public int projectileCounter;

    /**
     * Temporiza el movimiento.
     *
     * <p>Si se completo el interval y si la entidad esta en el frame de movimiento 1, cambia al frame de movimiento 2 y
     * resetea el contador. Si se completo el interval y si la entidad esta en el frame de movimiento 2, cambia al frame
     * de movimiento 1 y resetea el contador.
     */
    public void timeMovement(Entity entity, final int interval) {
        if (++movementCounter >= interval - entity.speed) {
            if (entity.movementNum == 1) entity.movementNum = 2;
            else if (entity.movementNum == 2) entity.movementNum = 1;
            movementCounter = 0;
        }
    }

    public void timeNaturalStopWalking(Entity entity, final int interval) {
        if (++naturalStopWalkingCounter >= interval) {
            entity.movementNum = 1;
            naturalStopWalkingCounter = 0;
        }
    }

    /**
     * Temporiza la invencibilidad.
     *
     * <p>Si se completo el interval, deja de ser invencible y resetea el contador.
     */
    public void timeInvincible(Entity entity, final int interval) {
        if (++invincibleCounter >= interval) {
            entity.invincible = false;
            invincibleCounter = 0;
        }
    }

    /**
     * Temporiza la direccion.
     *
     * <p>Si se completo el intervalo, calcula un numero aleatorio entre 1 y 100 para determinar la nueva direccion. Si
     * el numero es menor o igual a 25, cambia a "down" y resetea el contador. Si el numero esta entre 26 y 50, ambos
     * incluidos, cambia a "up" y resetea el contador. Si el numero esta entre 51 y 75, ambos incluidos, cambia a "left"
     * y resetea el contador. Si el numero es mayor a 75, cambia a "right" y resetea el contador.
     */
    public void timeDirection(Entity entity, final int interval) {
        if (++directionCounter >= interval) {
            int i = Utils.azar(100);
            if (i <= 25) entity.direction = DOWN;
            if (i > 25 && i <= 50) entity.direction = UP;
            if (i > 50 && i <= 75) entity.direction = LEFT;
            if (i > 75) entity.direction = RIGHT;
            directionCounter = 0;
        }
    }

    /**
     * Temporiza la animacion de muerte.
     *
     * <p>Calcula la transparencia basandose en el residuo de la division del contador por el intervalo.
     */
    public void timeDeadAnimation(Entity entity, final int interval, Graphics2D g2) {
        int alpha = (++deadCounter / interval) % 2 == 0 ? 0 : 1;
        Utils.changeAlpha(g2, alpha);
        if (deadCounter > interval * 8) entity.alive = false;
    }

    /**
     * Temporiza la barra de vida.
     *
     * <p>Si se completo el interval, desactiva la barra de vida y resetea el contador.
     */
    public void timeHpBar(Entity entity, final int interval) {
        if (++hpBarCounter >= interval) {
            entity.hpBar = false;
            hpBarCounter = 0;
        }
    }

    public void timerKnockBack(Entity entity, final int interval) {
        if (++knockBackCounter >= interval) {
            knockBackCounter = 0;
            entity.knockBack = false;
            entity.speed = entity.defaultSpeed;
        }
    }

}
