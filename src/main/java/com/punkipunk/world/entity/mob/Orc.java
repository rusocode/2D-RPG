package com.punkipunk.world.entity.mob;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.gfx.Animation;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_DEAD_ANIMATION;
import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

public class Orc extends Mob {

    public Orc(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Orc";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 10;
        stats.exp = 30;
        stats.attack = 8;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 30;
        soundHit = Assets.getAudio(AudioAssets.ORC_HIT);
        soundDeath = Assets.getAudio(AudioAssets.ORC_DEATH);
        int scale = 1;
        sheet.loadOrcMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.ORC), scale);
        hitbox.setX(5);
        hitbox.setY(32);
        hitbox.setWidth(20);
        hitbox.setHeight(25);
        hitbox = new Rectangle(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        attackbox.setWidth(44);
        attackbox.setHeight(48);

        int animationSpeed = 150;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
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
        // if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 2, tile * 2, 30);
    }

    @Override
    public void render(GraphicsContext g2) {
        if (isOnCamera()) {
            tempScreenX = getScreenX();
            tempScreenY = getScreenY();

            if (type == Type.HOSTILE && flags.hpBar && !flags.boss) game.system.ui.renderHpBar(this);

            if (flags.invincible) {
                // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY());

            if (game.system.keyboard.isKeyToggled(Key.RECTS)) drawRects(g2);

            Utils.changeAlpha(g2, 1f);
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        drop(this, new com.punkipunk.world.entity.item.Key(game, world, 1));
    }

    private Image getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = flags.colliding ? down.getFirstFrame() : down.getCurrentFrame();
            case UP -> currentFrame = flags.colliding ? up.getFirstFrame() : up.getCurrentFrame();
            case LEFT -> currentFrame = flags.colliding ? left.getFirstFrame() : left.getCurrentFrame();
            case RIGHT -> currentFrame = flags.colliding ? right.getFirstFrame() : right.getCurrentFrame();
        }
        return currentFrame;
    }

    private void drawRects(GraphicsContext gc) {
        // Frame
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(1);
        gc.strokeRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());

        // Hitbox
        gc.setStroke(Color.GREEN);
        gc.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

        // Attackbox
        if (flags.hitting) {
            gc.setStroke(Color.RED);
            gc.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}