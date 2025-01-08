package com.punkipunk.entity.mob;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.spells.Fireball;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.*;

public class RedSlime extends Mob {

    public RedSlime(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("mobs.redSlime", MobData.class), pos);
        mobType = MobType.HOSTILE;
        spell = new Fireball(game, world);
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 17);
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
            checkShoot();
        } else {
            checkFollow(world.entities.player, 6, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world, 50));
    }

    private void checkShoot() {
        if (Utils.random(100) == 1 && !spell.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            spell.set(pos.x, pos.y, direction, true, this);
            for (int i = 0; i < world.entities.spells[1].length; i++) {
                if (world.entities.spells[world.map.num][i] == null) {
                    world.entities.spells[world.map.num][i] = spell;
                    break;
                }
            }
            timer.projectileCounter = 0;
        }
    }

}
