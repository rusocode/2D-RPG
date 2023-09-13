package com.craivet.world.entity.mob;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import java.net.URL;

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

    protected URL soundHit, soundDeath;

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
     * Checks if the player is within the mob's attack range. In case this is true, the mob can hit.
     *
     * @param vertical   vertical distance within attack range.
     * @param horizontal horizontal distance within the attack range.
     * @param rate       rate that determines if the mob attacks the player.
     */
    protected void isPlayerWithinAttackRange(int vertical, int horizontal, int rate) {
        boolean inRange = false;
        int xDis = getXDistance(world.player);
        int yDis = getYDistance(world.player);
        switch (direction) {
            case DOWN -> {
                /* Si la posicion y central del player es mayor a la posicion y central del mob, y si la distancia en y
                 * del player con respecto al mob es menor a la distancia vertical dentro del rango de ataque, y si la
                 * distancia en x del player con respecto al mob es menor a la distancia horizontal dentro del rango
                 * de ataque. */
                if (world.player.getCenterY() > getCenterY() && yDis < vertical && xDis < horizontal)
                    inRange = true;
            }
            case UP -> {
                if (world.player.getCenterY() < getCenterY() && yDis < vertical && xDis < horizontal)
                    inRange = true;
            }
            case LEFT -> {
                if (world.player.getCenterX() < getCenterX() && xDis < horizontal && yDis < vertical)
                    inRange = true;
            }
            case RIGHT -> {
                if (world.player.getCenterX() > getCenterX() && xDis < vertical && yDis < horizontal) // TODO No esta al reves?
                    inRange = true;
            }
        }
        // Si el player esta dentro del rango de ataque del mob y si el rate se cumple
        if (inRange && Utils.azar(rate) == 1) {
            flags.hitting = true;
            ss.movementNum = 1;
            timer.movementCounter = 0; // TODO O se referia al contador de ataque?
            timer.projectileCounter = 0;
        }
    }

    /**
     * Gets the target's goal row.
     *
     * @param target target.
     * @return the target's goal row.
     */
    protected int getGoalRow(Entity target) {
        return (target.y + target.hitbox.y) / tile;
    }

    /**
     * Gets the target's goal column.
     *
     * @param target target.
     * @return the target's goal column.
     */
    protected int getGoalCol(Entity target) {
        return (target.x + target.hitbox.x) / tile;
    }

    /**
     * Check if the mob starts following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     * @param rate     rate that determines if the mob follows the target.
     */
    protected void checkFollow(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.azar(rate) == 1) flags.following = true;
    }

    /**
     * Check if the mob stops following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     */
    protected void checkUnfollow(Entity target, int distance) {
        if (getTileDistance(target) > distance) flags.following = false;
    }

    /**
     * Mueve el mob hacia el player.
     * <p>
     * Si se completo el intervalo, verifica si la distancia en x del player con respecto al mob es mayor a la
     * distancia en y del player con respecto al mob. Si se cumple la anterior condicion, verifica si la posicion
     * central x del player es menor a la posicion central x del mob. Si se cumple la anterior condicion, cambia la
     * direccion del mob hacia la izquierda, y en caso contrario cambia la direccion del mob hacia la derecha. Pero si
     * la distancia en x del player con respecto al mob es menor a la distancia en y del player con respecto al mob,
     * entonces verifica si la posicion central y del player es menor a la posicion central y del mob. Si se cumple la
     * anterior condicion, cambia la direccion del mob hacia arriba, y en caso contrario cambia la direccion del mob
     * hacia abajo.
     * <p>
     * TODO Es necesario el intervalo?
     *
     * @param interval intervalo de tiempo en ms.
     */
    protected void moveTowardPlayer(int interval) {
        if (++timer.directionCounter > interval) { // TODO o =?
            if (getXDistance(game.world.player) > getYDistance(game.world.player))
                direction = game.world.player.getCenterX() < getCenterX() ? Direction.LEFT : Direction.RIGHT;
            else if (getXDistance(game.world.player) < getYDistance(game.world.player))
                direction = game.world.player.getCenterY() < getCenterY() ? Direction.UP : Direction.DOWN;
            timer.directionCounter = 0;
        }
    }

    /**
     * Gets the distance in tiles of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in tiles of the target with respect to the mob.
     */
    protected int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / tile;
    }

    /**
     * Gets the distance in x of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in x of the target with respect to the mob.
     */
    private int getXDistance(Entity target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }

    /**
     * Gets the distance in y of the target with respect to the mob.
     *
     * @param target target.
     * @return the distance in y of the target with respect to the mob.
     */
    private int getYDistance(Entity target) {
        return Math.abs(getCenterY() - target.getCenterY());
    }

}
