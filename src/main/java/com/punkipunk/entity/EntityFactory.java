package com.punkipunk.entity;

import com.punkipunk.core.Game;
import com.punkipunk.entity.interactive.InteractiveType;
import com.punkipunk.entity.item.*;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.io.Progress;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.*;

public class EntityFactory {

    private final Game game;
    private final World world;
    private final EntitySystem entitySystem;

    public EntityFactory(Game game, World world, EntitySystem entitySystem) {
        this.game = game;
        this.world = world;
        this.entitySystem = entitySystem;
    }

    public void createEntities() {
        createItems();
        createMobs();
        createInteractives();
    }

    public void createItems() {

        // Items en Abandoned Island
        entitySystem.createItem(ItemType.CHEST, ABANDONED_ISLAND, 21, 22).setLoot(new StoneSword(game, world));
        entitySystem.createItem(ItemType.CHEST, ABANDONED_ISLAND, 30, 29).setLoot(new Key(game, world));
        entitySystem.createItem(ItemType.STONE_AXE, ABANDONED_ISLAND, 33, 7);
        entitySystem.createItem(ItemType.BOOTS, ABANDONED_ISLAND, 23, 25);
        entitySystem.createItem(ItemType.CHICKEN, ABANDONED_ISLAND, 23, 26);
        entitySystem.createItemWithAmount(ItemType.POTION_RED, ABANDONED_ISLAND, 5, 23, 27);
        entitySystem.createItem(ItemType.WOOD_DOOR, ABANDONED_ISLAND, 14, 28);
        entitySystem.createItem(ItemType.WOOD_DOOR, ABANDONED_ISLAND, 12, 12);

        // Items en Dungeon Breg
        entitySystem.createItem(ItemType.IRON_DOOR, DUNGEON_BREG, 18, 23);
        entitySystem.createItem(ItemType.CHEST, DUNGEON_BREG, 13, 16).setLoot(new PotionRed(game, world, 5));

        // Items en Dungeon Breg Sub
        entitySystem.createItem(ItemType.IRON_DOOR, DUNGEON_BREG_SUB, 25, 15);
        entitySystem.createItem(ItemType.CHEST, DUNGEON_BREG_SUB, 25, 8).setLoot(new Chicken(game, world));

    }

    public void createMobs() {

        // Mobs en Abandoned Island
        entitySystem.createMob(MobType.OLDMAN, ABANDONED_ISLAND, 23, 16);
        entitySystem.createMob(MobType.BOX, ABANDONED_ISLAND, 26, 19);
        entitySystem.createMob(MobType.BAT, ABANDONED_ISLAND, 18, 20);
        entitySystem.createMob(MobType.BAT, ABANDONED_ISLAND, 20, 20);
        entitySystem.createMob(MobType.SLIME, ABANDONED_ISLAND, 24, 38);
        entitySystem.createMob(MobType.SLIME, ABANDONED_ISLAND, 34, 42);
        entitySystem.createMob(MobType.SLIME, ABANDONED_ISLAND, 38, 42);
        entitySystem.createMob(MobType.ORC, ABANDONED_ISLAND, 12, 33);

        // Mobs en Abandoned Island Market
        entitySystem.createMob(MobType.TRADER, ABANDONED_ISLAND_MARKET, 12, 7);

        // Mobs en Dungeon Breg
        entitySystem.createMob(MobType.BOX, DUNGEON_BREG, 20, 25);
        entitySystem.createMob(MobType.BOX, DUNGEON_BREG, 11, 19);
        entitySystem.createMob(MobType.BOX, DUNGEON_BREG, 23, 14);
        entitySystem.createMob(MobType.RED_SLIME, DUNGEON_BREG, 34, 39);
        entitySystem.createMob(MobType.RED_SLIME, DUNGEON_BREG, 36, 25);
        entitySystem.createMob(MobType.BAT, DUNGEON_BREG, 28, 11);

        // Mobs en Dungeon Breg Sub
        if (!Progress.bossDefeated) entitySystem.createMob(MobType.LIZARD, DUNGEON_BREG_SUB, 23, 16);

    }

    public void createInteractives() {
        createDryTrees();
        createDestructibleWalls();
        createMetalPlates();
    }

    private void createDryTrees() {
        int[][] dryTreesPos = {
                {18, 19}, {19, 19}, {25, 27}, {26, 27},
                {27, 27}, {27, 28}, {27, 29}, {27, 30},
                {27, 31}, {28, 31}, {29, 31}, {30, 31}
        };

        for (int[] pos : dryTreesPos)
            entitySystem.createInteractive(InteractiveType.DRY_TREE, OVERWORLD, pos[0], pos[1]);

    }

    private void createDestructibleWalls() {
        int[][] destructibleWallsPos = {
                {18, 30}, {17, 31}, {17, 32}, {17, 34},
                {18, 34}, {18, 33}, {10, 22}, {10, 24},
                {38, 18}, {38, 19}, {38, 20}, {38, 21},
                {18, 13}, {18, 14}, {22, 28}, {30, 28},
                {32, 28}
        };

        for (int[] pos : destructibleWallsPos)
            entitySystem.createInteractive(InteractiveType.DESTRUCTIBLE_WALL, DUNGEON_BREG, pos[0], pos[1]);

    }

    private void createMetalPlates() {
        // Metal Plate en Abandoned Island
        entitySystem.createInteractive(InteractiveType.METAL_PLATE, ABANDONED_ISLAND, 26, 18);

        // Metal Plate en Dungeon Breg
        entitySystem.createInteractive(InteractiveType.METAL_PLATE, DUNGEON_BREG, 20, 22);
        entitySystem.createInteractive(InteractiveType.METAL_PLATE, DUNGEON_BREG, 8, 17);
        entitySystem.createInteractive(InteractiveType.METAL_PLATE, DUNGEON_BREG, 39, 31);
    }

}
