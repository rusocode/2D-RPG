package com.punkipunk.physics;

import com.punkipunk.entity.Entity;
import com.punkipunk.entity.mob.Box;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.mob.Trader;

public class Mechanics {

    // Indica cuando el player esta "unido" al mob
    private boolean united;

    /**
     * Sets the knockback to the attacker's target.
     *
     * @param target         target of the attacker.
     * @param attacker       attacker of the entity.
     * @param knockbackValue knockback value.
     */
    public void setKnockback(Entity target, Entity attacker, int knockbackValue) {
        target.direction.knockbackDirection = attacker.direction;
        target.stats.speed += knockbackValue;
        target.flags.knockback = true;
    }

    public void stopKnockback(Entity entity) {
        entity.flags.knockback = false;
        entity.stats.speed = entity.stats.baseSpeed;
        entity.timer.knockbackCounter = 0;
    }

    /**
     * <p>
     * Comprueba la velocidad del player cuando colisiona con un mob que se mueve en la misma direccion. Esto se hace para evitar
     * un "tartamudeo" en la animacion del movimiento del player. En ese caso, "une" el player al mob. De lo contrario, "desune"
     * al player del mob.
     */
    public void checkSpeed(Entity player, Entity mob) {
        if (checkConditionsForUnion(player, mob)) unite(player, mob);
        else disunite(player, mob);
    }

    /**
     * <p>
     * Comprueba si el mob es distinto a null, y si es un NPC, y si no es un Box o un Trader, y si el player colisiona con el mob,
     * y si el player esta en la misma direccion que el mob, y si el player no tiene distancia con el mob y si el mob no
     * colisiono.
     * <p>
     * FIXME Se genera un "tartamudeo" cuando se mueve el Box
     * FIXME Si agrego la condicion {@code mob.mobType == MobType.PEACEFUL}, entonces cuando ataco al Bat, la velocidad del player se iguala al knockback
     */
    private boolean checkConditionsForUnion(Entity player, Entity mob) {
        return mob != null
                && (mob.mobCategory == MobCategory.NPC /* || mob.mobType == MobType.PEACEFUL*/)
                && !(mob instanceof Box || mob instanceof Trader)
                && player.flags.collidingOnMob
                && player.direction == mob.direction
                && !isDistanceWithMob(player, mob)
                && !mob.flags.colliding;
    }

    /**
     * Check if there is distance with the Mob.
     * <p>
     * <h3>What is he doing this for?</h3>
     * When it follows (always colliding) the Mob but at some point stops following it and it stays in the same direction, the
     * speed will remain the same as that of the Mob. So to solve this problem, the distance is checked, and if there is distance
     * between the Player and the Mob, it returns to the default speed.
     *
     * @param mob current mob.
     * @return true if there is distance or false.
     */
    private boolean isDistanceWithMob(Entity player, Entity mob) {
        switch (mob.direction) {
            case DOWN -> {
                if (player.position.y + player.hitbox.getY() + player.hitbox.getHeight() + player.stats.speed < mob.position.y + mob.hitbox.getY())
                    return true;
            }
            case UP -> {
                if (player.position.y + player.hitbox.getY() - player.stats.speed > mob.position.y + mob.hitbox.getY() + mob.hitbox.getHeight())
                    return true;
            }
            case LEFT -> {
                if (player.position.x + player.hitbox.getX() - player.stats.speed > mob.position.x + mob.hitbox.getX() + mob.hitbox.getWidth())
                    return true;
            }
            case RIGHT -> {
                if (player.position.x + player.hitbox.getX() + player.hitbox.getWidth() + player.stats.speed < mob.position.x + mob.hitbox.getX())
                    return true;
            }
        }
        return false;
    }

    /**
     * Une el player al mob.
     * <p>
     * Agrega la velocidad del mob al player, y dependiendo de la direccion, agrega o resta un pixel. Esto ultimo se hace para que
     * el player pueda dialogar si el mob es un NPC y se mueve.
     */
    private void unite(Entity player, Entity mob) {
        player.stats.speed = mob.stats.speed;
        united = true;
        switch (player.direction) {
            case DOWN -> player.position.y++;
            case UP -> player.position.y--;
            case LEFT -> player.position.x--;
            case RIGHT -> player.position.x++;
        }
    }

    /**
     * Desune el player del mob.
     * <p>
     * Regrese a la velocidad base y verifica si estan unidos para "desbloquear" ambas entidades restando o agregando un pixel.
     */
    private void disunite(Entity player, Entity mob) {
        player.stats.speed = player.stats.baseSpeed;
        player.flags.collidingOnMob = false;
        if (united) {
            switch (player.direction) {
                case DOWN -> player.position.y--;
                case UP -> player.position.y++;
                case LEFT -> player.position.x++;
                case RIGHT -> player.position.x--;
            }
            united = false;
        }
    }

}
