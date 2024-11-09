package com.punkipunk.world.entity.mob;

import com.punkipunk.Direction;
import com.punkipunk.Game;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;

import java.net.URL;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Mob (Enemy/Monster or NPC - Non-Playable Character): A "mob" (short for "mobile" or "mobility") is a term generally used to
 * refer to characters or creatures in the game that are not controlled by the player. Mobs can include enemies, monsters,
 * non-playable characters (NPCs), allies controlled by artificial intelligence (AI), and other in-game characters. Mobs can have
 * varied roles, such as being enemies that the player must defeat, vendors in a city, allies on a mission, etc.
 * <p>
 * For this game the mobs are divided into
 * <ul>
 * <li>NPC: entities with which you interact, trade, etc. TODO or NO_HOSTILE?
 * <li>HOSTILE: are aggressive entities that can attack and be attacked.
 * </ul>
 * <p>
 * TODO Is it possible to create the enum here?
 * <p>
 * TODO I could create a MobStats class and create it from here, where that class will only have the attributes of mobs
 */

public class Mob extends Entity {

    public URL soundHit, soundDeath;

    public Mob(Game game, World world, int col, int row) {
        super(game, world, col, row);
    }

    public Mob(Game game, World world) {
        super(game, world);
    }

    /**
     * Moves the mob in a specified direction.
     *
     * @param direction direction in which it moves.
     */
    public void move(Direction direction) {
    }

    /**
     * Dialogue.
     */
    public void dialogue() {
    }

    /**
     * React to damage.
     */
    public void damageReaction() {
    }

    /**
     * Look at the Player.
     */
    protected void lookPlayer() {
        switch (world.entities.player.direction) {
            case DOWN -> direction = Direction.UP;
            case UP -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    /**
     * Checks if the player is within the mob's attack range. In case this is true, the mob can hit.
     *
     * @param interval   interval between each attack.
     * @param vertical   vertical distance within attack range.
     * @param horizontal horizontal distance within the attack range.
     * @param rate       rate that determines if the mob attacks the player.
     */
    protected void isPlayerWithinAttackRange(int interval, int vertical, int horizontal, int rate) {
        if (++timer.attackCounter > interval) {
            if (getXDistance(world.entities.player) < horizontal && getYDistance(world.entities.player) < vertical && Utils.random(rate) == 1) {
                flags.hitting = true;
                timer.projectileCounter = 0;
                timer.attackCounter = 0;
            }
        }
    }

    /**
     * Gets the target's goal row.
     *
     * @param target target.
     * @return the target's goal row.
     */
    protected int getGoalRow(Entity target) {
        return (int) ((target.pos.y + target.hitbox.getY()) / tile);
    }

    /**
     * Gets the target's goal column.
     *
     * @param target target.
     * @return the target's goal column.
     */
    protected int getGoalCol(Entity target) {
        return (int) ((target.pos.x + target.hitbox.getX()) / tile);
    }

    /**
     * Check if the mob starts following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     * @param rate     rate that determines if the mob follows the target.
     */
    protected void checkFollow(Mob target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.random(rate) == 1) flags.following = true;
    }

    /**
     * Check if the mob stops following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     */
    protected void checkUnfollow(Mob target, int distance) {
        if (getTileDistance(target) > distance) flags.following = false;
    }

    /**
     * Move the mob towards the player.
     * <p>
     * If the interval is complete, check if the distance in x of the player with respect to the mob is greater than the distance
     * in y of the player with respect to the mob. If the previous condition is met, check if the central position x of the player
     * is less than the central position x of the mob. If the previous condition is met, change the direction of the mob to the
     * left, and otherwise change the direction of the mob to the right. But if the distance in x of the player with respect to
     * the mob is less than the distance in y of the player with respect to the mob, then check if the central position y of the
     * player is less than the central position y of the mob. If the previous condition is met, change the direction of the mob
     * upwards, and otherwise change the direction of the mob downwards.
     * <p>
     * The interval prevents the mob from changing direction abruptly.
     *
     * @param target   target.
     * @param interval time interval in ms.
     */
    protected void moveTowardPlayer(Mob target, int interval) {
        if (++timer.directionCounter > interval) { // TODO o =?
            if (getXDistance(game.system.world.entities.player) > getYDistance(game.system.world.entities.player))
                direction = target.getCenterX() < getCenterX() ? Direction.LEFT : Direction.RIGHT;
            else if (getXDistance(game.system.world.entities.player) < getYDistance(game.system.world.entities.player))
                direction = target.getCenterY() < getCenterY() ? Direction.UP : Direction.DOWN;
            timer.directionCounter = 0;
        }
    }

    /**
     * Gets the distance in tiles of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in tiles of the target with respect to the mob.
     */
    protected int getTileDistance(Mob target) {
        return (getXDistance(target) + getYDistance(target)) / tile;
    }

    /**
     * Gets the distance in x of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in x of the target with respect to the mob.
     */
    private int getXDistance(Mob target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }

    /**
     * Gets the distance in y of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in y of the target with respect to the mob.
     */
    private int getYDistance(Mob target) {
        return Math.abs(getCenterY() - target.getCenterY());
    }


    /**
     * Gets the central position of x of the entity.
     *
     * @return the center x position of the entity.
     */
    private int getCenterX() {
        return (int) (pos.x + sheet.frame.getWidth() / 2);
    }

    /**
     * Gets the central position of y of the entity.
     *
     * @return the central position of y of the entity.
     */
    private int getCenterY() {
        return (int) (pos.y + sheet.frame.getHeight() / 2);
    }

}
