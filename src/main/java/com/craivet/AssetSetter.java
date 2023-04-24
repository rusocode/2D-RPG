package com.craivet;

import com.craivet.entity.item.*;
import com.craivet.entity.mob.Orc;
import com.craivet.entity.mob.Slime;
import com.craivet.entity.npc.Trader;
import com.craivet.entity.npc.Oldman;
import com.craivet.tile.DryTree;

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
        // world.items[NIX][i++] = new Tent(game, world, 12, 31);
        // world.items[NIX][i++] = new Lantern(game, world, 13, 31);
        world.items[NIX][i++] = new Axe(game, world, 33, 7);
        world.items[NIX][i++] = new Axe(game, world, 20, 21);
        world.items[NIX][i++] = new Door(game, world, 14, 28);
        world.items[NIX][i++] = new Door(game, world, 12, 12);

        world.items[NIX][i] = new Chest(game, world, 30, 29);
        world.items[NIX][i++].setLoot(new Key(game, world, 1));

        world.items[NIX][i] = new Chest(game, world, 17, 21);
        world.items[NIX][i++].setLoot(new Tent(game, world));

        world.items[NIX][i] = new Chest(game, world, 16, 21);
        world.items[NIX][i].setLoot(new PotionRed(game, world, 5));

    }

    public void setNPC() {
        int i = 0;
        world.npcs[NIX][i++] = new Oldman(game, world, 23, 18);
        i = 0;
        world.npcs[NIX_INDOOR_01][i++] = new Trader(game, world, 12, 7);

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
    }

}
