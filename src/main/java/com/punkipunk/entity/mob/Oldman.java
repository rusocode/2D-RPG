package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.gfx.Animation;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

public class Oldman extends Mob {

    public Oldman(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        mobType = MobType.NPC;
        stats.name = "Oldman";
        stats.speed = 1;
        int scale = 1;
        sheet.loadOldmanFrames(Assets.getSpriteSheet(SpriteSheetAssets.OLDMAN), scale);
        hitbox.setWidth(sheet.frame.getWidth() / 2);
        hitbox.setHeight(sheet.frame.getHeight() / 2);
        hitbox.setX(hitbox.getWidth() - hitbox.getWidth() / 2 + 1);
        hitbox.setY(hitbox.getHeight() - 4);
        hitbox = new Rectangle(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        dialogue.set = -1;
        initDialogue();

        int animationSpeed = 120;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);

    }

    @Override
    public void doActions() {
        if (flags.following)
            game.system.aStar.searchPath(this, getGoalRow(world.entities.player), getGoalCol(world.entities.player));
        else {
            // checkFollow(world.entities.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }
        // If it is not colliding, then update the frames
        if (!flags.colliding) {
            down.tick();
            up.tick();
            left.tick();
            right.tick();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY());
        if (game.system.keyboard.isKeyToggled(Key.RECTS)) drawRects(gc);
    }

    @Override
    public void dialogue() {
        lookPlayer();
        dialogue.startDialogue(State.DIALOGUE, this, dialogue.set);
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

    // TODO Mover a SpriteSheet y no sobreescribir el metodo render
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
