package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * Los items se muestran en el suelo y en los contenedores.
 * <p>
 * Para especificar la textura del item en el suelo con un tamaño mas pequeño:
 * <pre>{@code
 * sheet.frame = pos.length > 0 ? Utils.scaleImage(key, tile / 2, tile / 2) : Utils.scaleImage(key, tile, tile);
 * }</pre>
 */

public abstract class Item extends Entity {

    public ItemData itemData;
    public Item loot;
    public ItemType itemType;
    public String description; // TODO Donde se usa?
    public String sound;
    public int price; // TODO Donde se usa?
    public int amount;
    public int lightRadius;
    public int attack, defense;
    public boolean solid, stackable;
    public boolean opened, empty;
    public SpriteSheet ss;
    protected int points; // Puntos de las pociones azules y rojas que se usan para incrementar hp y mana

    public Item(Game game, World world, ItemData itemData, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);

        this.itemData = itemData;

        stats.name = itemData.name();
        description = itemData.description();
        price = itemData.price();
        attack = itemData.attack();
        defense = itemData.defense();
        stats.knockback = itemData.knockback();
        points = itemData.points();
        lightRadius = itemData.lightRadius();
        stackable = itemData.stackable();
        solid = itemData.solid();
        if (itemData.texturePath() != null) sheet.frame = Utils.loadTexture(itemData.texturePath());
        // TODO No se esta creando dos veces para el Chest?
        if (itemData.spriteSheetPath() != null) ss = new SpriteSheet(Utils.loadTexture(itemData.spriteSheetPath()));

        // Crea un Hitbox solo para los items de tipo OBSTACLE
        if (itemData.hitbox() != null) {
            hitbox = new Rectangle(
                    itemData.hitbox().x(),
                    itemData.hitbox().y(),
                    itemData.hitbox().width(),
                    itemData.hitbox().height()
            );
            hitboxDefaultX = hitbox.getX();
            hitboxDefaultY = hitbox.getY();
        }

        // TODO Cambiar nombre del "stone_sword_frame.png" a algo mas relacionado a un SS

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
