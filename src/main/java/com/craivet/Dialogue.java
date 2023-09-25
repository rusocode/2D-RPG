package com.craivet;

/**
 * Los dialogos son los textos que se generan cuando se dialoga con un npc, se interactua con un item especifico o
 * cuando se produce un evento. Estos textos se visualizan dentro de la ventana de dialogo.
 */

public class Dialogue {

    public String[][] dialogues;
    public int set, index;

    public Dialogue() {
        dialogues = new String[20][20];
    }

}
