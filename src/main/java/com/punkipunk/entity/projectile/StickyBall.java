package com.punkipunk.entity.projectile;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * TODO Implement the decrease in the player's speed when the sticky ball hits him.
 */

public class StickyBall extends Projectile {

    public StickyBall(Game game, World world) {
        super(game, world);
        stats.name = "Sticky Ball";
        stats.speed = 5;
        stats.hp = stats.maxHp = 120;
        stats.attack = 3;
        flags.alive = false;
        cost = 1;
        hitbox = new Rectangle(8, 8, 15, 15);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.STICKY_BALL), tile, tile);
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.stats.ammo >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.stats.ammo -= cost;
    }

    @Override
    public Color getParticleColor() {
        return Color.rgb(106, 193, 127);
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
