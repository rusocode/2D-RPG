package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;

import static com.punkipunk.utils.Global.tile;

/**
 * Items are rendered on the ground and in the inventory.
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
    protected int points;

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
     * Detects if the specified item is in the position adjacent to the entity.
     *
     * @param entity entity.
     * @param items  list of items.
     * @param name   name of the item.
     * @return the index of the specified item to the adjacent position of the entity or -1 if it does not exist.
     */
    protected int detect(Entity entity, Item[][] items, String name) {
        // Check the item adjacent to the entity
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

        // If the iterated item is equal to the adjacent position of the entity
        for (int i = 0; i < items[1].length; i++) {
            if (items[world.map.num][i] != null)
                if (items[world.map.num][i].getRow() == row && items[world.map.num][i].getCol() == col && items[world.map.num][i].stats.name.equals(name))
                    return i;
        }

        return -1;
    }

    /**
     * Gets the top position of the hitbox.
     *
     * @param entity entity.
     * @return the top position of the hitbox.
     */
    private int getTopHitbox(Entity entity) {
        return (int) (entity.pos.y + entity.hitbox.getY());
    }

    /**
     * Gets the bottom position of the hitbox.
     *
     * @param entity entity.
     * @return the bottom position of the hitbox.
     */
    private int getBottomHitbox(Entity entity) {
        return (int) (entity.pos.y + entity.hitbox.getY() + entity.hitbox.getHeight());
    }

    /**
     * Gets the left position of the hitbox.
     *
     * @param entity entity.
     * @return the left position of the hitbox.
     */
    private int getLeftHitbox(Entity entity) {
        return (int) (entity.pos.x + entity.hitbox.getX());
    }

    /**
     * Gets the right position of the hitbox.
     *
     * @param entity entity.
     * @return the right position of the hitbox.
     */
    private int getRightHitbox(Entity entity) {
        return (int) (entity.pos.x + entity.hitbox.getX() + entity.hitbox.getWidth());
    }

    /**
     * Gets the row of the entity.
     *
     * @return the entity row.
     */
    private int getRow() {
        return (int) ((pos.y + hitbox.getY()) / tile);
    }

    /**
     * Gets the entity column.
     *
     * @return the entity column.
     */
    private int getCol() {
        return (int) ((pos.x + hitbox.getX()) / tile);
    }

}
