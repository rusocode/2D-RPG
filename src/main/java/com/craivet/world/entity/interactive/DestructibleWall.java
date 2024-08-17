package com.craivet.world.entity.interactive;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.item.Stone;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int x, int y) {
        super(game, world, x, y);
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.ITILE_DESTRUCTIBLE_WALL), tile, tile);
        destructible = true;
        stats.hp = 3;
        hitbox = new Rectangle(0, 0, tile, tile);
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
        if (Utils.random(100) <= PROBABILITY_STONE_DROP) drop(this, new Stone(game, world, 1));
    }

    @Override
    public void playSound() {
        game.playSound(Assets.getAudio(AudioAssets.MINE));
    }
}
