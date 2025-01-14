package com.punkipunk.entity.mob;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.spells.StickyBall;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;
import static com.punkipunk.utils.Global.INTERVAL_PROJECTILE;

public class Slime extends Mob {

    public Slime(Game game, World world, int... pos) {
        super(game, world, pos);
        spell = new StickyBall(game, world);
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 15);
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
            checkShoot();
        } else {
            checkFollow(world.entities.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        drop(new Gold(game, world, mobData.gold()));
    }

    private void checkShoot() {
        if (Utils.random(100) == 1 && !spell.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            spell.set(position.x + 8, position.y + 17, direction, true, this);
            world.entities.getSpells(world.map.num).add(spell);
            timer.projectileCounter = 0;
        }
    }

    @Override
    protected MobType getType() {
        return MobType.SLIME;
    }

}
