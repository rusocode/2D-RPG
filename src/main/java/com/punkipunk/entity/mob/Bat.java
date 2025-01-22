package com.punkipunk.entity.mob;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.item.StoneAxe;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

public class Bat extends Mob {

    public Bat(Game game, World world, int... pos) {
        super(game, world, pos);
        soundHit = AudioID.Sound.BAT_HIT;
        soundDeath = AudioID.Sound.BAT_DEATH;
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        timer.timeDirection(this, Utils.random(100, 200));
    }

    @Override
    public void checkDrop() {
        drop(new Gold(game, world, mobData.gold()));
        if (Utils.random(100) <= mobData.probabilityItemDrop()) drop(new StoneAxe(game, world));
    }

    @Override
    public MobType getType() {
        return MobType.BAT;
    }

}