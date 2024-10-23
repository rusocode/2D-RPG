package com.punkipunk.assets;

import com.punkipunk.utils.Utils;
import javafx.scene.text.Font;

// TODO The font size would have to change with respect to the screen display
public enum FontAssets {

    TEST,
    BLACK_PEARL,
    KNIGHTS_QUEST,
    MINECRAFTIA;

    private static final String font_path = "font/";

    private static final Font black_pearl = Utils.loadFont(font_path + "BlackPearl.ttf", 18);
    private static final Font knights_quest = Utils.loadFont(font_path + "Knights Quest.ttf", 32);
    private static final Font minecraftia = Utils.loadFont(font_path + "Minecraftia.ttf", 12);
    private static final Font test = Utils.loadFont(font_path + "Morris Roman.ttf", 24);

    static Font getFont(FontAssets font) {
        return switch (font) {
            case TEST -> test;
            case BLACK_PEARL -> black_pearl;
            case KNIGHTS_QUEST -> knights_quest;
            case MINECRAFTIA -> minecraftia;
        };
    }

}
