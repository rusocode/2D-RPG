package com.craivet.world.entity.mob;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import static com.craivet.utils.Global.*;

/**
 * Los mobs se dividen en
 * <ul>
 * <li>PLAYER: jugador principal.
 * <li>NPC: entidades con las que se interactura, comercia, etc.
 * <li>HOSTILE: son entidades agresivas que pueden atacar y ser atacadas.
 * </ul>
 * <p>
 * TODO Es posible crear el enum aca?
 */

public class Mob extends Entity {

    public Mob(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

    public Mob(Game game, World world) {
        super(game, world);
    }

    /**
     * Moves the mob in a specified direction.
     *
     * @param direction direction in which it moves.
     */
    protected void move(Direction direction) {
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
        switch (world.player.direction) {
            case DOWN -> direction = Direction.UP;
            case UP -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    /**
     * It checks if it can attack or not by checking if the target is within the specified range.
     *
     * @param vertical   indicates the vertical distance.
     * @param horizontal indicates the horizontal distance.
     * @param rate       rate that determines if the mob attacks.
     */
    protected void checkAttackOrNot(int vertical, int horizontal, int rate) {
        boolean targetInRage = false;
        int xDis = getXDistance(world.player);
        int yDis = getYDistance(world.player);
        switch (direction) {
            case DOWN -> {
                if (world.player.y > y && yDis < vertical && xDis < horizontal) targetInRage = true;
            }
            case UP -> {
                if (world.player.y < y && yDis < vertical && xDis < horizontal) targetInRage = true;
            }
            case LEFT -> {
                if (world.player.x < x && xDis < vertical && yDis < horizontal) targetInRage = true;
            }
            case RIGHT -> {
                if (world.player.x > x && xDis < vertical && yDis < horizontal) targetInRage = true;
            }
        }
        // Calcula la probabilidad de atacar si el objetivo esta dentro del rango
        if (targetInRage && Utils.azar(rate) == 1) {
            flags.hitting = true;
            ss.movementNum = 1;
            timer.movementCounter = 0; // TODO O se referia al contador de ataque?
            timer.projectileCounter = 0;
        }
    }

    /**
     * Gets the row of the target.
     *
     * @param target target.
     * @return the target row.
     */
    protected int getGoalRow(Entity target) {
        return (target.y + target.hitbox.y) / tile;
    }

    /**
     * Gets the target column.
     *
     * @param target target.
     * @return the target column.
     */
    protected int getGoalCol(Entity target) {
        return (target.x + target.hitbox.x) / tile;
    }

    /**
     * Check if it starts following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     * @param rate     rate that determines if follows the target.
     */
    protected void checkFollow(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.azar(rate) == 1) flags.following = true;
    }

    /**
     * Check if it stops following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     */
    protected void checkUnfollow(Entity target, int distance) {
        if (getTileDistance(target) > distance) flags.following = false;
    }

    /**
     * Gets the distance of the target in tiles.
     *
     * @param target target.
     * @return the distance of the target in tiles.
     */
    private int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / tile;
    }

    /**
     * Gets the difference between the x position of the mob and the x position of the target.
     *
     * @param target target.
     * @return the difference between the x position of the mob and the x position of the target.
     */
    private int getXDistance(Entity target) {
        return Math.abs(x - target.x);
    }

    /**
     * Gets the difference between the y position of the mob and the y position of the target.
     *
     * @param target target.
     * @return the difference between the y position of the mob and the y position of the target.
     */
    private int getYDistance(Entity target) {
        return Math.abs(y - target.y);
    }

}
