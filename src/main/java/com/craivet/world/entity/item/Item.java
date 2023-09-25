package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;

import static com.craivet.utils.Global.*;

/**
 * Los items se renderizan en el suelo y en el inventario.
 */

public class Item extends Entity {

    public Item loot;
    public String description;
    public int price;
    public int amount;
    public int lightRadius = 350;
    public int attackValue, defenseValue;
    public boolean solid, stackable;
    public boolean opened, empty;

    protected int value;

    public Item(Game game, World world, int col, int row) {
        super(game, world, col, row);
    }

    /**
     * Use the item.
     *
     * @param entity entity using the item.
     * @return true if can use it or false.
     */
    public boolean use(Entity entity) {
        return false;
    }

    /**
     * Set loot to chest.
     *
     * @param loot loot from the chest.
     */
    public void setLoot(Item loot) {
    }

    /**
     * Interact with the Player.
     */
    public void interact() {
    }

    /**
     * Detecta si el item especificado se encuentra en la posicion adyacente a la entidad.
     *
     * @param entity entidad.
     * @param items  lista de items.
     * @param name   nombre del item.
     * @return el indice del item especificado a la posicion adyacente de la entidad o -1 si no existe.
     */
    protected int detect(Entity entity, Item[][] items, String name) {
        // Verifica el item adyacente a la entidad
        int nextX = getLeftHitbox(entity);
        int nextY = getTopHitbox(entity);

        switch (entity.direction) {
            case DOWN -> nextY = getBottomHitbox(entity) + entity.stats.speed;
            case UP -> nextY = getTopHitbox(entity) - entity.stats.speed;
            case LEFT -> nextX = getLeftHitbox(entity) - entity.stats.speed;
            case RIGHT -> nextX = getRightHitbox(entity) + entity.stats.speed;
        }

        int row = nextY / tile;
        int col = nextX / tile;

        // Si el item iterado es igual a la posicion adyacente de la entidad
        for (int i = 0; i < items[1].length; i++) {
            if (items[world.map][i] != null)
                if (items[world.map][i].getRow() == row && items[world.map][i].getCol() == col && items[world.map][i].stats.name.equals(name))
                    return i;
        }

        return -1;
    }

    /**
     * Obtiene la posicion superior de la hitbox.
     *
     * @param entity entidad.
     * @return la posicion superior de la hitbox.
     */
    private int getTopHitbox(Entity entity) {
        return entity.pos.y + entity.hitbox.y;
    }

    /**
     * Obtiene la posicion inferior de la hitbox.
     *
     * @param entity entidad.
     * @return la posicion inferior de la hitbox.
     */
    private int getBottomHitbox(Entity entity) {
        return entity.pos.y + entity.hitbox.y + entity.hitbox.height;
    }

    /**
     * Obtiene la posicion izquierda de la hitbox.
     *
     * @param entity entidad.
     * @return la posicion izquierda de la hitbox.
     */
    private int getLeftHitbox(Entity entity) {
        return entity.pos.x + entity.hitbox.x;
    }

    /**
     * Obtiene la posicion derecha de la hitbox.
     *
     * @param entity entidad.
     * @return la posicion derecha de la hitbox.
     */
    private int getRightHitbox(Entity entity) {
        return entity.pos.x + entity.hitbox.x + entity.hitbox.width;
    }

    /**
     * Obtiene la fila de la entidad.
     *
     * @return la fila de la entiad.
     */
    private int getRow() {
        return (pos.y + hitbox.y) / tile;
    }

    /**
     * Obtiene la columna de la entidad.
     *
     * @return la columna de la entiad.
     */
    private int getCol() {
        return (pos.x + hitbox.x) / tile;
    }

}
