package com.craivet.tile;

import java.awt.*;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class DryTree extends Interactive {

    public DryTree(Game game, World world, int x, int y) {
        super(game, world);
        this.x = tile_size * x;
        this.y = tile_size * y;
        image = Utils.scaleImage(itile_drytree, tile_size, tile_size);
        destructible = true;
        life = 1;
    }

    public boolean isCorrectWeapon(Entity weapon) {
        return weapon.type == TYPE_AXE;
    }

    public Interactive getDestroyedForm() {
        return new Trunk(game, world, x / tile_size, y / tile_size);
    }

    public Color getParticleColor() {
        return new Color(65, 50, 30);
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
