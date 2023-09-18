package com.craivet.physics;

import com.craivet.world.entity.Entity;
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
     * Golpea a la entidad si la attackbox en el frame de ataque colisiona con la hitbox del objetivo.
     * <p>
     * De 0 a motion1 ms se muestra el primer frame de ataque. De motion1 a motion2 ms se muestra el segundo frame de
     * ataque. Despues de motion2 vuelve al frame de movimiento. Para el caso del player solo hay un frame de ataque.
     * <p>
     * En el segundo frame de ataque, la posicion x/y se ajusta para la attackbox y verifica si colisiona con una
     * entidad.
     *
     * @param e entidad.
     */
    public void hit(Entity e) {
        e.timer.attackAnimationCounter++;
        if (e.timer.attackAnimationCounter <= e.motion1) e.ss.attackNum = 1; // (de 0-motion1ms frame de ataque 1)
        if (e.timer.attackAnimationCounter > e.motion1 && e.timer.attackAnimationCounter <= e.motion2) { // (de motion1-motion2ms frame de ataque 2)
            e.ss.attackNum = 2;

            // Guarda la posicion actual de x/y y el tamaño de la hitbox
            int currentX = e.x, currentY = e.y;
            int hitboxWidth = e.hitbox.width, hitboxHeight = e.hitbox.height;

            /* Ajusta la attackbox (en la hoja de la espada para ser mas especificos) del player dependiendo de la
             * direccion de ataque. Es importante aclarar que las coordenadas x/y de la attackbox parten de la esquina
             * superior izquierda de la hitbox del player (nose si es necesario partir desde esa esquina). */
            if (e.type == Type.PLAYER) {
                switch (e.direction) {
                    case DOWN -> {
                        e.attackbox.x = -1;
                        e.attackbox.y = 4;
                        e.attackbox.width = 4;
                        e.attackbox.height = 36;
                    }
                    case UP -> {
                        e.attackbox.x = 12;
                        e.attackbox.y = -43;
                        e.attackbox.width = 4;
                        e.attackbox.height = 42;
                    }
                    case LEFT -> {
                        e.attackbox.x = -20;
                        e.attackbox.y = 0;
                        e.attackbox.width = 19;
                        e.attackbox.height = 4;
                    }
                    case RIGHT -> {
                        e.attackbox.x = 10;
                        e.attackbox.y = 2;
                        e.attackbox.width = 19;
                        e.attackbox.height = 4;
                    }
                }
                /* Acumula la posicion de la attackbox a la posicion del player para verificar la colision con las
                 * coordenas ajustadas de la attackbox. */
                e.x += e.attackbox.x;
                e.y += e.attackbox.y;
            } else if (e.type == Type.HOSTILE) {
                switch (e.direction) {
                    case DOWN -> e.y += e.attackbox.height;
                    case UP -> e.y -= e.attackbox.height;
                    case LEFT -> e.x -= e.attackbox.width;
                    case RIGHT -> e.x += e.attackbox.width;
                }
            }

            // Convierte la hitbox (el ancho y alto) en la attackbox para verificar la colision solo con la attackbox
            e.hitbox.width = e.attackbox.width;
            e.hitbox.height = e.attackbox.height;

            if (e.type == Type.HOSTILE) e.hitPlayer(e.game.collision.checkPlayer(e), e.attack);
            else {
                // Verifica la colision con el mob usando la posicion y tamaño de la hitbox actualizada, osea con la attackbox
                int mobIndex = e.game.collision.checkEntity(e, e.world.mobs);
                e.world.player.hitMob(mobIndex, e, e.weapon.knockbackValue, e.attack);

                int interactiveIndex = e.game.collision.checkEntity(e, e.world.interactives);
                e.world.player.hitInteractive(interactiveIndex);

                int projectileIndex = e.game.collision.checkEntity(e, e.world.projectiles);
                e.world.player.hitProjectile(projectileIndex);
            }

            // Despues de verificar la colision, resetea los datos originales
            e.x = currentX;
            e.y = currentY;
            e.hitbox.width = hitboxWidth;
            e.hitbox.height = hitboxHeight;
        }
        if (e.timer.attackAnimationCounter > e.motion2) {
            e.ss.attackNum = 1;
            e.timer.attackAnimationCounter = 0;
            e.flags.hitting = false;
        }
    }

    /**
     * Establece el knockback al objetivo del atacante.
     *
     * @param target         objetivo del atacante.
     * @param attacker       atacante de la entidad.
     * @param knockbackValue valor de knockback.
     */
    public void setKnockback(Entity target, Entity attacker, int knockbackValue) {
        target.knockbackDirection = attacker.direction;
        target.speed += knockbackValue;
        target.flags.knockback = true;
    }

    public void stopKnockback(Entity entity) {
        entity.flags.knockback = false;
        entity.speed = entity.defaultSpeed;
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
        return mob != null && mob.type == Type.NPC && player.flags.collidingOnMob
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
                if (player.y + player.hitbox.y + player.hitbox.height + mob.speed < mob.y + mob.hitbox.y) return true;
            }
            case UP -> {
                if (player.y + player.hitbox.y - mob.speed > mob.y + mob.hitbox.y + mob.hitbox.height) return true;
            }
            case LEFT -> {
                if (player.x + player.hitbox.x - mob.speed > mob.x + mob.hitbox.x + mob.hitbox.width) return true;
            }
            case RIGHT -> {
                if (player.x + player.hitbox.x + player.hitbox.width + mob.speed < mob.x + mob.hitbox.x) return true;
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
        player.speed = mob.speed;
        united = true;
        if (!(mob instanceof Rock)) {
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
    private void disunite(Entity player) {
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
