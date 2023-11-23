package com.craivet.world;

import com.craivet.Game;
import com.craivet.io.Progress;
import com.craivet.world.entity.item.*;
import com.craivet.world.entity.mob.*;
import com.craivet.world.tile.DestructibleWall;
import com.craivet.world.tile.DryTree;
import com.craivet.world.tile.Interactive;
import com.craivet.world.tile.MetalPlate;

import static com.craivet.utils.Global.*;

public class Setter {
    
    private final Game game;
    private final World world;
    private final Item[][] items;
    private final Mob[][] mobs;
    private final Interactive[][] interactives;

    public Setter(Game game, World world, Item[][] items, Mob[][] mobs, Interactive[][] interactives) {
        this.game = game;
        this.world = world;
        this.items = items;
        this.mobs = mobs;
        this.interactives = interactives;
        createEntities();
    }
    
    /**
     * Create the entities.
     */
    private void createEntities() {
        createItems();
        createMobs();
        createInteractiveTile();
    }

    /**
     * Create the items.
     */
    public void createItems() {
        int i = 0; // TODO No se tiene que reiniciar la i despues de cada mapa?
        items[ABANDONED_ISLAND][i++] = new Axe(game, world, 33, 7);
        items[ABANDONED_ISLAND][i++] = new Door(game, world, 14, 28);
        items[ABANDONED_ISLAND][i++] = new Door(game, world, 12, 12);
        // TODO No es mejor pasarle directamente al cofre el item que va a tener desde el constructor?
        items[ABANDONED_ISLAND][i] = new Chest(game, world, 30, 29);
        // TODO What if there are many items?
        items[ABANDONED_ISLAND][i++].setLoot(new Key(game, world, 1));
        items[ABANDONED_ISLAND][i] = new Chest(game, world, 23, 40);
        items[ABANDONED_ISLAND][i++].setLoot(new PotionRed(game, world, 30));

        items[DUNGEON_BREG][i] = new Chest(game, world, 13, 16);
        items[DUNGEON_BREG][i++].setLoot(new PotionRed(game, world, 20));
        items[DUNGEON_BREG][i] = new Chest(game, world, 26, 34);
        items[DUNGEON_BREG][i++].setLoot(new PotionRed(game, world, 5));
        items[DUNGEON_BREG][i] = new Chest(game, world, 40, 41);
        items[DUNGEON_BREG][i++].setLoot(new Pickaxe(game, world));
        items[DUNGEON_BREG][i++] = new DoorIron(game, world, 18, 23);

        items[DUNGEON_BREG_SUB][i++] = new DoorIron(game, world, 25, 15);
        items[DUNGEON_BREG_SUB][i] = new Chest(game, world, 25, 8);
        items[DUNGEON_BREG_SUB][i].setLoot(new Chicken(game, world));

    }

    /**
     * Create the mobs.
     */
    public void createMobs() {
        int i = 0, j = 0, k = 0, z = 0;

        mobs[ABANDONED_ISLAND][i++] = new Oldman(game, world, 23, 16); // TODO set pos?
        mobs[ABANDONED_ISLAND][i++] = new Bat(game, world, 26, 19);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, world, 24, 37);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, world, 34, 42);
        mobs[ABANDONED_ISLAND][i++] = new Slime(game, world, 38, 42);
        mobs[ABANDONED_ISLAND][i++] = new Orc(game, world, 12, 33);

        mobs[ABANDONED_ISLAND_MARKET][j++] = new Trader(game, world, 12, 7);

        mobs[DUNGEON_BREG][k++] = new Box(game, world, 20, 25);
        mobs[DUNGEON_BREG][k++] = new Box(game, world, 11, 18);
        mobs[DUNGEON_BREG][k++] = new Box(game, world, 23, 14);
        mobs[DUNGEON_BREG][k++] = new Bat(game, world, 34, 39);
        mobs[DUNGEON_BREG][k++] = new Bat(game, world, 36, 25);
        mobs[DUNGEON_BREG][k++] = new Bat(game, world, 39, 26);
        mobs[DUNGEON_BREG][k++] = new Bat(game, world, 28, 11);
        mobs[DUNGEON_BREG][k++] = new Bat(game, world, 10, 19);

        if (!Progress.bossDefeated) mobs[DUNGEON_BREG_SUB][z++] = new Skeleton(game, world, 23, 16);

    }

    /**
     * Create the interactive tiles.
     */
    public void createInteractiveTile() {
        int i = 0;

        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 25, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 26, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 27, 27);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 27, 28);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 27, 29);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 27, 30);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 27, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 28, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 29, 31);
        interactives[ABANDONED_ISLAND][i++] = new DryTree(game, world, 30, 31);

        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 18, 30);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 17, 31);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 17, 32);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 17, 34);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 18, 34);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 18, 33);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 10, 22);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 10, 24);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 38, 18);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 38, 19);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 38, 20);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 38, 21);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 18, 13);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 18, 14);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 22, 28);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 30, 28);
        interactives[DUNGEON_BREG][i++] = new DestructibleWall(game, world, 32, 28);

        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, world, 20, 22);
        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, world, 8, 17);
        interactives[DUNGEON_BREG][i++] = new MetalPlate(game, world, 39, 31);

    }
    
}
