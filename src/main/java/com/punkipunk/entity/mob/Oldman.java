package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.core.IGame;
import com.punkipunk.gfx.Animation;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import com.punkipunk.physics.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

public class Oldman extends Mob {

    public Oldman(IGame game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        sheet.loadOldmanFrames(mobData.spriteSheetPath(), mobData.frameScale());

        currentFrame = sheet.frame;

        hitbox.setWidth((double) currentFrame.width / 2);
        hitbox.setHeight((double) currentFrame.height / 2);

        hitbox.setX(hitbox.getWidth() - hitbox.getWidth() / 2 + 1);
        hitbox.setY(hitbox.getHeight() - 4);
        hitbox = new Rectangle(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        dialogue.set = -1;
        initDialogue();

        down = new Animation(mobData.animationSpeed(), sheet.down);
        up = new Animation(mobData.animationSpeed(), sheet.up);
        left = new Animation(mobData.animationSpeed(), sheet.left);
        right = new Animation(mobData.animationSpeed(), sheet.right);
    }

    @Override
    public void doActions() {
        if (flags.following)
            game.getGameSystem().pathfinding.searchPath(this, getGoalRow(world.entitySystem.player), getGoalCol(world.entitySystem.player));
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
    public void dialogue() {
        lookPlayer();
        dialogue.startDialogue(State.DIALOGUE, this, dialogue.set);
        dialogue.set++;
        if (dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    @Override
    public MobID getID() {
        return MobID.OLDMAN;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "Hello stranger!";
        dialogue.dialogues[0][1] = "To the north there is a small \nlagoon with a beautiful view...";

        dialogue.dialogues[1][0] = "I'm starting to believe that there \nis something stranger in these \nforests.";
        dialogue.dialogues[1][1] = "Everything started happening since \nthat strange guy arrived on the \nisland.";
        dialogue.dialogues[1][2] = "I will continue exploring, until \nthen traveler!";
    }

}
