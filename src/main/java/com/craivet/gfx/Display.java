package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;

public class Display extends JFrame {

    private final Game game;

    public Display(Game game) {
        this.game = game;
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame() {
        game.start();
    }

}
