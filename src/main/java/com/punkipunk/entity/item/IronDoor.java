package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

public class IronDoor extends Item {

    public IronDoor(Game game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        initDialogue();
    }

    @Override
    public void interact() {
        dialogue.startDialogue(State.DIALOGUE, this, 0);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "It won't budge.";
    }

    @Override
    public ItemType getType() {
        return ItemType.IRON_DOOR;
    }

}
