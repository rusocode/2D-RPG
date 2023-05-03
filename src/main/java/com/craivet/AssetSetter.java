package com.craivet;

import com.craivet.entity.item.*;
import com.craivet.entity.mob.Orc;
import com.craivet.entity.mob.Slime;
import com.craivet.entity.npc.BigRock;
import com.craivet.entity.npc.Trader;
import com.craivet.entity.npc.Oldman;
import com.craivet.tile.DestructibleWall;
import com.craivet.tile.DryTree;
import com.craivet.tile.MetalPlate;

import static com.craivet.utils.Global.*;

/**
 * Establece las entidades en el mundo.
 */

public class AssetSetter {

    private final Game game;
    private final World world;

    public AssetSetter(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void setItem() {
        int i = 0;
        world.items[NIX][i++] = new Axe(game, world, 33, 7);
        world.items[NIX][i++] = new PotionRed(game, world, 20, 21, 5);
        world.items[NIX][i++] = new Door(game, world, 14, 28);
        world.items[NIX][i++] = new Door(game, world, 12, 12);

        world.items[NIX][i] = new Chest(game, world, 30, 29);
        world.items[NIX][i++].setLoot(new Key(game, world, 1));

        /* world.items[NIX][i] = new Chest(game, world, 17, 21);
        world.items[NIX][i++].setLoot(new Tent(game, world)); */

        world.items[NIX][i] = new Chest(game, world, 16, 21);
        world.items[NIX][i++].setLoot(new PotionRed(game, world, 5));

        world.items[NIX][i] = new Chest(game, world, 23, 40);
        world.items[NIX][i++].setLoot(new PotionRed(game, world, 30));

        world.items[DUNGEON_01][i] = new Chest(game, world, 13, 16);
        world.items[DUNGEON_01][i++].setLoot(new PotionRed(game, world, 20));

        world.items[DUNGEON_01][i] = new Chest(game, world, 26, 34);
        world.items[DUNGEON_01][i++].setLoot(new PotionRed(game, world, 5));

        world.items[DUNGEON_01][i] = new Chest(game, world, 40, 41);
        world.items[DUNGEON_01][i++].setLoot(new Pickaxe(game, world));

        world.items[DUNGEON_01][i] = new DoorIron(game, world, 18, 23);

    }

    public void setNPC() {
        int i = 0, j = 0, k = 0;
        
        world.npcs[NIX][i++] = new Oldman(game, world, 23, 18);
        world.npcs[NIX][i] = new Oldman(game, world, 23, 20);

        world.npcs[NIX_INDOOR_01][j] = new Trader(game, world, 12, 7);

        world.npcs[DUNGEON_01][k++] = new BigRock(game, world, 20, 25);
        world.npcs[DUNGEON_01][k++] = new BigRock(game, world, 11, 18);
        world.npcs[DUNGEON_01][k] = new BigRock(game, world, 23, 14);
    }

    public void setMOB() {
        int i = 0;
        world.mobs[NIX][i++] = new Slime(game, world, 23, 41);
        world.mobs[NIX][i++] = new Slime(game, world, 24, 37);
        world.mobs[NIX][i++] = new Slime(game, world, 34, 42);
        world.mobs[NIX][i++] = new Slime(game, world, 38, 42);
        world.mobs[NIX][i++] = new Orc(game, world, 12, 33);
    }

    public void setInteractiveTile() {
        int i = 0;
        world.interactives[NIX][i++] = new DryTree(game, world, 25, 27);
        world.interactives[NIX][i++] = new DryTree(game, world, 26, 27);
        world.interactives[NIX][i++] = new DryTree(game, world, 27, 27);
        world.interactives[NIX][i++] = new DryTree(game, world, 27, 28);
        world.interactives[NIX][i++] = new DryTree(game, world, 27, 29);
        world.interactives[NIX][i++] = new DryTree(game, world, 27, 30);
        world.interactives[NIX][i++] = new DryTree(game, world, 27, 31);
        world.interactives[NIX][i++] = new DryTree(game, world, 28, 31);
        world.interactives[NIX][i++] = new DryTree(game, world, 29, 31);
        world.interactives[NIX][i++] = new DryTree(game, world, 30, 31);

        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 18, 30);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 17, 31);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 17, 32);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 17, 34);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 18, 34);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 18, 33);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 10, 22);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 10, 24);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 38, 18);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 38, 19);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 38, 20);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 38, 21);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 18, 13);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 18, 14);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 22, 28);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 30, 28);
        world.interactives[DUNGEON_01][i++] = new DestructibleWall(game, world, 32, 28);

        world.interactives[DUNGEON_01][i++] = new MetalPlate(game, world, 20, 22);
        world.interactives[DUNGEON_01][i++] = new MetalPlate(game, world, 8, 17);
        world.interactives[DUNGEON_01][i++] = new MetalPlate(game, world, 39, 31);

    }

}
