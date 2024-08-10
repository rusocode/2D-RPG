package com.craivet.assets;

import com.craivet.utils.Utils;

import java.awt.*;

public final class FontAssets {

    public enum Type {
        MEDIEVAL1,
        MEDIEVAL2,
        MEDIEVAL3,
        MINECRAFT
    }

    private static final String font_path = "font/";

    private static final Font medieval1 = Utils.loadFont(font_path + "medieval1.ttf", 22);
    private static final Font medieval2 = Utils.loadFont(font_path + "medieval2.ttf", 32);
    private static final Font medieval3 = Utils.loadFont(font_path + "medieval3.ttf", 32);
    // TODO The font size would have to change with respect to the screen display
    private static final Font minecraft = Utils.loadFont(font_path + "minecraft.ttf", 24);

    static Font getFont(Type type) {
        return switch (type) {
            case MEDIEVAL1 -> medieval1;
            case MEDIEVAL2 -> medieval2;
            case MEDIEVAL3 -> medieval3;
            // TODO The font size would have to change with respect to the screen display
            case MINECRAFT -> minecraft;
        };
    }

    private FontAssets() {

    }

}
