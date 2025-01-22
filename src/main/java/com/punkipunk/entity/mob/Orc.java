package com.punkipunk.entity.mob;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Key;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

public class Orc extends Mob {

    public Orc(Game game, World world, int... pos) {
        super(game, world, pos);
        soundHit = AudioID.Sound.ORC_HIT;
        soundDeath = AudioID.Sound.ORC_DEATH;
        sheet.loadOrcMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameScale());
        down = new Animation(mobData.animationSpeed(), sheet.down);
        up = new Animation(mobData.animationSpeed(), sheet.up);
        left = new Animation(mobData.animationSpeed(), sheet.left);
        right = new Animation(mobData.animationSpeed(), sheet.right);
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entitySystem.player, 10);
            game.gameSystem.aStar.searchPath(this, getGoalRow(world.entitySystem.player), getGoalCol(world.entitySystem.player));
        } else {
            checkFollow(world.entitySystem.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
        if (!flags.colliding) {
            down.tick();
            up.tick();
            left.tick();
            right.tick();
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        drop(new Key(game, world));
    }

    @Override
    public MobType getType() {
        return MobType.ORC;
    }

}