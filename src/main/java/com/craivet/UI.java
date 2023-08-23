package com.craivet;

import com.craivet.world.entity.Entity;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Utils;
import com.craivet.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

/**
 * Interfaz de usuario.
 *
 * <p>TODO Reemplazar estas UI por GUIS echas como MC
 * <p>TODO Implementar musica para pantalla de titulo (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

    private final Game game;
    private final World world;
    public Entity entity;
    private Graphics2D g2;

    public String currentDialogue = "";
    int charIndex;
    String combinedText = "";

    public final ArrayList<String> message = new ArrayList<>();
    private final ArrayList<Integer> messageCounter = new ArrayList<>();

    public int playerSlotCol, playerSlotRow;
    public int npcSlotCol, npcSlotRow;
    public int titleScreenState;
    public int subState;
    public int command;
    private int counter;

    // Icons
    private BufferedImage heartFull, heartHalf, heartBlank;
    private BufferedImage manaFull, manaBlank;

    public UI(Game game, World world) {
        this.game = game;
        this.world = world;
        initHUD();
    }

    private void initHUD() {
        BufferedImage[] subimages = SpriteSheet.getIconsSubimages(icons, 16, 16);
        heartFull = Utils.scaleImage(subimages[0], tile_size, tile_size);
        heartHalf = Utils.scaleImage(subimages[1], tile_size, tile_size);
        heartBlank = Utils.scaleImage(subimages[2], tile_size, tile_size);
        manaFull = Utils.scaleImage(subimages[3], tile_size, tile_size);
        manaBlank = Utils.scaleImage(subimages[4], tile_size, tile_size);
    }

    public void render(Graphics2D g2) {

        this.g2 = g2;

        // TODO Esto va aca o en el constructor?
        // Fuente y color por defecto
        g2.setFont(font_marumonica);
        g2.setColor(Color.white);
        // Suaviza los bordes de la fuente
        // g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (game.state == MAIN_STATE) drawTitleScreen();
        if (game.state == PLAY_STATE) {
            drawPlayerStats();
            drawConsole();
        }
        if (game.state == PAUSE_STATE) {
            drawPlayerStats();
            drawPauseText();
        }
        if (game.state == DIALOGUE_STATE) drawDialogueScreen();
        if (game.state == CHARACTER_STATE) {
            drawCharacterScreen();
            drawInventory(world.player, true);
        }
        if (game.state == OPTION_STATE) drawOptionScreen();
        if (game.state == GAME_OVER_STATE) drawGameOverScreen();
        if (game.state == TRANSITION_STATE) drawTransition();
        if (game.state == TRADE_STATE) {
            drawTradeScreen();
            drawConsole();
        }
        if (game.state == SLEEP_STATE) drawSleepScreen();

    }

    private void drawTitleScreen() {
        if (titleScreenState == MAIN_SCREEN) {

            // Title
            changeFontSize(96f);
            String text = "Hersir";
            int x = getXForCenteredText(text);
            int y = tile_size * 3;
            // Title

            // Shadow
            g2.setColor(Color.gray);
            // g2.drawString(text, x + 5, y + 5);

            // Main color
            g2.setColor(Color.white);
            // g2.drawString(text, x, y);

            // Image
            x = SCREEN_WIDTH / 2 - (tile_size * 2) / 2;
            y += tile_size * 2;
            // g2.drawImage(world.player.down.getFirstFrame(), x, y, tile_size, tile_size * 2, null);

            // Menu
            changeFontSize(48f);
            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += tile_size * 3.5;
            g2.drawString(text, x, y);
            if (command == 0) g2.drawString(">", x - tile_size, y);

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += tile_size;
            g2.drawString(text, x, y);
            if (command == 1) g2.drawString(">", x - tile_size, y);

            text = "QUIT";
            x = getXForCenteredText(text);
            y += tile_size;
            g2.drawString(text, x, y);
            if (command == 2) g2.drawString(">", x - tile_size, y);
        } else if (titleScreenState == SELECTION_SCREEN) {
            g2.setColor(Color.white);
            changeFontSize(42f);

            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = tile_size * 3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenteredText(text);
            y += tile_size * 3;
            g2.drawString(text, x, y);
            if (command == 0) g2.drawString(">", x - tile_size, y);

            text = "Thief";
            x = getXForCenteredText(text);
            y += tile_size;
            g2.drawString(text, x, y);
            if (command == 1) g2.drawString(">", x - tile_size, y);

            text = "Sorcerer";
            x = getXForCenteredText(text);
            y += tile_size;
            g2.drawString(text, x, y);
            if (command == 2) g2.drawString(">", x - tile_size, y);

            text = "Back";
            x = getXForCenteredText(text);
            y += tile_size * 2;
            g2.drawString(text, x, y);
            if (command == 3) g2.drawString(">", x - tile_size, y);
        }
    }

    private void drawConsole() {
        int messageX = tile_size;
        int messageY = tile_size * 4;
        changeFontSize(29f);

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {

                // Sombra
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);
                // Color principal
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                // Actua como messageCounter++
                messageCounter.set(i, messageCounter.get(i) + 1);

                messageY += 50;

                // Despues de 3 segundos, elimina los mensajes en consola
                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawPlayerStats() {

        // world.player.life = 1;
        // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)

        int x = tile_size / 2;
        int y = tile_size / 2;
        int i = 0;

        // Dibuja los corazones vacios
        while (i < world.player.maxHp / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += tile_size;
        }

        // Resetea la posicion de x para dibujar los otros corazones (medio y lleno)
        x = tile_size / 2;
        i = 0;

        /* Dibuja los corazones medios y llenos (vida actual) sobre los corazones vacios de izquierda a derecha mientras
         * el player tenga vida. */
        while (i < world.player.hp) {
            g2.drawImage(heartHalf, x, y, null); // Dibuja medio corazon
            i++; // Incrementa la posicion del siguiente valor de vida
            if (i < world.player.hp) { // Si todavia tiene vida, dibuja la otra parte del corazon (osea, un corazon lleno)
                g2.drawImage(heartFull, x, y, null);
                i++;
            }
            x += tile_size;
        }

        // Dibuja la mana vacia
        x = (tile_size / 2) - 4;
        y = (int) (tile_size * 1.5);
        i = 0;
        while (i < world.player.maxMana) {
            g2.drawImage(manaBlank, x, y, null);
            i++;
            x += (tile_size / 2) + 1;
        }

        // Dibuja la mana actual
        x = (tile_size / 2) - 4;
        i = 0;
        while (i < world.player.mana) {
            g2.drawImage(manaFull, x, y, null);
            i++;
            x += (tile_size / 2) + 1;
        }

    }

    private void drawPauseText() {
        changeFontSize(80);
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = getYForCenteredText(text);
        g2.drawString(text, x, y);
    }

    private void drawDialogueScreen() {
        final int frameX = tile_size * 3;
        final int frameY = tile_size / 2;
        final int frameWidth = SCREEN_WIDTH - (tile_size * 6);
        final int frameHeight = tile_size * 4;
        drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

        changeFontSize(22f);

        // Text
        int textX = frameX + tile_size;
        int textY = frameY + tile_size;

        if (entity.dialogues[entity.dialogueSet][entity.dialogueIndex] != null) {
            char[] characters = entity.dialogues[entity.dialogueSet][entity.dialogueIndex].toCharArray();

            if (charIndex < characters.length) {
                combinedText += String.valueOf(characters[charIndex]);
                currentDialogue = combinedText;
                charIndex++;
            }

            // En el caso de tener varios cuadros de dialogos (ejemplo, Oldman)
            if (game.keyboard.enter) {
                charIndex = 0;
                combinedText = "";
                if (game.state == DIALOGUE_STATE) {
                    entity.dialogueIndex++;
                    game.keyboard.enter = false;
                }
            }
            if (game.keyboard.esc) { // TODO o else if?
                charIndex = 0;
                combinedText = "";
                if (game.state == TRADE_STATE) {
                    entity.dialogueIndex++;
                    game.keyboard.esc = false;
                }
            }

        } else { // Si la conversacion termino
            entity.dialogueIndex = 0;
            game.state = PLAY_STATE;
        }

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
    }

    private void drawCharacterScreen() {
        final int frameWidth = tile_size * 10;
        final int frameHeight = tile_size * 11;
        final int frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) - tile_size * 4;
        final int frameY = tile_size - 15;
        drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

        final int gap = 37; // Espacio entre lineas
        changeFontSize(28f);

        // Names
        int textX = frameX + 20;
        int textY = frameY + tile_size;
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
        int tailX = (frameX + frameWidth) - 30; // Cola de la posicion x
        // Reset textY
        textY = frameY + tile_size;
        String value;

        value = String.valueOf(world.player.lvl);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = world.player.hp + "/" + world.player.maxHp;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = world.player.mana + "/" + world.player.maxMana;
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.nextLvlExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        value = String.valueOf(world.player.gold);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += gap;

        g2.drawImage(world.player.weapon.image, tailX - 40, textY - 15, null);
        textY += gap;

        g2.drawImage(world.player.shield.image, tailX - 40, textY - 5, null);

    }

    private void drawInventory(Entity entity, boolean cursor) {
        int frameX, frameY;
        int frameWidth, frameHeight;
        int slotCol, slotRow;

        // Inventario del lado derecho (player)
        if (entity == world.player) {
            frameWidth = tile_size * 6;
            frameHeight = tile_size * 5;
            frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) + tile_size * 5;
            frameY = tile_size - 15;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else { // Inventario del lado izquierdo (npc)
            frameWidth = tile_size * 6;
            frameHeight = tile_size * 5;
            frameX = (SCREEN_WIDTH / 2 - frameWidth / 2) - tile_size * 5;
            frameY = tile_size - 15;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

        // Slots
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int gap = tile_size + 3;
        // TODO Estas constantes y variables no tendrian que declararse como globales?

        // Draw player´s items
        for (int i = 0; i < entity.inventory.size(); i++) {
            // Remarca de color amarillo el item seleccionado
            if (entity.inventory.get(i) == entity.weapon || entity.inventory.get(i) == entity.shield || entity.inventory.get(i) == entity.light) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRect(slotX, slotY, tile_size, tile_size);
            }

            g2.drawImage(entity.inventory.get(i).image, slotX, slotY, null);

            if (entity.inventory.get(i).amount > 1) {
                g2.setFont(new Font("Consolas", Font.BOLD, 18));
                int amountX, amountY;
                String amount = String.valueOf(entity.inventory.get(i).amount);
                amountX = getXforAlignToRightText(amount, slotX + 44);
                amountY = slotY + tile_size - 2;

                // Number
                g2.setColor(Color.white);
                g2.drawString(amount, amountX, amountY);

                g2.setFont(font_medieval1);

            }

            slotX += gap;

            // Salta la fila
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gap;
            }

        }

        // Cursor
        if (cursor) {
            int cursorX = slotXStart + (gap * slotCol);
            int cursorY = slotYStart + (gap * slotRow);

            // Draw cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(cursorX, cursorY, tile_size, tile_size);

            // Description frame
            int dFrameY = frameY + frameHeight;
            int dFrameHeight = tile_size * 3;

            // Draw description text
            int textX = frameX + 20;
            int textY = dFrameY + tile_size + 5;
            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if (itemIndex < entity.inventory.size()) {
                drawSubwindow(frameX, dFrameY, frameWidth, dFrameHeight, SUBWINDOW_ALPHA);
                changeFontSize(18f);
                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }

    }

    private void drawOptionScreen() {
        int frameWidth = tile_size * 10;
        int frameHeight = tile_size * 10;
        int frameX = (SCREEN_WIDTH / 2 - frameWidth / 2);
        int frameY = tile_size;
        drawSubwindow(frameX, frameY, frameWidth, frameHeight, SUBWINDOW_ALPHA);

        switch (subState) {
            case 0 -> optionsMain(frameX, frameY, frameWidth, frameHeight);
            case 1 -> optionsControl(frameX, frameY, frameWidth, frameHeight);
            case 2 -> optionsEndGameConfirmation(frameX, frameY);
        }

        game.keyboard.enter = false;

    }

    private void optionsMain(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX, textY;

        // Title
        changeFontSize(28f);
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + tile_size;
        g2.drawString(text, textX, textY);
        // Title

        changeFontSize(28f);

        // Full Screen
        // Music
        textX = frameX + tile_size;
        textY += tile_size * 2;
        g2.drawString("Music", textX, textY);
        if (command == 0) g2.drawString(">", textX - 25, textY);
        // Sound
        textY += tile_size;
        g2.drawString("Sound", textX, textY);
        if (command == 1) g2.drawString(">", textX - 25, textY);
        // Control
        textY += tile_size;
        g2.drawString("Control", textX, textY);
        if (command == 2) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 1;
                command = 0;
            }
        }
        // Save Game
        textY += tile_size;
        g2.drawString("Save Game", textX, textY);
        if (command == 3) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                game.file.saveData();
                game.state = PLAY_STATE;
                game.ui.addMessage("Game saved!");
                command = 0;
            }
        }
        // End Game
        textY += tile_size;
        g2.drawString("End Game", textX, textY);
        if (command == 4) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 2;
                command = 0;
            }
        }

        // Music volume
        textX = frameX + frameWidth - tile_size * 3;
        textY = frameY + tile_size * 2 + 28;
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * game.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        // Sound volume
        textY += tile_size;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * game.sound.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // Back
        textX = frameX + tile_size;
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

    private void optionsControl(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX, textY;

        // Title
        changeFontSize(33f);
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + tile_size;
        g2.drawString(text, textX, textY);
        // Title

        changeFontSize(22f);

        textX = frameX + tile_size;
        textY += tile_size - 10;
        g2.drawString("Move", textX, textY);
        textY += tile_size;
        g2.drawString("Confirm/Attack", textX, textY);
        textY += tile_size;
        g2.drawString("Shoot", textX, textY);
        textY += tile_size;
        g2.drawString("Minimap", textX, textY);
        textY += tile_size;
        g2.drawString("Debug", textX, textY);
        textY += tile_size;
        g2.drawString("Character Screen", textX, textY);
        textY += tile_size;
        g2.drawString("Pause", textX, textY);
        textY += tile_size;
        g2.drawString("Options/Back", textX, textY);

        textX = frameX + frameWidth - tile_size * 3;
        textY = (frameY + tile_size * 2) - 10;
        g2.drawString("WASD", textX, textY);
        textY += tile_size;
        g2.drawString("ENTER", textX, textY);
        textY += tile_size;
        g2.drawString("F", textX, textY);
        textY += tile_size;
        g2.drawString("M", textX, textY);
        textY += tile_size;
        g2.drawString("T", textX, textY);
        textY += tile_size;
        g2.drawString("C", textX, textY);
        textY += tile_size;
        g2.drawString("P", textX, textY);
        textY += tile_size;
        g2.drawString("ESC", textX, textY);

        // Back
        textX = frameX + tile_size;
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

    private void optionsEndGameConfirmation(int frameX, int frameY) {
        int textX = frameX + tile_size;
        int textY = frameY + tile_size * 3;

        currentDialogue = "Quit the game and \nreturn to the title screen?";
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // YES
        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += tile_size * 3;
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

        // NO
        text = "NO";
        textX = getXForCenteredText(text);
        textY += tile_size;
        g2.drawString(text, textX, textY);
        if (command == 1) {
            g2.drawString(">", textX - 25, textY);
            if (game.keyboard.enter) {
                subState = 0;
                command = 4;
            }
        }

    }

    private void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        int x, y;
        String text;

        changeFontSize(110);

        text = "Game Over";
        // Sombra
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = tile_size * 4;
        g2.drawString(text, x, y);
        // Principal
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);
        // Retry
        changeFontSize(50);
        text = "Retry";
        x = getXForCenteredText(text);
        y += tile_size * 4;
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
     * Cuando el efecto de transicion termina, entonces teletransporta al player.
     */
    private void drawTransition() {
        counter++;
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        if (counter >= INTERVAL_TRANSITION) {
            counter = 0;
            game.state = PLAY_STATE;
            world.map = game.event.map;
            world.player.x = tile_size * game.event.col;
            world.player.y = tile_size * game.event.row - 122 / 2;
            game.event.previousEventX = world.player.x;
            game.event.previousEventY = world.player.y;
            world.changeArea();
        }
    }

    private void drawTradeScreen() {
        switch (subState) {
            case 0 -> tradeSelect();
            case 1 -> tradeBuy();
            case 2 -> tradeSell();
        }
        game.keyboard.enter = false; // Reinicia la entrada de teclado
    }

    private void tradeSelect() {
        drawDialogueScreen();

        int x = tile_size * 4;
        int y = tile_size * 4;

        // Draw texts
        g2.drawString("Buy", x, y);
        if (command == 0) {
            g2.drawString(">", x - 24, y);
            if (game.keyboard.enter) subState = 1;
        }

        x += 4 * tile_size;

        g2.drawString("Sell", x, y);
        if (command == 1) {
            g2.drawString(">", x - 24, y);
            if (game.keyboard.enter) subState = 2;
        }
    }

    private void tradeBuy() {
        drawInventory(world.player, false);
        drawInventory(entity, true);

        // Draw hint window
        int x = tile_size * 2;
        int y = tile_size * 9;
        int width = tile_size * 6;
        int height = tile_size * 2;
        drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        changeFontSize(23f);
        g2.drawString("[esc] Back", x + 24, y + 60);

        // Draw player gold window
        x = tile_size * 12;
        drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Your gold: " + world.player.gold, x + 24, y + 60);

        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if (itemIndex < entity.inventory.size()) {
            // Draw price window
            x = (int) (tile_size * 5.5);
            y = (int) (tile_size * 5.2);
            width = (int) (tile_size * 2.5);
            height = tile_size;
            drawSubwindow(x, y, width, height, 240);

            // Draw image gold
            g2.drawImage(gold, x + 14, y + 10, 32, 32, null);

            // Draw price item
            int price = entity.inventory.get(itemIndex).price;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, tile_size * 8 - 20);
            g2.drawString(text, x, y + 32);

            // BUY AN ITEM
            if (game.keyboard.enter) {
                if (entity.inventory.get(itemIndex).price > world.player.gold)
                    addMessage("You need more gold to buy that!");
                else {
                    // TODO Especificar la cantidad de items a comprar
                    if (world.player.canPickup(entity.inventory.get(itemIndex))) {
                        game.playSound(sound_trade_buy);
                        world.player.gold -= entity.inventory.get(itemIndex).price;
                    } else addMessage("You cannot carry any more!");
                }
            }

        }

    }

    private void tradeSell() {
        drawInventory(world.player, true);

        // Draw hint window
        int x = tile_size * 2;
        int y = tile_size * 9;
        int width = tile_size * 6;
        int height = tile_size * 2;
        drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        changeFontSize(23f);
        g2.drawString("[esc] Back", x + 24, y + 60);

        // Draw player gold window
        x = tile_size * 12;
        drawSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Your gold: " + world.player.gold, x + 24, y + 60);

        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < world.player.inventory.size()) {
            // Draw price window
            x = (int) (tile_size * 15.5);
            y = (int) (tile_size * 5.2);
            width = (int) (tile_size * 2.5);
            height = tile_size;
            drawSubwindow(x, y, width, height, 255);

            // Draw image icon
            g2.drawImage(gold, x + 14, y + 10, 32, 32, null);

            // Draw price item
            int price = world.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, tile_size * 18 - 20);
            g2.drawString(text, x, y + 32);

            // SELL AN ITEM
            if (game.keyboard.enter) {
                if (world.player.inventory.get(itemIndex) == world.player.weapon || world.player.inventory.get(itemIndex) == world.player.shield)
                    addMessage("You cannot sell an equipped item!");
                else {
                    game.playSound(sound_trade_sell);
                    if (world.player.inventory.get(itemIndex).amount > 1)
                        world.player.inventory.get(itemIndex).amount--;
                    else world.player.inventory.remove(itemIndex);
                    world.player.gold += price;
                }
            }
        }

    }

    /**
     * Genera un efecto de transicion que va desde lo mas oscuro a lo mas luminoso.
     */
    private void drawSleepScreen() {
        counter++;
        if (counter < 120) {
            world.environment.lighting.filterAlpha += 0.01f;
            if (world.environment.lighting.filterAlpha > 1f) world.environment.lighting.filterAlpha = 1f;
        }
        if (counter >= 120) {
            world.environment.lighting.filterAlpha -= 0.01f;
            if (world.environment.lighting.filterAlpha <= 0f) {
                world.environment.lighting.filterAlpha = 0f;
                counter = 0;
                world.environment.lighting.dayState = world.environment.lighting.day;
                world.environment.lighting.dayCounter = 0;
                game.state = PLAY_STATE;
                world.player.ss.loadMovementFramesOfPlayer(player_movement2, 2);
                // world.player.frame.loadWeaponFrames(world.player.weapon.type == Type.SWORD ? player_sword : player_axe, ENTITY_WIDTH, ENTITY_HEIGHT);
            }
        }
    }

    private void drawSubwindow(int x, int y, int width, int height, int alpha) {
        // Fondo negro
        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRoundRect(x, y, width, height, 10, 10);
        // Borde blanco
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(3)); // Grosor del borde
        g2.drawRoundRect(x, y, width, height, 10, 10);
    }

    private void changeFontSize(float fontSize) {
        g2.setFont(g2.getFont().deriveFont(fontSize));
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0); // Creo que evita un IndexOutOfBoundsException
    }

    /**
     * Obtiene el indice del item en el slot.
     */
    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5);
    }

    /**
     * Obtiene x alineada hacia la derecha.
     *
     * @param text  el texto.
     * @param tailX la cola de x.
     */
    private int getXforAlignToRightText(String text, int tailX) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - textLength;
    }

    /**
     * Obtiene x para texo centrado.
     *
     * @param text el texto.
     */
    private int getXForCenteredText(String text) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return SCREEN_WIDTH / 2 - textLength / 2;
    }

    private int getYForCenteredText(String text) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getHeight();
        return SCREEN_HEIGHT / 2 + textLength / 2;
    }

}
