package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.util.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import static com.craivet.util.Global.*;

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

    /**
     * Se mueve.
     *
     * @param direction direccion hacia donde se mueve.
     */
    protected void move(int direction) {
    }

    /**
     * Dialoga.
     */
    public void dialogue() {
    }

    /**
     * Reacciona al daño.
     */
    public void damageReaction() {
    }

    /**
     * Mira al Player.
     */
    protected void lookPlayer() {
        switch (world.player.direction) {
            case DOWN -> direction = UP;
            case UP -> direction = DOWN;
            case LEFT -> direction = RIGHT;
            case RIGHT -> direction = LEFT;
        }
    }

    /**
     * Comprueba si puede atacar o no verificando si el objetivo esta dentro del rango especificado.
     *
     * @param rate       probabilidad de que el mob ataque.
     * @param vertical   indica la distancia vertical.
     * @param horizontal indica la distancia horizontal.
     */
    protected void checkAttackOrNot(int rate, int vertical, int horizontal) {
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
        if (targetInRage) {
            if (Utils.azar(rate) == 1) {
                flags.hitting = true;
                movementNum = 1;
                timer.movementCounter = 0; // TODO O se referia al contador de ataque?
                timer.projectileCounter = 0;
            }
        }
    }

    /**
     * Obtiene la fila del objetivo.
     *
     * @param target objetivo.
     * @return la fila del objetivo.
     */
    protected int getGoalRow(Entity target) {
        return (target.y + target.hitbox.y) / tile_size;
    }

    /**
     * Obtiene la columna del objetivo.
     *
     * @param target objetivo.
     * @return la columna del objetivo.
     */
    protected int getGoalCol(Entity target) {
        return (target.x + target.hitbox.x) / tile_size;
    }

    /**
     * Comprueba si comienza a seguir al objetivo.
     *
     * @param target   objetivo.
     * @param distance distancia en tiles.
     * @param rate     la tasa que determina si sigue al objetivo.
     */
    protected void checkFollow(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.azar(rate) == 1) flags.following = true;
    }

    /**
     * Comprueba si deja de seguir al objetivo.
     *
     * @param target   objetivo.
     * @param distance distancia en tiles.
     */
    protected void checkUnfollow(Entity target, int distance) {
        if (getTileDistance(target) > distance) flags.following = false;
    }

    /**
     * Obtiene la distancia del objetivo en tiles.
     *
     * @param target objetivo.
     * @return la distancia del objetivo en tiles.
     */
    private int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / tile_size;
    }

    /**
     * Obtiene la diferencia entre la posicion x de la entidad actual y la posicion x del objetivo.
     *
     * @param target objetivo.
     * @return la diferencia entre la posicion x de la entidad actual y la posicion x del objetivo.
     */
    private int getXDistance(Entity target) {
        return Math.abs(x - target.x);
    }

    /**
     * Obtiene la diferencia entre la posicion y de la entidad actual y la posicion y del objetivo.
     *
     * @param target objetivo.
     * @return la diferencia entre la posicion y de la entidad actual y la posicion y del objetivo.
     */
    private int getYDistance(Entity target) {
        return Math.abs(y - target.y);
    }

}
