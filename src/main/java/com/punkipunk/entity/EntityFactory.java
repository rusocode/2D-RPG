package com.punkipunk.entity;

import com.punkipunk.core.Game;
import com.punkipunk.entity.interactive.InteractiveID;
import com.punkipunk.entity.item.*;
import com.punkipunk.entity.mob.MobID;
import com.punkipunk.io.Progress;
import com.punkipunk.world.MapID;
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
        createEntities();
    }

    public void createEntities() {
        createItems();
        createMobs();
        createInteractives();
    }

    public void createItems() {

        // Items en Abandoned Island
        entitySystem.createItem(ItemID.CHEST, MapID.ABANDONED_ISLAND, 21, 22).setLoot(new StoneSword(game, world));
        entitySystem.createItem(ItemID.CHEST, MapID.ABANDONED_ISLAND, 30, 29).setLoot(new Key(game, world));
        entitySystem.createItem(ItemID.STONE_AXE, MapID.ABANDONED_ISLAND, 33, 7);
        entitySystem.createItem(ItemID.BOOTS, MapID.ABANDONED_ISLAND, 23, 25);
        entitySystem.createItem(ItemID.CHICKEN, MapID.ABANDONED_ISLAND, 23, 26);
        entitySystem.createItemWithAmount(ItemID.POTION_RED, MapID.ABANDONED_ISLAND, 5, 23, 27);
        entitySystem.createItem(ItemID.WOOD_DOOR, MapID.ABANDONED_ISLAND, 14, 28);
        entitySystem.createItem(ItemID.WOOD_DOOR, MapID.ABANDONED_ISLAND, 12, 12);

        // Items en Dungeon Breg
        entitySystem.createItem(ItemID.IRON_DOOR, MapID.DUNGEON_BREG, 18, 23);
        entitySystem.createItem(ItemID.CHEST, MapID.DUNGEON_BREG, 13, 16).setLoot(new PotionRed(game, world, 5));

        // Items en Dungeon Breg Sub
        entitySystem.createItem(ItemID.IRON_DOOR, MapID.DUNGEON_BREG_SUB, 25, 15);
        entitySystem.createItem(ItemID.CHEST, MapID.DUNGEON_BREG_SUB, 25, 8).setLoot(new Chicken(game, world));

    }

    public void createMobs() {

        // Mobs en Abandoned Island
        entitySystem.createMob(MobID.OLDMAN, MapID.ABANDONED_ISLAND, 23, 16);
        entitySystem.createMob(MobID.BOX, MapID.ABANDONED_ISLAND, 26, 19);
        entitySystem.createMob(MobID.BAT, MapID.ABANDONED_ISLAND, 18, 20);
        entitySystem.createMob(MobID.BAT, MapID.ABANDONED_ISLAND, 20, 20);
        entitySystem.createMob(MobID.SLIME, MapID.ABANDONED_ISLAND, 24, 38);
        entitySystem.createMob(MobID.SLIME, MapID.ABANDONED_ISLAND, 34, 42);
        entitySystem.createMob(MobID.SLIME, MapID.ABANDONED_ISLAND, 38, 42);
        entitySystem.createMob(MobID.ORC, MapID.ABANDONED_ISLAND, 12, 33);

        // Mobs en Abandoned Island Market
        entitySystem.createMob(MobID.TRADER, MapID.ABANDONED_ISLAND_MARKET, 12, 7);

        // Mobs en Dungeon Breg
        entitySystem.createMob(MobID.BOX, MapID.DUNGEON_BREG, 20, 25);
        entitySystem.createMob(MobID.BOX, MapID.DUNGEON_BREG, 11, 19);
        entitySystem.createMob(MobID.BOX, MapID.DUNGEON_BREG, 23, 14);
        entitySystem.createMob(MobID.RED_SLIME, MapID.DUNGEON_BREG, 34, 39);
        entitySystem.createMob(MobID.RED_SLIME, MapID.DUNGEON_BREG, 36, 25);
        entitySystem.createMob(MobID.BAT, MapID.DUNGEON_BREG, 28, 11);

        // Mobs en Dungeon Breg Sub
        if (!Progress.bossDefeated) entitySystem.createMob(MobID.LIZARD, MapID.DUNGEON_BREG_SUB, 23, 16);

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
            entitySystem.createInteractive(InteractiveID.DRY_TREE, MapID.ABANDONED_ISLAND, pos[0], pos[1]);

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
            entitySystem.createInteractive(InteractiveID.DESTRUCTIBLE_WALL, MapID.DUNGEON_BREG, pos[0], pos[1]);

    }

    private void createMetalPlates() {
        // Metal Plate en Abandoned Island
        entitySystem.createInteractive(InteractiveID.METAL_PLATE, MapID.ABANDONED_ISLAND, 26, 18);

        // Metal Plate en Dungeon Breg
        entitySystem.createInteractive(InteractiveID.METAL_PLATE, MapID.DUNGEON_BREG, 20, 22);
        entitySystem.createInteractive(InteractiveID.METAL_PLATE, MapID.DUNGEON_BREG, 8, 17);
        entitySystem.createInteractive(InteractiveID.METAL_PLATE, MapID.DUNGEON_BREG, 39, 31);
    }

}
