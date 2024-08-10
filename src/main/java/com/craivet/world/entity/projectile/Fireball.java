package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.assets.SpriteSheetAssets;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;

import java.awt.*;

import static com.craivet.utils.Global.*;

public class Fireball extends Projectile {

    public Fireball(Game game, World world) {
        super(game, world);
        stats.name = "Fireball";
        stats.speed = 5;
        stats.hp = stats.maxHp = 80;
        stats.attack = 1;
        stats.knockbackValue = 5;
        flags.alive = false;
        cost = 2;
        sound = Assets.getAudio(AudioAssets.Type.FIREBALL);
        hitbox = new Rectangle(0, 0, tile, tile);
        int scale = 2;
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.Type.FIREBALL), 16, 16, scale);
        interval = 80;
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.stats.mana >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.stats.mana -= cost;
    }

    @Override
    public Color getParticleColor() {
        return new Color(240, 50, 0);
    }

    @Override
    public int getParticleSize() {
        return 10;
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }

}
