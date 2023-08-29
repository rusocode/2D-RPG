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
 * <p>TODO Reemplazar estas UI por GUIS echas como MC y VOLARLAS A LA MIERDA!!
 * <p>TODO Implementar musica para pantalla de titulo (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

    private final Game game;
    private final World world;
    private Graphics2D g2;
    public Entity entity;

    private String currentDialogue, combinedText;
    private int charIndex, counter;

    public final ArrayList<String> console = new ArrayList<>();
    private final ArrayList<Integer> consoleCounter = new ArrayList<>();

    public int playerSlotCol, playerSlotRow, npcSlotCol, npcSlotRow;
    public int mainWindowState, subState, command; // TODO Se podrian combinar estas dos variables (mainWindowState y subState) haciendo referencia a un solo subState

    // Icons
    private BufferedImage heartFull, heartHalf, heartBlank, manaFull, manaBlank;

    public UI(Game game, World world) {
        this.game = game;
        this.world = world;
        initHUD();
    }

    private void initHUD() {
        BufferedImage[] subimages = SpriteSheet.getIconsSubimages(icons, 16, 16);
        heartFull = Utils.scaleImage(subimages[0], tile, tile);
        heartHalf = Utils.scaleImage(subimages[1], tile, tile);
        heartBlank = Utils.scaleImage(subimages[2], tile, tile);
        manaFull = Utils.scaleImage(subimages[3], tile, tile);
        manaBlank = Utils.scaleImage(subimages[4], tile, tile);
    }

    public void render(Graphics2D g2) {

        this.g2 = g2;

        // Fuente y color por defecto
        g2.setFont(font_minecraft);
        g2.setColor(Color.white);
        // g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // Suaviza los bordes de la fuente

        // Renderiza las ventanas dependiendo del estado del juego
        switch (game.state) {
            case MAIN_STATE -> renderMainWindow();
            case PLAY_STATE -> renderStats();
            case DIALOGUE_STATE -> renderDialogueWindow();
            case STATS_STATE -> renderStatsWindow();
            case INVENTORY_STATE -> renderInventoryWindow(world.player, true);
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
                // Sombra
                g2.setColor(Color.black);
                g2.drawString(console.get(i), x + 2, y + 2);
                // Color principal
                g2.setColor(Color.white);
                g2.drawString(console.get(i), x, y);
                // Actua como consoleCounter++
                consoleCounter.set(i, consoleCounter.get(i) + 1);

                y += gap;

                // Despues de 3 segundos, elimina los mensajes en consola
                if (consoleCounter.get(i) > 180) {
                    console.remove(i);
                    consoleCounter.remove(i);
                }
            }
        }
    }

    private void renderStats() {
        // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
        int x = tile / 2, y = tile / 2, i = 0;

        // Dibuja los corazones vacios
        while (i < world.player.maxHp / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += tile;
        }

        // Resetea la posicion de x para dibujar los otros corazones (medio y lleno)
        x = tile / 2;
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
            x += tile;
        }

        // Dibuja la mana vacia
        x = (tile / 2) - 4;
        y = (int) (tile * 1.5);
        i = 0;
        while (i < world.player.maxMana) {
            g2.drawImage(manaBlank, x, y, null);
            i++;
            x += (tile / 2) + 1;
        }

        // Dibuja la mana actual
        x = (tile / 2) - 4;
        i = 0;
        while (i < world.player.mana) {
            g2.drawImage(manaFull, x, y, null);
            i++;
            x += (tile / 2) + 1;
        }

    }

    private void renderDialogueWindow() {
        int x = tile * 3, y = tile / 2;
        int width = WINDOW_WIDTH - (tile * 6), height = tile * 4;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        changeFontSize(14);

        // Text
        int textX = x + tile;
        int textY = y + tile;

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

    private void renderStatsWindow() {
        int width = tile * 9, height = tile * 11;
        int x = (WINDOW_WIDTH / 2 - width / 2), y = tile - 15;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        int gap = 24;
        changeFontSize(16);

        // Nombres
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

        // Valores
        int tailX = (x + width) - 30; // Cola de la posicion x
        // Resetea textY
        textY = y + tile;
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

        g2.drawImage(world.player.weapon.image, tailX - 35, textY - 5, null);
        textY += gap;

        g2.drawImage(world.player.shield.image, tailX - 35, textY + 5, null);

    }

    private void renderInventoryWindow(Entity entity, boolean cursor) {
        int x, y, width, height;
        int slotCol, slotRow;

        width = (int) (tile * 6.5);
        height = tile * 5;
        // Dependiendo del tipo de entidad, renderiza el inventario del lado derecho (player) o izquierdo (npc)
        if (entity == world.player) {
            x = (int) ((WINDOW_WIDTH / 2 - width / 2) + tile * 4.5);
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            x = (int) ((WINDOW_WIDTH / 2 - width / 2) - tile * 4.5);
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }
        y = tile - 15;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        // Slots
        final int slotXStart = x + 20, slotYStart = y + 20;
        int slotX = slotXStart, slotY = slotYStart;
        int gap = tile + 3;
        changeFontSize(14);

        for (int i = 0; i < entity.inventory.size(); i++) {

            // Marca de color amarillo el item seleccionado
            if (entity.inventory.get(i) == entity.weapon || entity.inventory.get(i) == entity.shield || entity.inventory.get(i) == entity.light) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRect(slotX, slotY, tile, tile);
            }

            // Dibuja el item
            g2.drawImage(entity.inventory.get(i).image, slotX, slotY, null);

            // Dibuja la cantidad del item si este tiene mas de 1
            if (entity.inventory.get(i).amount > 1) {
                int amountX, amountY;
                String amount = String.valueOf(entity.inventory.get(i).amount);
                amountX = getXforAlignToRightText(amount, slotX + gap);
                amountY = slotY + tile - 2;
                g2.setColor(Color.white);
                g2.drawString(amount, amountX, amountY);
            }

            slotX += gap;

            // Salta a la siguiente fila
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += gap;
            }

        }

        // Cursor
        if (cursor) {
            int cursorX = slotXStart + (gap * slotCol);
            int cursorY = slotYStart + (gap * slotRow);

            // Dibuja el cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(cursorX, cursorY, tile, tile);

            // Ventana de descripcion
            int dFrameY = y + height;
            int dFrameHeight = tile * 4;

            // Dibuja la descripcion
            int textX = x + 20;
            int textY = dFrameY + tile + 5;
            int itemIndex = getItemIndexOnInventory(slotCol, slotRow);
            if (itemIndex < entity.inventory.size()) {
                renderSubwindow(x, dFrameY, width, dFrameHeight, SUBWINDOW_ALPHA);
                changeFontSize(14);
                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
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
        g2.drawString("Pause", textX, textY);
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
        // Sombra
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = tile * 4;
        g2.drawString(text, x, y);
        // Principal
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
     * Renderiza un efecto de transicion y cuando este termina, teletransporta al player.
     */
    private void renderTransitionEffect() {
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        counter++;
        if (counter >= INTERVAL_TRANSITION) {
            counter = 0;
            game.state = PLAY_STATE;
            world.map = game.event.map;
            world.player.x = (game.event.col * tile) + world.player.hitbox.width / 2;
            world.player.y = (game.event.row * tile) - world.player.hitbox.height;
            game.event.previousEventX = world.player.x;
            game.event.previousEventY = world.player.y;
            world.changeArea();
        }
    }

    private void renderTradeWindow() {
        switch (subState) {
            case 0 -> renderTradeMainWindow();
            case 1 -> renderTradeBuyWindow();
            case 2 -> renderTradeSellWindow();
        }
        game.keyboard.enter = false; // Reinicia la entrada de teclado
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

        renderInventoryWindow(entity, true);
        renderInventoryWindow(world.player, false); // TODO El player forma parte de una entidad, creo que se podria especificar el argumento del metodo de otra forma

        // Ventana del precio
        int itemIndex = getItemIndexOnInventory(npcSlotCol, npcSlotRow);
        if (itemIndex < entity.inventory.size()) {
            x = tile * 5;
            y = tile * 5;
            width = (int) (tile * 2.5);
            height = tile;
            renderSubwindow(x, y, width, height, 255);
            // Imagen del oro
            g2.drawImage(gold, x + 6, y + 10, null);
            // Precio de compra del item
            int price = entity.inventory.get(itemIndex).price;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, x + width);
            g2.drawString(text, x, y + 24);

            // Compra un item
            if (game.keyboard.enter) {
                if (entity.inventory.get(itemIndex).price > world.player.gold)
                    addMessageToConsole("You need more gold to buy that!");
                else {
                    // TODO Especificar la cantidad de items a comprar
                    if (world.player.canPickup(entity.inventory.get(itemIndex))) {
                        game.playSound(sound_trade_buy);
                        world.player.gold -= entity.inventory.get(itemIndex).price;
                    } else addMessageToConsole("You cannot carry any more!");
                }
            }

        }

        // Ventana del oro disponible
        x = (int) (tile * 9.75);
        y = (int) (tile * 5.68);
        width = (int) (tile * 6.5);
        height = tile * 2;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Gold: " + world.player.gold, x + 24, y + 40);

    }

    private void renderTradeSellWindow() {
        int x, y, width, height;

        renderInventoryWindow(world.player, true);

        // Ventana del precio
        int itemIndex = getItemIndexOnInventory(playerSlotCol, playerSlotRow);
        if (itemIndex < world.player.inventory.size()) {
            x = tile * 14;
            y = tile * 5;
            width = (int) (tile * 2.5);
            height = tile;
            renderSubwindow(x, y, width, height, 255);
            // Imagen del oro
            g2.drawImage(gold, x + 6, y + 10, null);
            // Precio de venta del item
            int price = world.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);
            x = getXforAlignToRightText(text, x + width);
            g2.drawString(text, x, y + 24);

            // Vende un item
            if (game.keyboard.enter) {
                if (world.player.inventory.get(itemIndex) == world.player.weapon || world.player.inventory.get(itemIndex) == world.player.shield)
                    addMessageToConsole("You cannot sell an equipped item!");
                else {
                    game.playSound(sound_trade_sell);
                    if (world.player.inventory.get(itemIndex).amount > 1)
                        world.player.inventory.get(itemIndex).amount--;
                    else world.player.inventory.remove(itemIndex);
                    world.player.gold += price;
                }
            }

        }

        // Ventana del oro disponible
        x = (int) (tile * 9.75);
        y = (int) (tile * 9.68);
        width = (int) (tile * 6.5);
        height = tile * 2;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);
        g2.drawString("Gold: " + world.player.gold, x + 24, y + 40);

    }

    /**
     * Renderiza un efecto de sueño que va desde lo mas oscuro a lo mas luminoso.
     */
    private void renderSleepEffect() {
        // Si el contador es menor a 120, oscurece el render en 0.01 hasta que quede completamente oscuro
        if (counter < 120) {
            counter++;
            world.environment.lighting.filterAlpha += 0.01F;
            // En caso de que el valor supere el maximo de oscuridad, lo establece en 1
            if (world.environment.lighting.filterAlpha > 1F) world.environment.lighting.filterAlpha = 1F;
        }
        // Al llegar a 120, ilumina el render en 0.01 hasta que quede completamente luminoso
        if (counter >= 120) {
            world.environment.lighting.filterAlpha -= 0.01F;
            if (world.environment.lighting.filterAlpha <= 0F) {
                world.environment.lighting.filterAlpha = 0F;
                world.environment.lighting.dayState = world.environment.lighting.day;
                world.environment.lighting.dayCounter = 0;
                world.player.currentFrame = world.player.ss.down[0];
                game.state = PLAY_STATE;
                counter = 0; // Resetea el contador para volver a generar el efecto desde 0
            }
        }
    }

    /**
     * Dibuja una sub ventana.
     *
     * @param x      posicion x de la ventana.
     * @param y      posicion y de la ventana.
     * @param width  ancho de la ventana.
     * @param height alto de la ventana.
     * @param alpha  transparencia de la ventana.
     */
    private void renderSubwindow(int x, int y, int width, int height, int alpha) {
        // Fondo negro
        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRoundRect(x, y, width, height, 10, 10);
        // Borde blanco
        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(3)); // Grosor del borde
        g2.drawRoundRect(x, y, width, height, 10, 10);
    }

    private void changeFontSize(float size) {
        g2.setFont(g2.getFont().deriveFont(size));
    }

    public void addMessageToConsole(String msg) {
        console.add(msg);
        consoleCounter.add(0); // Creo que evita un IndexOutOfBoundsException
    }

    /**
     * Obtiene el indice del item en el inventario.
     *
     * @param slotCol columna de ranura.
     * @param slotRow fila de ranura.
     * @return el indice del item en el inventario.
     */
    public int getItemIndexOnInventory(int slotCol, int slotRow) {
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
        // Valor adicional para que el texto (por ejemplo, cantidad de oro) no quede tan pegado al borde derecho de la ventana
        int distanceToTheRightBorder = 2;
        return tailX - textLength - distanceToTheRightBorder;
    }

    /**
     * Obtiene x para texo centrado.
     *
     * @param text el texto.
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
