package com.craivet.world.entity.mob;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import java.net.URL;

import static com.craivet.utils.Global.*;

/**
 * Mob (Enemigo o NPC - Personaje No Jugable): Un "mob" (termino abreviado de "mobile" o "mobility") es un termino
 * generalmente utilizado para referirse a personajes o criaturas en el juego que no son controlados por el jugador. Los
 * mobs pueden incluir enemigos, monstruos, personajes no jugables (NPC), aliados controlados por la inteligencia
 * artificial (IA), y otros personajes dentro del juego. Los mobs pueden tener roles variados, como ser enemigos que el
 * jugador debe derrotar, vendedores en una ciudad, aliados en una mision, etc.
 * <p>
 * Para este juego los mobs se dividen en
 * <ul>
 * <li>NPC: entidades con las que se interactura, comercia, etc. TODO o NO_HOSTILE
 * <li>HOSTILE: son entidades agresivas que pueden atacar y ser atacadas.
 * </ul>
 * <p>
 * TODO Es posible crear el enum aca?
 * TODO Podria crear una clase MobStats y crearla desde aca, en donde esa clase solo va a tener los atributos de mobs
 */

public class Mob extends Entity {

    public URL soundHit, soundDeath;
    public boolean boss;

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
     * @param interval   interval between each attack.
     * @param vertical   vertical distance within attack range.
     * @param horizontal horizontal distance within the attack range.
     * @param rate       rate that determines if the mob attacks the player.
     */
    protected void isPlayerWithinAttackRange(int interval, int vertical, int horizontal, int rate) {
        if (++timer.attackCounter > interval) {
            if (getXDistance(world.player) < horizontal && getYDistance(world.player) < vertical && Utils.random(rate) == 1) {
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
        return (target.pos.y + target.hitbox.y) / tile;
    }

    /**
     * Gets the target's goal column.
     *
     * @param target target.
     * @return the target's goal column.
     */
    protected int getGoalCol(Entity target) {
        return (target.pos.x + target.hitbox.x) / tile;
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
     * El intervalo evita que el mob cambie de direccion de manera brusca.
     *
     * @param target   target.
     * @param interval intervalo de tiempo en ms.
     */
    protected void moveTowardPlayer(Mob target, int interval) {
        if (++timer.directionCounter > interval) { // TODO o =?
            if (getXDistance(game.world.player) > getYDistance(game.world.player))
                direction = target.getCenterX() < getCenterX() ? Direction.LEFT : Direction.RIGHT;
            else if (getXDistance(game.world.player) < getYDistance(game.world.player))
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
     * Obtiene la posicion central de x de la entidad.
     *
     * @return la posicion central de x de la entidad.
     */
    private int getCenterX() {
        return pos.x + sheet.frame.getWidth() / 2;
    }

    /**
     * Obtiene la posicion central de y de la entidad.
     *
     * @return la posicion central de y de la entidad.
     */
    private int getCenterY() {
        return pos.y + sheet.frame.getHeight() / 2;
    }

}
