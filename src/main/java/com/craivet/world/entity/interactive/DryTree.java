package com.craivet.world.entity.interactive;

import java.awt.*;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.item.Wood;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class DryTree extends Interactive {

    public DryTree(Game game, World world, int x, int y) {
        super(game, world, x, y);
        sheet.frame = Utils.scaleImage(itile_drytree, tile, tile);
        destructible = true;
        stats.hp = 1;
        hitbox = new Rectangle(0, 0, tile, tile);
    }

    public boolean isCorrectWeapon(Item weapon) {
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

    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_WOOD_DROP) drop(this, new Wood(game, world, 1));
    }

    @Override
    public void playSound() {
        game.playSound(sound_cut_tree);
    }
}
