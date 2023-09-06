package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;

/**
 * Crea nuevos items al comprar o recoger. Esto evita utilizar la misma referencia cuando se compra un item.
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
            case Axe.NAME -> item = new Axe(game, world);
            case Boots.NAME -> item = new Boots(game, world);
            case Chest.NAME -> item = new Chest(game, world);
            case Gold.NAME -> item = new Gold(game, world);
            case Door.NAME -> item = new Door(game, world);
            case DoorIron.NAME -> item = new DoorIron(game, world);
            case Key.NAME -> item = new Key(game, world, 0);
            case Lantern.NAME -> item = new Lantern(game, world);
            case Pickaxe.NAME -> item = new Pickaxe(game, world);
            /* En el caso de comerciar con el Trader y comprar de a 1 seria ineficiente ya que se crea 1 objeto por
             * pocion, por lo tanto seria bueno que el player pueda determinar la cantidad a comprar. Es importante
             * pasarle 0 cuando se genera para solo crear el objeto y no esepecificar una cantidad. */
            case PotionRed.NAME -> item = new PotionRed(game, world, 0);
            case ShieldBlue.NAME -> item = new ShieldBlue(game, world);
            case ShieldWood.NAME -> item = new ShieldWood(game, world);
            case Stone.NAME -> item = new Stone(game, world, 0);
            case SwordIron.NAME -> item = new SwordIron(game, world);
            case Tent.NAME -> item = new Tent(game, world);
        }
        return item;
    }
}
