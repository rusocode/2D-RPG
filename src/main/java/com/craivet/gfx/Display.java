package com.craivet.gfx;

import com.craivet.Game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.craivet.utils.Global.*;

public class Display extends JFrame {

    private final Game game;

    public Display(Game game) {
        this.game = game;
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game.state == PLAY_STATE || game.state == OPTION_STATE || game.state == CHARACTER_STATE) {
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to save the changes?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (option) {
                        case JOptionPane.YES_OPTION -> {
                            game.saveLoad.save();
                            System.exit(0);
                        }
                        case JOptionPane.NO_OPTION -> System.exit(0);
                        case JOptionPane.CANCEL_OPTION -> setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        });
        setVisible(true);
    }

    public void startGame() {
        game.start();
    }

}
