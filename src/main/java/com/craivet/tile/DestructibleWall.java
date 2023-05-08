package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.entity.item.Stone;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int x, int y) {
        super(game, world, x, y);
        image = Utils.scaleImage(itile_destructiblewall, tile_size, tile_size);
        destructible = true;
        HP = 3;
    }

    public boolean isCorrectWeapon(Entity weapon) {
        return weapon.type == TYPE_PICKAXE;
    }

    public Color getParticleColor() {
        return new Color(131, 130, 130);
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

    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILIDAD_DROP_STONE) dropItem(this, new Stone(game, world));
    }

    @Override
    public void playSound() {
        game.playSound(sound_mine);
    }
}
