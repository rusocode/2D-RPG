package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.json.JsonLoader;
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

    public Item loot;
    public ItemCategory itemCategory;
    public String description; // TODO Donde se usa?
    public String soundSwing;
    public String soundDraw;
    public int price; // TODO Donde se usa?
    public int amount;
    public int lightRadius;
    public int attack, defense;
    public boolean solid, stackable;
    public boolean opened, empty;
    public SpriteSheet ss;
    protected ItemData itemData;
    protected int points; // Puntos de las pociones azules y rojas que se usan para incrementar hp y mana

    public Item(IGame game, World world, int... pos) {
        super(game, world, pos);

        this.itemData = JsonLoader.getInstance().deserialize("items." + getID().name, ItemData.class);
        this.itemCategory = getID().category;

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

    public abstract ItemID getID();

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

    protected boolean isNearby(Entity entity, ItemID id) {
        return world.entitySystem.getItems(world.map.id).stream()
                .filter(item -> item.getID() == id) // La comparacion de enums es mas rapida que la comparacion de strings
                .anyMatch(item -> isAdjacentTo(entity, item));
    }

    protected boolean isAdjacentTo(Entity entity, Item item) {
        // Verificar el item adyacente a la entidad
        int nextX = getLeftHitbox(entity);
        int nextY = getTopHitbox(entity);

        switch (entity.direction) {
            case DOWN -> nextY = getBottomHitbox(entity) + entity.stats.speed;
            case UP -> nextY = getTopHitbox(entity) - entity.stats.speed;
            case LEFT -> nextX = getLeftHitbox(entity) - entity.stats.speed;
            case RIGHT -> nextX = getRightHitbox(entity) + entity.stats.speed;
        }

        int itemRow = (int) (item.position.y + item.hitbox.getY()) / tile;
        int itemCol = (int) (item.position.x + item.hitbox.getX()) / tile;

        int entityRow = nextY / tile;
        int entityCol = nextX / tile;

        return itemRow == entityRow && itemCol == entityCol;
    }

    /**
     * Gets the top position of the hitbox.
     *
     * @param entity entity.
     * @return the top position of the hitbox.
     */
    private int getTopHitbox(Entity entity) {
        return (int) (entity.position.y + entity.hitbox.getY());
    }

    /**
     * Gets the bottom position of the hitbox.
     *
     * @param entity entity.
     * @return the bottom position of the hitbox.
     */
    private int getBottomHitbox(Entity entity) {
        return (int) (entity.position.y + entity.hitbox.getY() + entity.hitbox.getHeight());
    }

    /**
     * Gets the left position of the hitbox.
     *
     * @param entity entity.
     * @return the left position of the hitbox.
     */
    private int getLeftHitbox(Entity entity) {
        return (int) (entity.position.x + entity.hitbox.getX());
    }

    /**
     * Gets the right position of the hitbox.
     *
     * @param entity entity.
     * @return the right position of the hitbox.
     */
    private int getRightHitbox(Entity entity) {
        return (int) (entity.position.x + entity.hitbox.getX() + entity.hitbox.getWidth());
    }

}
