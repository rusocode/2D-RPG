package com.punkipunk.entity.mob;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Type;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.projectile.Fireball;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public class RedSlime extends Mob {

    public RedSlime(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Red Slime";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 6;
        stats.exp = 15;
        stats.attack = 3;
        stats.defense = 2;
        soundHit = AudioID.Sound.SLIME_HIT;
        hitbox = new Rectangle(3, 6, tile - 7, tile - 11);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        projectile = new Fireball(game, world);
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.RED_SLIME), 16, 16, 2);
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
        if (Utils.random(100) == 1 && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE) {
            projectile.set(pos.x, pos.y, direction, true, this);
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
