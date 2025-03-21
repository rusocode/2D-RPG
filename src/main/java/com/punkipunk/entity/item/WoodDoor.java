package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

public class WoodDoor extends Item {

    public WoodDoor(Game game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        initDialogue();
    }

    @Override
    public void interact() {
        dialogue.startDialogue(State.DIALOGUE, this, 0);
    }

    @Override
    public ItemID getID() {
        return ItemID.WOOD_DOOR;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "You need a key to open this.";
    }

}
