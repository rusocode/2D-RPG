package com.punkipunk.entity.spells;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.world.World;
import com.punkipunk.gfx.opengl.Color;
import com.punkipunk.physics.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * FIXME Este objeto forma parte de un proyectil y no de un hechizo
 * TODO Implementar disminucion en la velocidad del jugador cuando este colisiona con StickyBall
 */

public class StickyBall extends Spell {

    public StickyBall(IGame game, World world) {
        super(game, world, JsonLoader.getInstance().deserialize("spells.stickyBall", SpellData.class));
        hitbox = new Rectangle(8, 8, 15, 15);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.loadStaticFrame(spellData.texturePath(), tile, tile);
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
