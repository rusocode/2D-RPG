package com.craivet.physics;

import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.mob.Box;

/**
 * Game mechanics.
 */

public class Mechanics {

    // Indicates when the Player is "united" to the Mob
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
        entity.stats.speed = entity.stats.defaultSpeed;
        entity.timer.knockbackCounter = 0;
    }

    /**
     * Check the speed of the Player's direction when it collides with a Mob moving in the same direction. This is done
     * to avoid "stuttering" in the Player's movement animation. In that case, "united" the Player to the Mob. Otherwise,
     * "disunite" the Player from the Mob.
     *
     * @param mob current mob.
     */
    public void checkDirectionSpeed(Entity player, Entity mob) {
        if (checkConditionsForUnion(player, mob)) unite(player, mob);
        else disunite(player);
    }

    /**
     * Checks if the Mob is other than null, and if the Mob is an Npc, and if the Player is colliding with the Mob, and
     * if the Player is in the same direction as the Mob, and if the Player has no distance from it Mob and if the Mob
     * did not collide.
     *
     * @param mob current mob.
     * @return true if all specified conditions are met or false.
     */
    private boolean checkConditionsForUnion(Entity player, Entity mob) {
        return mob != null && mob.type == Type.NPC && player.flags.collidingOnMob
                && player.direction == mob.direction && !isDistanceWithMob(player, mob) && !mob.flags.colliding;
    }

    /**
     * Check if there is distance with the Mob.
     * <p>
     * <h3>What is he doing this for?</h3>
     * When it follows (always colliding) the Mob but at some point stops following it and it stays in the same
     * direction, the speed will remain the same as that of the Mob. So to solve this problem, the distance is checked,
     * and if there is distance between the Player and the Mob, it returns to the default speed.
     *
     * @param mob current mob.
     * @return true if there is distance or false.
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
     * Join the Player to the Mob.
     * <p>
     * Matches the speed of the Mob to that of the Player and depending on the direction, adds or subtracts a pixel. The
     * latter is done so that the Player can dialogue if the Mob is an Npc and it is moving.
     *
     * @param mob current mob.
     */
    private void unite(Entity player, Entity mob) {
        player.stats.speed = mob.stats.speed;
        united = true;
        if (!(mob instanceof Box)) {
            switch (player.direction) {
                case DOWN -> player.pos.y++;
                case UP -> player.pos.y--;
                case LEFT -> player.pos.x--;
                case RIGHT -> player.pos.x++;
            }
        }
    }

    /**
     * Disunite the Player from the Mob.
     * <p>
     * Return to the default speed and check if they are joined to "unlock" both entities by subtracting or adding a
     * pixel.
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
