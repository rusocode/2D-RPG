package com.punkipunk.utils;

import com.punkipunk.Direction;
import com.punkipunk.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import static com.punkipunk.utils.Global.*;

/**
 * Time game actions.
 */

public class Timer {

    public int attackAnimationCounter; // Contador de animacion de ataque que cambia entre el frame de ataque 1 y 2
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
     * Time the movement of 2 frame entities.
     * <p>
     * If the interval is complete and if the entity is in movement frame 1, change to movement frame 2 and reset the counter. If
     * the interval is complete and if the entity is in movement frame 2, it changes to movement frame 1 and resets the counter.
     *
     * @param entity   entity.
     * @param interval time interval in ms.
     */
    public void timeMovement(Entity entity, int interval) {
        if (++movementCounter >= interval - entity.stats.speed) {
            if (entity.sheet.movementNum == 1) entity.sheet.movementNum = 2;
            else if (entity.sheet.movementNum == 2) entity.sheet.movementNum = 1;
            movementCounter = 0;
        }
    }

    public void timeStopMovement(Entity entity, int interval) {
        if (++stopMovementCounter >= interval) {
            entity.sheet.movementNum = 1;
            stopMovementCounter = 0;
        }
    }

    /**
     * Time the invincibility.
     * <p>
     * If the interval is completed, the entity is no longer invincible and resets the counter.
     */
    public void timeInvincible(Entity entity, int interval) {
        if (++invincibleCounter >= interval) {
            entity.flags.invincible = false;
            invincibleCounter = 0;
        }
    }

    /**
     * Temporiza la direccion.
     * <p>
     * Si el intervalo esta completo, calcula un numero aleatorio entre 1 y 100 para determinar la nueva direccion. Si el numero
     * es menor o igual a 25, cambia a DOWN y reinicia el contador. Si el numero esta entre 26 y 50, ambos incluidos, cambia a UP
     * y reinicia el contador. Si el numero esta entre 51 y 75, ambos incluidos, cambia a LEFT y reinicia el contador. Si el
     * numero es mayor a 75, cambia a RIGHT y reinicia el contador.
     */
    public void timeDirection(Entity entity, int interval) {
        if (++directionCounter >= interval) {
            int i = Utils.random(100);
            if (i <= 25) entity.direction = Direction.DOWN;
            if (i > 25 && i <= 50) entity.direction = Direction.UP;
            if (i > 50 && i <= 75) entity.direction = Direction.LEFT;
            if (i > 75) entity.direction = Direction.RIGHT;
            directionCounter = 0;
        }
    }

    /**
     * Time the death animation.
     * <p>
     * Calculates the transparency based on the remainder of the division of the counter by the interval.
     */
    public void timeDeadAnimation(Entity entity, int interval, GraphicsContext context) {
        int alpha = (++deadCounter / interval) % 2 == 0 ? 0 : 1;
        Utils.changeAlpha(context, alpha);
        if (deadCounter > interval * 8) entity.flags.alive = false;
    }

    /**
     * Time the life bar.
     * <p>
     * If the interval is complete, disable the life bar and reset the counter.
     */
    public void timeHpBar(Entity entity, int interval) {
        if (++hpBarCounter >= interval) {
            entity.flags.hpBar = false;
            hpBarCounter = 0;
        }
    }

    public void timerKnockback(Entity entity, int interval) {
        if (++knockbackCounter >= interval) {
            entity.flags.knockback = false;
            entity.stats.speed = entity.stats.baseSpeed;
            knockbackCounter = 0;
        }
    }

    /**
     * Temporiza el ataque. Si el intervalo esta completo, el player puede atacar de nuevo.
     */
    public void timeAttack() {
        if (attackCounter < INTERVAL_ATTACK) attackCounter++;

    }

    public boolean isAttackReady() {
        return attackCounter >= INTERVAL_ATTACK;
    }

    public void timeShoot() {
        if (projectileCounter < INTERVAL_PROJECTILE) projectileCounter++;

    }

    public boolean isShootReady() {
        return projectileCounter >= INTERVAL_PROJECTILE;
    }

    /**
     * Control the timers.
     *
     * @param entity entity.
     */
    public void checkTimers(Entity entity) {
        timeMovement(entity, INTERVAL_MOVEMENT_ANIMATION);
        timeAttack();
        timeShoot();
        if (entity.flags.invincible) timeInvincible(entity, INTERVAL_INVINCIBLE);

    }

    /**
     * Reset the counters.
     */
    public void reset() {
        attackAnimationCounter = 0; // TODO Is this necessary?
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
