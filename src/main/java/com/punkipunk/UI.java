package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.FontAssets;
import com.punkipunk.core.Game;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.mob.Mob;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static com.punkipunk.utils.Global.*;

/**
 * User interface.
 *
 * <p>TODO Replace these UIs with textures made like MC and THROW THEM TO SHIT!
 * <p>TODO Implement musicVolume for title screen (<a href="https://www.youtube.com/watch?v=blyK-QkZkQ8">...</a>)
 */

public class UI {

    // TODO The console would have to be a separate class
    public final ArrayList<String> console = new ArrayList<>();
    private final Game game;
    private final World world;
    private final ArrayList<Integer> consoleCounter = new ArrayList<>();
    public Entity entity;
    public int subState, command; // TODO You could combine these two variables (mainWindowState and subState) referring to a single subState
    private GraphicsContext context;
    private String currentDialogue, combinedText = "";
    private int charIndex, counter;

    public UI(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void render(GraphicsContext context) {

        this.context = context;

        // context.setFontSmoothingType(FontSmoothingType.GRAY); // TODO Funciona?
        context.setFont(Assets.getFont(FontAssets.BLACK_PEARL));
        context.setFill(Color.WHITE);
        /* Determina como se posicionara el texto horizontalmente respecto al punto de origen. En este caso, indica que el texto
         * se alineara desde el borde izquierdo, que por cierto, es el valor por defecto. */
        // context.setTextAlign(TextAlignment.LEFT);
        /* Define la alineacion vertical del texto. En este caso, indica que el texto se alineara sobre la linea base de los
         * caracteres. La linea base es la linea imaginaria sobre la que "descansan" las letras, que por cierto, es el valor por
         * defecto.*/
        // context.setTextBaseline(VPos.BASELINE);

        switch (State.getState()) {
            case PLAY -> {
                renderManaBar(world.entities.player);
                renderHpBar(world.entities.player);
                renderBossHpBar();
                renderLvl();
            }
            case DIALOGUE -> renderDialogueWindow();
            case OPTION -> renderOptionWindow();
            case GAME_OVER -> renderGameOverWindow();
            case TELEPORT -> renderTeleportEffect();
            case SLEEP -> renderSleepEffect();
        }

        renderConsole();

    }

    public void renderHpBar(Entity entity) {
        int x = entity.getScreenX();
        int y = (int) (entity.getScreenY() + entity.sheet.frame.getHeight());
        int width = (int) entity.sheet.frame.getWidth();
        int height = 1;

        double oneScale = (double) width / entity.stats.maxHp;
        double hpBarValue = oneScale * entity.stats.hp;
        context.setFill(Color.BLACK);
        context.fillRect(x - 1, y - 1, width + 2, (entity instanceof Player) ? height + 1 : height + 2); // En caso de que la barra de vida sea del player, entonces se agrega 1 px al alto de esta, y sino, se agrega 2 px al alto de la barra del mob hostil (esto lo hago para que se vea bien el borde inferior)
        context.setFill(Color.RED);
        context.fillRect(x, y, (int) hpBarValue, height);

        entity.timer.timeHpBar(entity, INTERVAL_HP_BAR);
    }

    public void renderManaBar(Entity entity) {
        int x = entity.getScreenX();
        int y = (int) (entity.getScreenY() + entity.sheet.frame.getHeight());
        int width = (int) entity.sheet.frame.getWidth();
        int height = 1;

        double oneScale = (double) width / entity.stats.maxMana;
        double manaBarValue = oneScale * entity.stats.mana;

        y += height;

        context.setFill(Color.BLACK);
        context.fillRect(x - 1, y, width + 2, height + 2);
        context.setFill(Color.BLUE);
        context.fillRect(x, y + 1, (int) manaBarValue, height);

    }

    private void renderBossHpBar() {
        for (int i = 0; i < world.entities.mobs[1].length; i++) {
            Mob mob = world.entities.mobs[world.map.num][i];
            if (mob != null && mob.isOnCamera() && mob.flags.boss) {
                double oneScale = (double) tile * 8 / mob.stats.maxHp;
                double hpBarValue = oneScale * mob.stats.hp;

                int x = WINDOW_WIDTH / 2 - tile * 4;
                int y = (int) (tile * 10.5);

                if (hpBarValue < 0) hpBarValue = 0;

                context.setFill(Color.rgb(35, 35, 35));
                context.fillRect(x - 1, y - 1, tile * 8 + 2, 22);

                context.setFill(Color.rgb(255, 0, 30));
                context.fillRect(x, y, (int) hpBarValue, 20); // TODO Or 21?

                changeFontSize(24);
                context.setFill(Color.WHITE);
                context.fillText(mob.stats.name, x + 4, y - 10);
            }
        }
    }

    private void renderLvl() {
        int x = tile - 15, y = tile, gap = 20;
        context.setFontSmoothingType(FontSmoothingType.GRAY);
        changeFontSize(24);
        // context.setFont(Font.font(context.getFont().getFamily(), 24));
        context.setFill(Color.BLACK);
        context.fillText("Level: " + world.entities.player.stats.lvl, x + 2, y + 2);
        context.setFill(Color.WHITE);
        context.fillText("Level: " + world.entities.player.stats.lvl, x, y);
        y += gap;
        context.setFill(Color.BLACK);
        context.fillText("Exp: " + world.entities.player.stats.exp, x + 2, y + 2);
        context.setFill(Color.WHITE);
        context.fillText("Exp: " + world.entities.player.stats.exp, x, y);
        y += gap;
        context.setFill(Color.BLACK);
        context.fillText("Next Exp: " + world.entities.player.stats.nextExp, x + 2, y + 2);
        context.setFill(Color.WHITE);
        context.fillText("Next Exp: " + world.entities.player.stats.nextExp, x, y);
        y += gap;
        context.setFill(Color.BLACK);
        context.fillText("Gold: " + world.entities.player.stats.gold, x + 2, y + 2);
        context.setFill(Color.WHITE);
        context.fillText("Gold: " + world.entities.player.stats.gold, x, y);
    }

    public void renderDialogueWindow() {
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
            if (game.system.keyboard.isKeyPressed(Key.ENTER)) {
                charIndex = 0;
                combinedText = "";
                if (State.isState(State.DIALOGUE) || State.isState(State.CUTSCENE)) {
                    entity.dialogue.index++;
                    game.system.keyboard.releaseKey(Key.ENTER);
                }
            }

            if (game.system.keyboard.isKeyPressed(Key.ESCAPE)) { // TODO Or else if?
                charIndex = 0;
                combinedText = "";
                if (State.isState(State.TRADE)) {
                    State.setState(State.PLAY);
                    currentDialogue = "";
                    entity.dialogue.index++;
                    game.system.keyboard.releaseKey(Key.ESCAPE);
                }
            }

        } else { // If the dialogue ended
            entity.dialogue.index = 0;
            if (State.isState(State.DIALOGUE))
                State.setState(State.PLAY);
            if (State.isState(State.CUTSCENE)) world.cutscene.phase++;
        }

        for (String line : currentDialogue.split("\n")) {
            context.fillText(line, textX, textY);
            textY += 40;
        }

    }

    private void renderOptionWindow() {
        int width = tile * 10, height = tile * 10;
        int x = (WINDOW_WIDTH / 2 - width / 2), y = tile;
        renderSubwindow(x, y, width, height, SUBWINDOW_ALPHA);

        switch (subState) {
            case 1 -> renderOptionControlWindow(x, y, width, height);
            case 2 -> renderOptionEndGameConfirmationWindow(x, y);
        }

        game.system.keyboard.releaseKey(Key.ENTER);

    }

    private void renderOptionControlWindow(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX, textY, gap = 22;

        // Title
        changeFontSize(28);
        String text = "Controls";
        textX = getXForCenteredText(text);
        textY = frameY + tile;
        context.fillText(text, textX, textY);
        // Title

        changeFontSize(18);

        textX = frameX + tile;
        textY += gap + 10;
        context.fillText("Confirm/Attack", textX, textY);
        textY += gap;
        context.fillText("Options/Back", textX, textY);
        textY += gap;
        context.fillText("Move", textX, textY);
        textY += gap;
        context.fillText("Shoot", textX, textY);
        textY += gap;
        context.fillText("Pickup Item", textX, textY);
        textY += gap;
        context.fillText("Minimap", textX, textY);
        textY += gap;
        context.fillText("Inventory", textX, textY);
        textY += gap;
        context.fillText("Stats Window", textX, textY);
        textY += gap;
        context.fillText("Debug", textX, textY);
        textY += gap;
        context.fillText("Test Mode", textX, textY);
        textY += gap;
        context.fillText("Hitbox", textX, textY);

        textX = frameX + frameWidth - tile * 3;
        textY = frameY + tile + gap + 10;
        context.fillText("Enter", textX, textY);
        textY += gap;
        context.fillText("ESC", textX, textY);
        textY += gap;
        context.fillText("WASD", textX, textY);
        textY += gap;
        context.fillText("F", textX, textY);
        textY += gap;
        context.fillText("P", textX, textY);
        textY += gap;
        context.fillText("M", textX, textY);
        textY += gap;
        context.fillText("I", textX, textY);
        textY += gap;
        context.fillText("C", textX, textY);
        textY += gap;
        context.fillText("Q", textX, textY);
        textY += gap;
        context.fillText("T", textX, textY);
        textY += gap;
        context.fillText("H", textX, textY);

        // Back
        textX = frameX + tile;
        textY = frameHeight + 25;
        context.fillText("Back", textX, textY);
        if (command == 0) {
            context.fillText(">", textX - 25, textY);
            if (game.system.keyboard.isKeyPressed(Key.ENTER)) {
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
            context.fillText(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += (int) (tile * 1.9);
        context.fillText(text, textX, textY);
        if (command == 0) {
            context.fillText(">", textX - 25, textY);
            if (game.system.keyboard.isKeyPressed(Key.ENTER)) {
                subState = 0;
                State.setState(State.MAIN);
                game.reset(true);
            }
        }

        text = "No";
        textX = getXForCenteredText(text);
        textY += tile;
        context.fillText(text, textX, textY);
        if (command == 1) {
            context.fillText(">", textX - 25, textY);
            if (game.system.keyboard.isKeyPressed(Key.ENTER)) {
                subState = 0;
                command = 4;
            }
        }

    }

    private void renderGameOverWindow() {
        int x, y;
        String text;
        context.setFill(new Color(0, 0, 0, /* counter * 150 */ 0.5));
        context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        changeFontSize(82);

        text = "Game Over";
        // Shade
        context.setFill(Color.BLACK);
        x = getXForCenteredText(text);
        y = tile * 4;
        context.fillText(text, x, y);
        // Main
        context.setFill(Color.WHITE);
        context.fillText(text, x - 4, y - 4);
        // Retry
        changeFontSize(50);
        text = "Retry";
        x = getXForCenteredText(text);
        y += tile * 4;
        context.fillText(text, x, y);
        if (command == 0) context.fillText(">", x - 40, y);
        // Quit
        text = "Quit";
        x = getXForCenteredText(text);
        y += 55;
        context.fillText(text, x, y);
        if (command == 1) context.fillText(">", x - 40, y);

    }

    /**
     * Renders a teleport effect and when it finishes, teleports the player.
     */
    private void renderTeleportEffect() {
        context.setFill(new Color(0, 0, 0, /* counter * 5 */ 0.5));
        context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        counter++;
        if (counter >= INTERVAL_TELEPORT) {
            counter = 0;
            State.setState(State.PLAY);
            world.map.num = game.system.event.mapNum;
            world.entities.player.pos.x = (int) ((game.system.event.col * tile) + world.entities.player.hitbox.getWidth() / 2);
            world.entities.player.pos.y = (int) ((game.system.event.row * tile) - world.entities.player.hitbox.getHeight());
            game.system.event.previousEventX = world.entities.player.pos.x;
            game.system.event.previousEventY = world.entities.player.pos.y;
            world.map.changeArea();
        }
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
                world.entities.player.currentFrame = world.entities.player.sheet.down[0];
                State.setState(State.PLAY);
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
        context.setFill(new Color(0, 0, 0, /* alpha */ 0.5));
        context.fillRoundRect(x, y, width, height, 10, 10);
        // White background
        context.setFill(Color.rgb(255, 255, 255));
        context.setLineWidth(3); // Edge thickness
        context.strokeRoundRect(x, y, width, height, 20, 20);
    }

    private void renderConsole() {
        int x = tile, y = tile * 5 - 10, gap = 50;
        changeFontSize(18);

        for (int i = 0; i < console.size(); i++) {
            if (console.get(i) != null) {
                // Shade
                context.setFill(Color.BLACK);
                context.fillText(console.get(i), x + 2, y + 2);
                // Main color
                context.setFill(Color.WHITE);
                context.fillText(console.get(i), x, y);
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

    private void changeFontSize(float size) {
        context.setFont(Font.font(context.getFont().getFamily(), size));
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
        Text theText = new Text(text);
        theText.setFont(context.getFont());
        double textWidth = theText.getBoundsInLocal().getWidth();
        // Additional value so that the text (for example, amount of gold) is not so close to the right edge of the window
        int distanceToTheRightBorder = 2;
        return (int) (tailX - textWidth - distanceToTheRightBorder);
    }

    /**
     * Get x for centered text.
     *
     * @param text the text.
     */
    private int getXForCenteredText(String text) {
        Text theText = new Text(text);
        theText.setFont(context.getFont());
        /* Para una mayor precision se puede usar getLayoutBounds() en lugar de getBoundsInLocal(). La diferencia es sutil, pero
         * getLayoutBounds() puede ser mas preciso en algunos casos, especialmente con fuentes complejas. */
        double textWidth = theText.getBoundsInLocal().getWidth();
        return (int) (WINDOW_WIDTH / 2 - textWidth / 2);
    }

    private int getYForCenteredText(String text) {
        Text theText = new Text(text);
        theText.setFont(context.getFont());
        double textHeight = theText.getBoundsInLocal().getHeight();
        /* La diferencia clave es el uso de getBaselineOffset(). Este metodo nos da la distancia desde la parte superior del texto
         * hasta su linea base, lo cual es crucial para un centrado vertical preciso. */
        double baselineOffset = theText.getBaselineOffset();
        // Encuentra el centro de la ventana, luego ajusta hacia abajo por la mitad de la altura del texto y el baselineOffset
        return (int) (WINDOW_HEIGHT / 2 + textHeight / 2 + baselineOffset);
    }

}
