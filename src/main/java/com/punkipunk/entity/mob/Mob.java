package com.punkipunk.entity.mob;

import com.punkipunk.Direction;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * En videojuegos, un "mob" (abreviatura de "mobile" o "mobiles") es un termino que se refiere a cualquier criatura o personaje
 * controlado por la computadora que los jugadores pueden encontrar durante el juego. El termino se origino en los primeros MUDs
 * (Multi-User Dungeons) y se popularizo con juegos como World of Warcraft y Minecraft.
 * <p>
 * Los mobs comparten caracteristicas comunes implementadas en la clase base {@code Mob}:
 * <ul>
 * <li>Sistema de movimiento
 * <li>Inteligencia artificial para seguimiento
 * <li>Sistema de sonidos (golpes, muerte)
 * <li>Capacidad de interaccion con el jugador
 * <li>Animaciones y estados
 * </ul>
 * La implementacion en el codigo sigue patrones comunes en juegos RPG, donde los mobs son fundamentales para crear una
 * experiencia de juego completa y variada.
 * <p>
 * TODO Es eficiente crear los mobs por clases o es mejor meterlos todos en el json?
 */

public abstract class Mob extends Entity {

    protected MobData mobData;

    public Mob(IGame game, World world, int... pos) {
        super(game, world, pos);

        this.mobData = JsonLoader.getInstance().deserialize("mobs." + getID().name, MobData.class);

        // La categoria se obtiene automaticamente del id
        this.mobCategory = getID().category;

        stats.name = mobData.name();
        stats.speed = stats.baseSpeed = mobData.speed();
        stats.hp = stats.maxHp = mobData.hp();
        stats.exp = mobData.exp();
        stats.attack = mobData.attack();
        stats.defense = mobData.defense();
        flags.boss = mobData.boss();
        sleep = mobData.sleep();

        if (mobData.hitbox() != null) {
            hitbox = new Rectangle(
                    mobData.hitbox().x(),
                    mobData.hitbox().y(),
                    mobData.hitbox().width(),
                    mobData.hitbox().height()
            );
            hitboxDefaultX = hitbox.getX();
            hitboxDefaultY = hitbox.getY();
        }

    }

    public abstract MobID getID();

    /**
     * Moves the mob in a specified direction.
     *
     * @param direction direction in which it moves.
     */
    public void move(Entity entity, Direction direction) {
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
        switch (world.entitySystem.player.direction) {
            case DOWN -> direction = Direction.UP;
            case UP -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    /**
     * Comprueba si el jugador se encuentra dentro del rango de ataque del enemigo. En caso de que esto sea asi, el enemigo puede
     * atacar.
     *
     * @param interval   interval between each attack.
     * @param vertical   vertical distance within attack range.
     * @param horizontal horizontal distance within the attack range.
     * @param rate       rate that determines if the mob attacks the player.
     */
    protected void isPlayerWithinAttackRange(int interval, int vertical, int horizontal, int rate) {
        if (++timer.attackCounter > interval) {
            if (getXDistance(world.entitySystem.player) < horizontal && getYDistance(world.entitySystem.player) < vertical && Utils.random(rate) == 1) {
                flags.hitting = true;
                timer.projectileCounter = 0;
                timer.attackCounter = 0;
            }
        }
    }

    /**
     * Gets the target's goal y.
     *
     * @param target target.
     * @return the target's goal y.
     */
    protected int getGoalRow(Entity target) {
        return (int) ((target.position.y + target.hitbox.getY()) / tile);
    }

    /**
     * Gets the target's goal column.
     *
     * @param target target.
     * @return the target's goal column.
     */
    protected int getGoalCol(Entity target) {
        return (int) ((target.position.x + target.hitbox.getX()) / tile);
    }

    /**
     * Check if the mob starts following the target.
     *
     * @param target   target.
     * @param distance distance in tiles.
     * @param rate     rate that determines if the mob follows the target.
     */
    protected void checkFollow(Entity target, int distance, int rate) {
        if (getTileDistance(target) < distance && Utils.random(rate) == 1) flags.following = true;
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
    protected void moveTowardPlayer(Entity target, int interval) {
        if (++timer.directionCounter > interval) { // TODO o =?
            if (getXDistance(game.getGameSystem().world.entitySystem.player) > getYDistance(game.getGameSystem().world.entitySystem.player))
                direction = target.getCenterX() < getCenterX() ? Direction.LEFT : Direction.RIGHT;
            else if (getXDistance(game.getGameSystem().world.entitySystem.player) < getYDistance(game.getGameSystem().world.entitySystem.player))
                direction = target.getCenterY() < getCenterY() ? Direction.UP : Direction.DOWN;
            timer.directionCounter = 0;
        }
    }


}
