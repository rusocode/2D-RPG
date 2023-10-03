package com.craivet;

/**
 * Dialogues are the texts that are generated when you dialogue with an NPC, interact with a specific item or when an
 * event occurs. These texts are displayed within the dialog window.
 */

public class Dialogue {

    public String[][] dialogues;
    public int set, index;

    public Dialogue() {
        dialogues = new String[20][20];
    }

}
