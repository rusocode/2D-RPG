package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Mob {

    public Oldman(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        type = Type.NPC;
        stats.name = "Oldman";
        stats.speed = 1;
        int scale = 1;
        sheet.loadOldmanFrames(oldman, scale);
        hitbox.width = sheet.frame.getWidth() / 2;
        hitbox.height = sheet.frame.getHeight() / 2;
        hitbox.x = hitbox.width - hitbox.width / 2 + 1;
        hitbox.y = hitbox.height - 4;
        hitbox = new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogue.set = -1;
        initDialogue();

        int animationSpeed = 120;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
        currentFrame = down.getFirstFrame(); // TODO Haca falta?

    }

    @Override
    public void doActions() {
        if (flags.following) game.aStar.searchPath(this, getGoalRow(world.player), getGoalCol(world.player));
        else {
            // checkFollow(world.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
        // Si no esta colisionando, entonces actualiza los frames
        if (!flags.colliding) {
            down.tick();
            up.tick();
            left.tick();
            right.tick();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY(), null);
        if (game.keyboard.hitbox) drawRects(g2);
    }

    @Override
    public void dialogue() {
        lookPlayer();
        dialogue.startDialogue(DIALOGUE_STATE, this, dialogue.set);
        dialogue.set++;
        if (dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "Hello stranger!";
        dialogue.dialogues[0][1] = "To the north there is a small \nlagoon with a beautiful view...";

        dialogue.dialogues[1][0] = "I'm starting to believe that there \nis something stranger in these \nforests.";
        dialogue.dialogues[1][1] = "Everything started happening since \nthat strange guy arrived on the \nisland.";
        dialogue.dialogues[1][2] = "I will continue exploring, until \nthen traveler!";
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
