package com.punkipunk.entity.spells;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * TODO Implement the decrease in the player's speed when the sticky ball hits him.
 */

public class StickyBall extends Spell {

    public StickyBall(Game game, World world) {
        super(game, world, JsonLoader.getInstance().deserialize("spells.stickyBall", SpellData.class));
        hitbox = new Rectangle(8, 8, 15, 15);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.frame = Utils.scaleTexture(Utils.loadTexture(spellData.texturePath()), tile, tile);
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
