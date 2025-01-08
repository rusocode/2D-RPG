package com.punkipunk.entity.mob;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.spells.StickyBall;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public class Slime extends Mob {

    public Slime(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("mobs.slime", MobData.class), pos);
        mobType = MobType.HOSTILE;
        spell = new StickyBall(game, world);
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 15);
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
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
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world, 25));
    }

    private void checkShoot() {
        if (Utils.random(100) == 1 && !spell.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            spell.set(pos.x + 8, pos.y + 17, direction, true, this);
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
