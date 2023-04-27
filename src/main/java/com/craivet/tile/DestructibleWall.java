package com.craivet.tile;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.entity.item.Coin;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int x, int y) {
        super(game, world);
        // TODO Se podria calcular la edad desde la clase Entity
        this.x = tile_size * x;
        this.y = tile_size * y;
        mobImage = Utils.scaleImage(itile_destructiblewall, tile_size, tile_size);
        destructible = true;
        life = 3;
    }

    public boolean isCorrectWeapon(Entity weapon) {
        return weapon.type == TYPE_PICKAXE;
    }

    public Interactive getDestroyedForm() {
        return null;
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
        if (Utils.azar(100) <= PROBABILIDAD_DROP_ORO) dropItem(new Coin(game, world));
    }

    @Override
    public void playSound() {
        game.playSound(sound_mine);
    }
}
