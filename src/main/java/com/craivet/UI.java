package com.craivet;

import com.craivet.world.entity.Entity;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.mob.Mob;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * User interface.
 *
 * <p>TODO Replace these UIs with textures made like MC and THROW THEM TO SHIT!
 * <p>TODO Implement music for title screen (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

    private final Game game;
    private final World world;
    private Graphics2D g2;
    public Entity entity;

    private String currentDialogue, combinedText = "";
    private int charIndex, counter;

    // TODO The console would have to be a separate class
    public final ArrayList<String> console = new ArrayList<>();
    private final ArrayList<Integer> consoleCounter = new ArrayList<>();

    public int mainWindowState, subState, command; // TODO You could combine these two variables (mainWindowState and subState) referring to a single subState

    // Icons
    private BufferedImage heartFull, heartHalf, heartBlank, manaFull, manaBlank;

    public UI(Game game, World world) {
        this.game = game;
        this.world = world;
        initHUD();
    }

    public void render(Graphics2D g2) {

        this.g2 = g2;

        // Default font and color
        g2.setFont(font_minecraft);
        g2.setColor(Color.white);
        // g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // Softens the edges of the font, but in this case it is not necessary to apply it since a pixelart type font is used

        // Render windows depending on game state
        switch (game.state) {
            case MAIN_STATE -> renderMainWindow();
            case PLAY_STATE -> {
                renderHUD();
                renderBossHpBar();
            }
            case DIALOGUE_STATE -> renderDialogueWindow();
            case STATS_STATE -> renderStatsWindow();
            case INVENTORY_STATE -> renderPlayerInventoryWindow(world.player, true);
            case OPTION_STATE -> renderOptionWindow();
            case GAME_OVER_STATE -> renderGameOverWindow();
            case TRANSITION_STATE -> renderTransitionEffect();
            case TRADE_STATE -> renderTradeWindow();
            case SLEEP_STATE -> renderSleepEffect();
        }

        renderConsole();

    }

    private void renderMainWindow() {
        if (mainWindowState == MAIN_WINDOW) {
            changeFontSize(48);
            String text = "NEW GAME";
            int x = getXForCenteredText(text);
            int y = (int) (tile * 5.5);
            g2.drawString(text, x, y);
            if (command == 0) g2.drawString(">", x - tile, y);

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += tile * 2;
            g2.drawString(text, x, y);
            if (command == 1) g2.drawString(">", x - tile, y);

            text = "QUIT";
            x = getXForCenteredText(text);
            y += tile * 2;
            g2.drawString(text, x, y);
            if (command == 2) g2.drawString(">", x - tile, y);
        } else if (mainWindowState == SELECTION_WINDOW) {
            g2.setColor(Color.white);
            changeFontSize(48);

            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = tile * 3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenteredText(text);
            y += tile * 3;
            g2.drawString(text, x, y);
            if (command == 0) g2.drawString(">", x - tile, y);

            text = "Thief";
            x = getXForCenteredText(text);
            y += tile;
            g2.drawString(text, x, y);
            if (command == 1) g2.drawString(">", x - tile, y);

            text = "Sorcerer";
            x = getXForCenteredText(text);
            y += tile;
            g2.drawString(text, x, y);
            if (command == 2) g2.drawString(">", x - tile, y);

            text = "Back";
            x = getXForCenteredText(text);
            y += tile * 2;
            g2.drawString(text, x, y);
            if (command == 3) g2.drawString(">", x - tile, y);
        }
    }

    private void renderConsole() {
        int x = tile, y = tile * 4, gap = 50;
        changeFontSize(24);

        for (int i = 0; i < console.size(); i++) {
            if (console.get(i) != null) {
                // Shade
                g2.setColor(Color.black);
                g2.drawString(console.get(i), x + 2, y + 2);
                // Main color
                g2.setColor(Color.white);
                g2.drawString(console.get(i), x, y);
                // Acts as consoleCounter++
                consoleCounter.set(i, consoleCounter.get(i) + 1);

                y += gap;

                // After 3 seconds, delete the messages in the console
                if (consoleCounter.get(i) > 180) {
                    console.remove(i);
                    consoleCounter.remove(i);
                }
            }
        }
    }

    private void renderHUD() {
        int iconSize = 16;

        // 2 life represents 1 heart (heartFull) and 1 life represents half a heart (heartHalf)
        int x = iconSize / 2, y = iconSize / 2, i = 0;

        // Draw the empty hearts
        while (i < world.player.stats.maxHp / 2) {
            g2.drawImage(heartBlank, x, y, iconSize, iconSize, null);
            i++;
            x += iconSize;
        }

        // Reset the position of x to draw the other hearts (half and full)
        x = iconSize / 2;
        i = 0;

        // Draw the half and full hearts (current life) over the empty hearts from left to right while the player is alive
        while (i < world.player.stats.hp) {
            g2.drawImage(heartHalf, x, y, iconSize, iconSize, null); // Draw half a heart
            i++; // Increase the position of the next life value
            if (i < world.player.stats.hp) { // If it still has life, draw the other part of the heart (that is, a full heart)
                g2.drawImage(heartFull, x, y, iconSize, iconSize, null);
                i++;
            }
            x += iconSize;
        }

        // Draw the empty mana
        x = (iconSize / 2) - 4;
        y = (int) (iconSize * 1.5);
        i = 0;
        while (i < world.player.stats.maxMana) {
            g2.drawImage(manaBlank, x, y, iconSize, iconSize, null);
            i++;
            x += (iconSize / 2) + 1;
        }

        // Draw the current mana
        x = (iconSize / 2) - 4;
        i = 0;
        while (i < world.player.stats.mana) {
            g2.drawImage(manaFull, x, y, iconSize, iconSize, null);
            i++;
            x += (iconSize / 2) + 1;
        }

    }

    public void renderHpBar(Entity mob) {
        double oneScale = (double) tile / mob.stats.maxHp;
        double hpBarValue = oneScale * mob.stats.hp;

        // If the calculated life bar value is less than 0, assign it 0 so that it is not drawn as negative value to the left
        if (hpBarValue < 0) hpBarValue = 0;

        g2.setColor(new Color(35, 35, 35));
        g2.fillRect(mob.getScreenX() - 1, mob.getScreenY() + tile + 4, tile + 2, 7);

        g2.setColor(new Color(255, 0, 30));
        g2.fillRect(mob.getScreenX(), mob.getScreenY() + tile + 5, (int) hpBarValue, 5);

        mob.timer.timeHpBar(mob, INTERVAL_HP_BAR);
    }

    private void renderBossHpBar() {
        for (int i = 0; i < world.mobs[1].length; i++) {
            Mob mob = world.mobs[world.map][i];
            if (mob != null && mob.isOnCamera() && mob.boss) {
                double oneScale = (double) tile * 8 / mob.stats.maxHp;
                double hpBarValue = oneScale * mob.stats.hp;

                int x = WINDOW_WIDTH / 2 - tile * 4;
                int y = (int) (tile * 10.5);

                if (hpBarValue < 0) hpBarValue = 0;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(x - 1, y - 1, tile * 8 + 2, 22);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(x, y, (int) hpBarValue, 20); // TODO Or 21?

                changeFontSize(24);
                g2.setColor(Color.white);
                g2.drawString(mob.stats.name, x + 4, y - 10);
            }
        }
    }

    private void renderDialogueWindow() {
        int x = tile * 3, y = tile / 2;
        int width = WINDOW_WIDTH - (tile * 6), height = tile * 4;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        changeFontSize(14);

        // Text
        int textX = x + tile - 10;
        int textY = y + tile;

        if (entity.dialogue.dialogues[entity.dialogue.set][entity.dialogue.index] != null) {
            char[] characters = entity.dialogue.dialogues[entity.dialogue.set][entity.dialogue.index].toCharArray();

            if (charIndex < characters.length) {
                combinedText += String.valueOf(characters[charIndex]);
                currentDialogue = combinedText;
                charIndex++;
            }

            // In the case of having several dialog boxes (example, Oldman)
            if (game.keyboard.enter) {
                charIndex = 0;
                combinedText = "";
                if (game.state == DIALOGUE_STATE) {
                    entity.dialogue.index++;
                    game.keyboard.enter = false;
                }
            }
            if (game.keyboard.esc) { // TODO Or else if?
                charIndex = 0;
                combinedText = "";
                if (game.state == TRADE_STATE) {
                    entity.dialogue.index++;
                    game.keyboard.esc = false;
                }
            }

        } else { // If the dialogue ended
            entity.dialogue.index = 0;
            game.state = PLAY_STATE;
        }

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
    }

    private void renderStatsWindow() {
        int width = tile * 9, height = tile * 11;
        int x = (WINDOW_WIDTH / 2 - width / 2), y = tile - 15;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        int gap = 24;
        changeFontSize(16);

        // Names
        int textX = x + 20;
        int textY = y + tile;
        g2.drawString("Level", textX, textY);
        textY += gap;
        g2.drawString("Life", textX, textY);
        textY += gap;
        g2.drawString("Mana", textX, textY);
        textY += gap;
        g2.drawString("Strength", textX, textY);
        textY += gap;
        g2.drawString("Dexterity", textX, textY);
        textY += gap;
        g2.drawString("Attack", textX, textY);
        textY += gap;
        g2.drawString("Defense", textX, textY);
        textY += gap;
        g2.drawString("Exp", textX, textY);
        textY += gap;
        g2.drawString("Next Level Exp", textX, textY);
        textY += gap;
        g2.drawString("Gold", textX, textY);
        textY += gap + 15;
        g2.drawString("Weapon", textX, textY);
        textY += gap + 15;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = (x + width) - 30; // Tail of position x
        // Resetea textY
        textY = y + tile;
        String value;

        value = String.valueOf(world.player.stats.lvl);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = world.player.stats.hp + "/" + world.player.stats.maxHp;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = world.player.stats.mana + "/" + world.player.stats.maxMana;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.nextLvlExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.stats.gold);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        g2.drawImage(world.player.weapon.sheet.frame, tailX - 35, textY - 5, null);
        textY += gap;

        g2.drawImage(world.player.shield.sheet.frame, tailX - 35, textY + 5, null);

    }

    private void renderPlayerInventoryWindow(Player player, boolean cursor) {
        int x, y, width, height;
        int slotCol, slotRow;

        width = (int) (tile * 6.5);
        height = tile * 5;
        x = (int) ((WINDOW_WIDTH / 2 - width / 2) + tile * 4.5);
        slotCol = world.player.inventory.playerSlotCol;
        slotRow = world.player.inventory.playerSlotRow;
        y = tile - 15;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        // Slots
        final int slotXStart = x + 20, slotYStart = y + 20;
        int slotX = slotXStart, slotY = slotYStart;
        int gap = tile + 3;
        changeFontSize(14);

        for (int i = 0; i < player.inventory.size(); i++) {

            // Mark the selected item in yellow
            if (player.inventory.get(i) == player.weapon || player.inventory.get(i) == player.shield || player.inventory.get(i) == player.light) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRect(slotX, slotY, tile, tile);
            }

            // Draw the item
            g2.drawImage(player.inventory.get(i).sheet.frame, slotX, slotY, null);

            // Draw the quantity of the item if it has more than 1
            if (player.inventory.get(i).amount > 1) {
                int amountX, amountY;
                String amount = String.valueOf(player.inventory.get(i).amount);
                amountX = getXforAlignToRightText(amount, slotX + gap);
                amountY = slotY + tile - 2;
                g2.setColor(Color.white);
                g2.drawString(amount, amountX, amountY);
            }

            slotX += gap;

            // Skip to the next row
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gap;
            }

        }

        // Cursor
        if (cursor) {
            int cursorX = slotXStart + (gap * slotCol);
            int cursorY = slotYStart + (gap * slotRow);

            // Draw the cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(cursorX, cursorY, tile, tile);

            // Description window
            int dFrameY = y + height;
            int dFrameHeight = tile * 4;

            // Draw the description
            int textX = x + 20;
            int textY = dFrameY + tile + 5;
            int itemIndex = world.player.inventory.getSlot(slotCol, slotRow);
            if (itemIndex < player.inventory.size()) {
                renderSubwindow(x, dFrameY, width, dFrameHeight, SUBWINDOW_ALPHA);
                changeFontSize(14);
                for (String line : player.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }

    }

    private void renderTraderInventoryWindow(Entity entity) {
        int x, y, width, height;
        int slotCol, slotRow;

        width = (int) (tile * 6.5);
        height = tile * 5;
        x = (int) ((WINDOW_WIDTH / 2 - width / 2) - tile * 4.5);
        slotCol = world.player.inventory.npcSlotCol;
        slotRow = world.player.inventory.npcSlotRow;
        y = tile - 15;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        // Slots
        final int slotXStart = x + 20, slotYStart = y + 20;
        int slotX = slotXStart, slotY = slotYStart;
        int gap = tile + 3;
        changeFontSize(14);

        for (int i = 0; i < entity.inventory.size(); i++) {

            if (entity.inventory.get(i) == world.player.weapon || entity.inventory.get(i) == world.player.shield || entity.inventory.get(i) == world.player.light) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRect(slotX, slotY, tile, tile);
            }

            g2.drawImage(entity.inventory.get(i).sheet.frame, slotX, slotY, null);

            if (entity.inventory.get(i).amount > 1) {
                int amountX, amountY;
                String amount = String.valueOf(entity.inventory.get(i).amount);
                amountX = getXforAlignToRightText(amount, slotX + gap);
                amountY = slotY + tile - 2;
                g2.setColor(Color.white);
                g2.drawString(amount, amountX, amountY);
            }

            slotX += gap;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gap;
            }

        }

        int cursorX = slotXStart + (gap * slotCol);
        int cursorY = slotYStart + (gap * slotRow);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(cursorX, cursorY, tile, tile);

        int dFrameY = y + height;
        int dFrameHeight = tile * 4;

        int textX = x + 20;
        int textY = dFrameY + tile + 5;
        int itemIndex = world.player.inventory.getSlot(slotCol, slotRow);
        if (itemIndex < entity.inventory.size()) {
            renderSubwindow(x, dFrameY, width, dFrameHeight, SUBWINDOW_ALPHA);
            changeFontSize(14);
            for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    private void renderOptionWindow() {
        int width = tile * 10, height = tile * 10;
        int x = (WINDOW_WIDTH / 2 - width / 2), y = tile;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        switch (subState) {
            case 0 -> renderOptionMainWindow(x, y, width, height);
            case 1 -> renderOptionControlWindow(x, y, width, height);
            case 2 -> renderOptionEndGameConfirmationWindow(x, y);
        }

        game.keyboard.enter = false;

    }

    private void renderOptionMainWindow(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX, textY;

        // Title
        changeFontSize(28);
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + tile;
        g2.drawString(text, textX, textY);

        changeFontSize(28f);

        // Full Screen
        // Music
        textX = frameX + tile;
        textY += tile * 2;
        g2.drawString("Music", textX, textY);
        if (command == 0) g2.drawString(">", textX - 25, textY);
        // Sound
        textY += tile;
        g2.drawString("Sound", textX, textY);
        if (command == 1) g2.drawString(">", textX - 25, textY);
        // Control
        textY += tile;
        g2.drawString("Control", textX, textY);
        if (command == 2) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 1;
                command = 0;
            }
        }
        // Save Game
        textY += tile;
        g2.drawString("Save Game", textX, textY);
        if (command == 3) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                game.file.saveData();
                game.state = PLAY_STATE;
                game.ui.addMessageToConsole("Game saved!");
                command = 0;
            }
        }
        // End Game
        textY += tile;
        g2.drawString("End Game", textX, textY);
        if (command == 4) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 2;
                command = 0;
            }
        }

        // Music volume
        textX = frameX + frameWidth - (tile * 4);
        textY = frameY + (tile * 2 + 7);
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * game.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        // Sound volume
        textY += tile;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * game.sound.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // Back
        textX = frameX + tile;
        textY = frameHeight;
        g2.drawString("Back", textX, textY);
        if (command == 5) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                game.state = PLAY_STATE;
                command = 0;
            }
        }

        game.file.saveConfig();

    }

    private void renderOptionControlWindow(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX, textY;

        // Title
        changeFontSize(33);
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + tile;
        g2.drawString(text, textX, textY);
        // Title

        changeFontSize(22f);

        textX = frameX + tile;
        textY += tile - 10;
        g2.drawString("Move", textX, textY);
        textY += tile;
        g2.drawString("Confirm/Attack", textX, textY);
        textY += tile;
        g2.drawString("Shoot", textX, textY);
        textY += tile;
        g2.drawString("Minimap", textX, textY);
        textY += tile;
        g2.drawString("Debug", textX, textY);
        textY += tile;
        g2.drawString("Character Screen", textX, textY);
        textY += tile;
        g2.drawString("Pickup item", textX, textY);
        textY += tile;
        g2.drawString("Options/Back", textX, textY);

        textX = frameX + frameWidth - tile * 3;
        textY = (frameY + tile * 2) - 10;
        g2.drawString("WASD", textX, textY);
        textY += tile;
        g2.drawString("ENTER", textX, textY);
        textY += tile;
        g2.drawString("F", textX, textY);
        textY += tile;
        g2.drawString("M", textX, textY);
        textY += tile;
        g2.drawString("T", textX, textY);
        textY += tile;
        g2.drawString("C", textX, textY);
        textY += tile;
        g2.drawString("P", textX, textY);
        textY += tile;
        g2.drawString("ESC", textX, textY);

        // Back
        textX = frameX + tile;
        textY = frameHeight + 30;
        g2.drawString("Back", textX, textY);
        if (command == 0) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 0;
                command = 2;
            }
        }
    }

    private void renderOptionEndGameConfirmationWindow(int frameX, int frameY) {
        int textX = frameX + tile;
        int textY = frameY + tile * 3;

        currentDialogue = "Quit the game and \nreturn to the title \nscreen?";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += (int) (tile * 1.9);
        g2.drawString(text, textX, textY);
        if (command == 0) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 0;
                game.state = MAIN_STATE;
                game.keyboard.t = false;
                game.reset(true);
            }
        }

        text = "No";
        textX = getXForCenteredText(text);
        textY += tile;
        g2.drawString(text, textX, textY);
        if (command == 1) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 0;
                command = 4;
            }
        }

    }

    private void renderGameOverWindow() {
        int x, y;
        String text;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        changeFontSize(82);

        text = "Game Over";
        // Shade
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = tile * 4;
        g2.drawString(text, x, y);
        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);
        // Retry
        changeFontSize(50);
        text = "Retry";
        x = getXForCenteredText(text);
        y += tile * 4;
        g2.drawString(text, x, y);
        if (command == 0) g2.drawString(">", x - 40, y);
        // Quit
        text = "Quit";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if (command == 1) g2.drawString(">", x - 40, y);

    }

    /**
     * Renders a transition effect and when it finishes, teleports the player.
     */
    private void renderTransitionEffect() {
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        counter++;
        if (counter >= INTERVAL_TRANSITION) {
            counter = 0;
            game.state = PLAY_STATE;
            world.map = game.event.map;
            world.player.pos.x = (game.event.col * tile) + world.player.hitbox.width / 2;
            world.player.pos.y = (game.event.row * tile) - world.player.hitbox.height;
            game.event.previousEventX = world.player.pos.x;
            game.event.previousEventY = world.player.pos.y;
            world.changeArea();
        }
    }

    private void renderTradeWindow() {
        switch (subState) {
            case 0 -> renderTradeMainWindow();
            case 1 -> renderTradeBuyWindow();
            case 2 -> renderTradeSellWindow();
        }
        game.keyboard.enter = false; // Reset keyboard input
    }

    private void renderTradeMainWindow() {
        renderDialogueWindow();

        int x = tile * 4, y = tile * 4;

        g2.drawString("Buy", x, y);
        if (command == 0) {
            g2.drawString(">", x - 14, y);
            if (game.keyboard.enter) subState = 1;
        }

        x += 4 * tile;

        g2.drawString("Sell", x, y);
        if (command == 1) {
            g2.drawString(">", x - 14, y);
            if (game.keyboard.enter) subState = 2;
        }
    }

    private void renderTradeBuyWindow() {
        int x, y, width, height;

        // Depending on the type of entity, it renders the inventory on the right side (player) or left side (npc)
        renderPlayerInventoryWindow(world.player, false);
        renderTraderInventoryWindow(entity);

        // Price window
        int itemIndex = world.player.inventory.getSlot(world.player.inventory.npcSlotCol, world.player.inventory.npcSlotRow);
        if (itemIndex < entity.inventory.size()) {
            x = tile * 5;
            y = tile * 5;
            width = (int) (tile * 2.5);
            height = tile;
            renderSubwindow(x, y, width, height, 255);
            // Gold image
            g2.drawImage(gold, x + 6, y + 10, null);
            // Item price
            int price = entity.inventory.get(itemIndex).price;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, x + width);
            g2.drawString(text, x, y + 24);

            // Buy an item
            if (game.keyboard.enter) {
                if (entity.inventory.get(itemIndex).price > world.player.stats.gold)
                    addMessageToConsole("You need more gold to buy that!");
                else {
                    // TODO
                    if (world.player.inventory.canPickup(entity.inventory.get(itemIndex))) {
                        game.playSound(sound_trade_buy);
                        world.player.stats.gold -= entity.inventory.get(itemIndex).price;
                    } else addMessageToConsole("You cannot carry any more!");
                }
            }

        }

        // Gold window available
        x = (int) (tile * 9.75);
        y = (int) (tile * 5.68);
        width = (int) (tile * 6.5);
        height = tile * 2;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Gold: " + world.player.stats.gold, x + 24, y + 40);

    }

    private void renderTradeSellWindow() {
        int x, y, width, height;

        renderPlayerInventoryWindow(world.player, true);
        int itemIndex = world.player.inventory.getSlot(world.player.inventory.playerSlotCol, world.player.inventory.playerSlotRow);
        if (itemIndex < world.player.inventory.size()) {
            x = tile * 14;
            y = tile * 5;
            width = (int) (tile * 2.5);
            height = tile;
            renderSubwindow(x, y, width, height, 255);
            g2.drawImage(gold, x + 6, y + 10, null);
            int price = world.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, x + width);
            g2.drawString(text, x, y + 24);

            // Sell an item
            if (game.keyboard.enter) {
                if (world.player.inventory.get(itemIndex) == world.player.weapon || world.player.inventory.get(itemIndex) == world.player.shield)
                    addMessageToConsole("You cannot sell an equipped item!");
                else {
                    game.playSound(sound_trade_sell);
                    if (world.player.inventory.get(itemIndex).amount > 1)
                        world.player.inventory.get(itemIndex).amount--;
                    else world.player.inventory.remove(itemIndex);
                    world.player.stats.gold += price;
                }
            }

        }

        // Gold window available
        x = (int) (tile * 9.75);
        y = (int) (tile * 9.68);
        width = (int) (tile * 6.5);
        height = tile * 2;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Gold: " + world.player.stats.gold, x + 24, y + 40);

    }

    /**
     * Renders a dream effect that goes from darkest to brightest.
     */
    private void renderSleepEffect() {
        // If the counter is less than 120, darken the render by 0.01 until it is completely dark
        if (counter < 120) {
            counter++;
            world.environment.lighting.filterAlpha += 0.01f;
            // If the value exceeds the maximum darkness, set it to 1
            if (world.environment.lighting.filterAlpha > 1f) world.environment.lighting.filterAlpha = 1f;
        }
        // When you reach 120, brighten the render by 0.01 until it is completely bright
        if (counter >= 120) {
            world.environment.lighting.filterAlpha -= 0.01f;
            if (world.environment.lighting.filterAlpha <= 0f) {
                world.environment.lighting.filterAlpha = 0f;
                world.environment.lighting.dayState = world.environment.lighting.day;
                world.environment.lighting.dayCounter = 0;
                world.player.currentFrame = world.player.sheet.down[0];
                game.state = PLAY_STATE;
                counter = 0; // Reset the counter to regenerate the effect from 0
            }
        }
    }

    /**
     * Draw a sub window.
     *
     * @param x      position x of the window.
     * @param y      position y of the window.
     * @param width  width of the window.
     * @param height height of the window.
     * @param alpha  window transparency.
     */
    private void renderSubwindow(int x, int y, int width, int height, int alpha) {
        // Black background
        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRoundRect(x, y, width, height, 10, 10);
        // White background
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(3)); // Edge thickness
        g2.drawRoundRect(x, y, width, height, 10, 10);
    }

    private void initHUD() {
        BufferedImage[] subimages = SpriteSheet.getIconsSubimages(icons, 16, 16);
        heartFull = Utils.scaleImage(subimages[0], tile, tile);
        heartHalf = Utils.scaleImage(subimages[1], tile, tile);
        heartBlank = Utils.scaleImage(subimages[2], tile, tile);
        manaFull = Utils.scaleImage(subimages[3], tile, tile);
        manaBlank = Utils.scaleImage(subimages[4], tile, tile);
    }

    private void changeFontSize(float size) {
        g2.setFont(g2.getFont().deriveFont(size));
    }

    public void addMessageToConsole(String msg) {
        console.add(msg);
        consoleCounter.add(0); // I think it prevents an IndexOutOfBoundsException
    }

    /**
     * Gets x aligned to the right.
     *
     * @param text  the text.
     * @param tailX the tail of x.
     */
    private int getXforAlignToRightText(String text, int tailX) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        // Additional value so that the text (for example, amount of gold) is not so close to the right edge of the window
        int distanceToTheRightBorder = 2;
        return tailX - textLength - distanceToTheRightBorder;
    }

    /**
     * Get x for centered text.
     *
     * @param text the text.
     */
    private int getXForCenteredText(String text) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return WINDOW_WIDTH / 2 - textLength / 2;
    }

    private int getYForCenteredText(String text) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getHeight();
        return WINDOW_HEIGHT / 2 + textLength / 2;
    }

}
