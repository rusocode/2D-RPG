package com.punkipunk.entity.mob;

import com.punkipunk.audio.AudioID;
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
        soundHit = AudioID.Sound.SLIME_HIT;
        spell = new StickyBall(game, world);
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entitySystem.player, 15);
            game.gameSystem.aStar.searchPath(this, getGoalRow(world.entitySystem.player), getGoalCol(world.entitySystem.player));
            checkShoot();
        } else {
            checkFollow(world.entitySystem.player, 5, 100);
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
            world.entitySystem.getSpells(world.map.num).add(spell);
            timer.projectileCounter = 0;
        }
    }

    @Override
    public MobType getType() {
        return MobType.SLIME;
    }

}
