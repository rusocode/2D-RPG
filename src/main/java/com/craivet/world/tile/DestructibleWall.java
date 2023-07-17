package com.craivet.world.tile;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.item.Stone;
import com.craivet.world.entity.mob.Type;
import com.craivet.util.Utils;

import java.awt.*;

import static com.craivet.util.Global.*;
import static com.craivet.gfx.Assets.*;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int x, int y) {
        super(game, world, x, y);
        image = Utils.scaleImage(itile_destructiblewall, tile_size, tile_size);
        destructible = true;
        hp = 3;
    }

    public boolean isCorrectWeapon(Item weapon) {
        return weapon.type == Type.PICKAXE;
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
        if (Utils.azar(100) <= PROBABILITY_STONE_DROP) dropItem(this, new Stone(game, world, 1));
    }

    @Override
    public void playSound() {
        game.playSound(sound_mine);
    }
}
