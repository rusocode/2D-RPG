package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;

/**
 * Crea nuevos objetos al comerciar con el Trader, evitando asi utilizar la misma referencia del item comprado.
 */

public class ItemGenerator {

    private final Game game;
    private final World world;

    public ItemGenerator(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Genera un nuevo item.
     *
     * @param name nombre del item.
     * @return un nuevo item.
     */
    public Item generate(String name) {
        Item item = null;
        switch (name) {
            case Axe.item_name -> item = new Axe(game, world);
            case Boots.item_name -> item = new Boots(game, world);
            case Chest.item_name -> item = new Chest(game, world);
            case Gold.item_name -> item = new Gold(game, world);
            case Door.item_name -> item = new Door(game, world);
            case DoorIron.item_name -> item = new DoorIron(game, world);
            case Key.item_name -> item = new Key(game, world, 0);
            case Lantern.item_name -> item = new Lantern(game, world);
            case Pickaxe.item_name -> item = new Pickaxe(game, world);
            // En el caso de comerciar con el Trader y comprar de a 1 seria ineficiente ya que se crean 1 objeto por pocion, por lo tanto seria bueno que el player pueda determinar la cantidad a comprar
            case PotionRed.item_name ->
                    item = new PotionRed(game, world, 0); // Es importante pasarle 0 cuando se genera para solo crear el objeto y no esepecificar una cantidad
            case ShieldBlue.item_name -> item = new ShieldBlue(game, world);
            case ShieldWood.item_name -> item = new ShieldWood(game, world);
            case Stone.item_name -> item = new Stone(game, world, 0);
            case SwordNormal.item_name -> item = new SwordNormal(game, world);
            case Tent.item_name -> item = new Tent(game, world);
        }
        return item;
    }
}
