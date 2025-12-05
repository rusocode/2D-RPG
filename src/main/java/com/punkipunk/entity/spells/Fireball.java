package com.punkipunk.entity.spells;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.world.World;
import com.punkipunk.gfx.opengl.Color;

public class Fireball extends Spell {

    public Fireball(IGame game, World world) {
        super(game, world, JsonLoader.getInstance().deserialize("spells.fireball", SpellData.class));
        sound = AudioID.Sound.FIREBALL;
        sheet.loadMovementFrames(spellData.spriteSheetPath(), spellData.frameWidth(), spellData.frameHeight(), spellData.frameScale());
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
