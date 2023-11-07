package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Orc extends Mob {

    public Orc(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Orc";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 10;
        stats.exp = 2;
        stats.attack = 8;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 30;
        soundHit = sound_hit_orc;
        soundDeath = sound_orc_death;
        int scale = 1;
        sheet.loadOrcMovementFrames(orc_movement, scale);
        sheet.loadAttackFrames(orc_attack, 16, 16, scale);
        hitbox.width = sheet.frame.getWidth() / 2;
        hitbox.height = sheet.frame.getHeight() / 2;
        hitbox.x = hitbox.width - hitbox.width / 2;
        hitbox.y = hitbox.height - 6;
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
        currentFrame = down.getFirstFrame();
    }

    @Override
    public void doActions() {
        if (flags.following) {
            // checkUnfollow(world.player, 10);
            game.aStar.searchPath(this, getGoalRow(world.player), getGoalCol(world.player));
        } else {
            checkFollow(world.player, 5, 100);
            // TODO Bug de ss muy grande al seguir al player, si lo redusco no pasa
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
        g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY(), null);
        drawRects(g2);
    }

    @Override
    public void damageReaction() {
        flags.following = true;
    }

    @Override
    public void checkDrop() {
        int probabilityKeyDrop = 100;
        if (Utils.random(100) <= probabilityKeyDrop) drop(this, new Key(game, world, 1));
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