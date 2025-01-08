package com.punkipunk.entity.mob;

import com.punkipunk.core.Game;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

public class Orc extends Mob {

    public Orc(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("mobs.orc", MobData.class), pos);
        mobType = MobType.HOSTILE;
        sheet.loadOrcMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameScale());
        down = new Animation(mobData.animationSpeed(), sheet.down);
        up = new Animation(mobData.animationSpeed(), sheet.up);
        left = new Animation(mobData.animationSpeed(), sheet.left);
        right = new Animation(mobData.animationSpeed(), sheet.right);
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 10);
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
        } else {
            checkFollow(world.entities.player, 5, 100);
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
        drop(this, new com.punkipunk.entity.item.Key(game, world));
    }

}