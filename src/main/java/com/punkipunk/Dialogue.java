package com.punkipunk;

import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.entity.Entity;

/**
 * Dialogues are the texts that are generated when you interact with an NPC, with a specific item or when an event occurs. These
 * texts are displayed within the dialog window.
 */

public class Dialogue {

    private final Game game;

    public String[][] dialogues;
    public int set, index;

    public Dialogue(Game game) {
        dialogues = new String[20][20];
        this.game = game;
    }

    public void startDialogue(State state, Entity entity, int set) {
        State.setState(state);
        game.gameSystem.ui.entity = entity;
        this.set = set;
    }

}
