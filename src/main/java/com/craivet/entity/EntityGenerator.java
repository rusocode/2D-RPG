package com.craivet.entity;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.item.*;

public class EntityGenerator {

    private final Game game;
    private final World world;

    public EntityGenerator(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Obtiene el item.
     *
     * @param name nombre del item.
     * @return el item.
     */
    public Item getItem(String name) {
        Item item = null;
        switch (name) {
            case Axe.item_name -> item = new Axe(game, world);
            case Boots.item_name -> item = new Boots(game, world);
            case Chest.item_name -> item = new Chest(game, world);
            case Gold.item_name -> item = new Gold(game, world);
            case Door.item_name -> item = new Door(game, world);
            case DoorIron.item_name -> item = new DoorIron(game, world);
            case Key.item_name -> item = new Key(game, world, 1);
            case Lantern.item_name -> item = new Lantern(game, world);
            case Pickaxe.item_name -> item = new Pickaxe(game, world);
            case PotionRed.item_name -> item = new PotionRed(game, world, 1);
            case ShieldBlue.item_name -> item = new ShieldBlue(game, world);
            case ShieldWood.item_name -> item = new ShieldWood(game, world);
            case Stone.item_name -> item = new Stone(game, world);
            case SwordNormal.item_name -> item = new SwordNormal(game, world);
            case Tent.item_name -> item = new Tent(game, world);
        }
        return item;
    }

}
