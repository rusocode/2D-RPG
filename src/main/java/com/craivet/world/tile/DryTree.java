package com.craivet.world.tile;

import java.awt.*;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.util.Utils;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class DryTree extends Interactive {

    public DryTree(Game game, World world, int x, int y) {
        super(game, world, x, y);
        image = Utils.scaleImage(itile_drytree, tile_size, tile_size);
        destructible = true;
        hp = 1;
    }

    public boolean isCorrectWeapon(Entity weapon) {
        return weapon.type == Type.AXE;
    }

    public Color getParticleColor() {
        return new Color(121, 90, 47);
    }

    public int getParticleSize() {
        return 6;
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleMaxLife() {
        return 20;
    }

    @Override
    public void playSound() {
        game.playSound(sound_cut_tree);
    }
}