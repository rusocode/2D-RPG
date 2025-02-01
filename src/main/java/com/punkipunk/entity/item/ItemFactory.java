package com.punkipunk.entity.item;

import com.punkipunk.core.Game;
import com.punkipunk.world.World;

public class ItemFactory {

    private final Game game;
    private final World world;

    public ItemFactory(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public Item create(ItemID id, int... pos) {
        return switch (id) {
            case BOOTS -> new Boots(game, world, pos);
            case CHEST -> new Chest(game, world, pos);
            case CHICKEN -> new Chicken(game, world, pos);
            case IRON_DOOR -> new IronDoor(game, world, pos);
            case IRON_SHIELD -> new IronShield(game, world, pos);
            case KEY -> new Key(game, world, pos);
            case LANTERN -> new Lantern(game, world, pos);
            case STONE_AXE -> new StoneAxe(game, world, pos);
            case STONE_PICKAXE -> new StonePickaxe(game, world, pos);
            case STONE_SWORD -> new StoneSword(game, world, pos);
            case TENT -> new Tent(game, world, pos);
            case WOOD_DOOR -> new WoodDoor(game, world, pos);
            case WOOD_SHIELD -> new WoodShield(game, world, pos);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public Item createWithAmount(ItemID id, int amount, int... pos) {
        return switch (id) {
            case GOLD -> new Gold(game, world, amount, pos);
            case POTION_RED -> new PotionRed(game, world, amount, pos);
            case POTION_BLUE -> new PotionBlue(game, world, amount, pos);
            case STONE -> new Stone(game, world, amount, pos);
            case WOOD -> new Wood(game, world, amount, pos);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

}
