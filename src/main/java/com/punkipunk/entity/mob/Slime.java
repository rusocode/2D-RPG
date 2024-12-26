package com.punkipunk.entity.mob;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Type;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.projectile.StickyBall;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public class Slime extends Mob {

    public Slime(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Slime";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 4;
        stats.exp = 10;
        stats.attack = 2;
        stats.defense = 1;
        soundHit = AudioID.Sound.SLIME_HIT;
        hitbox = new Rectangle(3, 6, tile - 7, tile - 11);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        projectile = new StickyBall(game, world);
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.SLIME), 16, 16, 2);
    }

    @Override
    public void doActions() {
        if (flags.following) {
            checkUnfollow(world.entities.player, 15);
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
            // checkShoot();
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
        if (Utils.random(100) == 1 && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            projectile.set(pos.x + 8, pos.y + 17, direction, true, this);
            for (int i = 0; i < world.entities.projectiles[1].length; i++) {
                if (world.entities.projectiles[world.map.num][i] == null) {
                    world.entities.projectiles[world.map.num][i] = projectile;
                    break;
                }
            }
            timer.projectileCounter = 0;
        }
    }

}
