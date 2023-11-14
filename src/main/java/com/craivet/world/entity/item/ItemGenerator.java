package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;

/**
 * Create new items when purchasing or collecting. This avoids using the same reference when purchasing an item.
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
        Item item = null;
        switch (name) {
            case Axe.NAME -> item = new Axe(game, world);
            case Boots.NAME -> item = new Boots(game, world);
            case Chest.NAME -> item = new Chest(game, world);
            case Chicken.NAME -> item = new Chicken(game, world);
            case Gold.NAME -> item = new Gold(game, world);
            case Door.NAME -> item = new Door(game, world);
            case DoorIron.NAME -> item = new DoorIron(game, world);
            case Key.NAME -> item = new Key(game, world, 0);
            case Lantern.NAME -> item = new Lantern(game, world);
            case Pickaxe.NAME -> item = new Pickaxe(game, world);
            /* In the case of trading with the Trader and buying 1 at a time, it would be inefficient since 1 object is
             * created per potion, therefore it would be good for the player to be able to determine the quantity to buy.
             * It is important to pass 0 when it is generated to only create the object and not specify a quantity. */
            case PotionRed.NAME -> item = new PotionRed(game, world, 0);
            case ShieldIron.NAME -> item = new ShieldIron(game, world);
            case ShieldWood.NAME -> item = new ShieldWood(game, world);
            case Stone.NAME -> item = new Stone(game, world, 0);
            case SwordIron.NAME -> item = new SwordIron(game, world);
            case Tent.NAME -> item = new Tent(game, world);
            case Wood.NAME -> item = new Wood(game, world, 0);
        }
        return item;
    }
}
