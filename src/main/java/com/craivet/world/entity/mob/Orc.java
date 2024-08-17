package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.assets.*;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

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
        hitbox.x = 5;
        hitbox.y = 32;
        hitbox.width = 20;
        hitbox.height = 25;
        hitbox = new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 44;
        attackbox.height = 48;

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
            game.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
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
    public void render(Graphics2D g2) {
        if (isOnCamera()) {
            screen.tempScreenX = getScreenX();
            screen.tempScreenY = getScreenY();

            if (type == Type.HOSTILE && flags.hpBar && !flags.boss) game.ui.renderHpBar(this);

            if (flags.invincible) {
                // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY(), null);

            if (game.keyboard.hitbox) drawRects(g2);

            Utils.changeAlpha(g2, 1f);
        }
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        drop(this, new Key(game, world, 1));
    }

    private BufferedImage getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = flags.colliding ? down.getFirstFrame() : down.getCurrentFrame();
            case UP -> currentFrame = flags.colliding ? up.getFirstFrame() : up.getCurrentFrame();
            case LEFT -> currentFrame = flags.colliding ? left.getFirstFrame() : left.getCurrentFrame();
            case RIGHT -> currentFrame = flags.colliding ? right.getFirstFrame() : right.getCurrentFrame();
        }
        return currentFrame;
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(getScreenX() + hitbox.x, getScreenY() + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            g2.drawRect(getScreenX() + attackbox.x + hitbox.x, getScreenY() + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

}