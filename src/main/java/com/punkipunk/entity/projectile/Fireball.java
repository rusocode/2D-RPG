package com.punkipunk.entity.projectile;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.world.World;
import com.punkipunk.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

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
        sound = AudioID.Sound.FIREBALL;
        hitbox = new Rectangle(0, 0, tile, tile);
        int scale = 2;
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.FIREBALL), 16, 16, scale);
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
        return Color.rgb(240, 50, 0);
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
