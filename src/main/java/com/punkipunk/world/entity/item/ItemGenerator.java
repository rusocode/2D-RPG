package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.world.World;

/**
 * Create new items when purchasing or collecting. This avoids using the same reference when purchasing an item.
 * <p>
 * TODO Se tendria que unir con Item (ojo! que al hacerlo cuando se carga el juego, los items se mueven rarisimo)
 */

public class ItemGenerator {

    private final Game game;
    private final World world;

    public ItemGenerator(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Generates a new item.
     *
     * @param name name of the item.
     * @return a new item.
     */
    public Item generate(String name) {
        return switch (name) {
            case IronAxe.NAME -> new IronAxe(game, world);
            case Boots.NAME -> new Boots(game, world);
            case Chest.NAME -> new Chest(game, world);
            case Chicken.NAME -> new Chicken(game, world);
            case Gold.NAME -> new Gold(game, world, 0);
            case Door.NAME -> new Door(game, world);
            case IronDoor.NAME -> new IronDoor(game, world);
            case Key.NAME -> new Key(game, world, 0);
            case Lantern.NAME -> new Lantern(game, world);
            case IronPickaxe.NAME -> new IronPickaxe(game, world);
            case PotionBlue.NAME -> new PotionBlue(game, world, 0);
            /* In the case of trading with the Trader and buying 1 at a time, it would be inefficient since 1 object is
             * created per potion, therefore it would be good for the player to be able to determine the quantity to buy.
             * It is important to pass 0 when it is generated to only create the object and not specify a quantity. */
            case PotionRed.NAME -> new PotionRed(game, world, 0);
            case IronShield.NAME -> new IronShield(game, world);
            case WoodShield.NAME -> new WoodShield(game, world);
            case Stone.NAME -> new Stone(game, world, 0);
            case IronSword.NAME -> new IronSword(game, world);
            case Tent.NAME -> new Tent(game, world);
            case Wood.NAME -> new Wood(game, world, 0);
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }
}