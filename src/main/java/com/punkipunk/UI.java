package com.punkipunk;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.player.Player;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private final IGame game;
    private final World world;
    private final ArrayList<Integer> consoleCounter = new ArrayList<>();
    public Entity entity;
    public int subState, command; // TODO You could combine these two variables (mainWindowState and subState) referring to a single subState
    private GraphicsContext context;
    private String currentDialogue, combinedText = "";
    private int charIndex, counter;

    public UI(IGame game, World world) {
        this.game = game;
        this.world = world;
    }

    public void render(GraphicsContext context) {

        this.context = context;

        switch (State.getState()) {
            case PLAY -> {
                renderManaBar(world.entitySystem.player);
                renderHpBar(world.entitySystem.player);
                renderBossHpBar();
            }
            case DIALOGUE -> renderDialogueWindow();
            case GAME_OVER -> renderGameOverWindow();
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
        world.entitySystem.getMobs(world.map.id).stream()
                .filter(mob -> mob.isOnCamera() && mob.flags.boss)
                .findFirst()
                .ifPresent(boss -> {
                    double oneScale = (double) tile * 8 / boss.stats.maxHp;
                    double hpBarValue = oneScale * boss.stats.hp;

                    int x = WINDOW_WIDTH / 2 - tile * 4;
                    int y = (int) (tile * 10.5);

                    if (hpBarValue < 0) hpBarValue = 0;

                    context.setFill(Color.rgb(35, 35, 35));
                    context.fillRect(x - 1, y - 1, tile * 8 + 2, 22);

                    context.setFill(Color.rgb(255, 0, 30));
                    context.fillRect(x, y, (int) hpBarValue, 20); // TODO Or 21?

                    changeFontSize(24);
                    context.setFill(Color.WHITE);
                    context.fillText(boss.stats.name, x + 4, y - 10);
                });
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
            if (game.getGameSystem().keyboard.isKeyPressed(Key.ENTER)) {
                charIndex = 0;
                combinedText = "";
                if (State.isState(State.DIALOGUE) || State.isState(State.CUTSCENE)) {
                    entity.dialogue.index++;
                    game.getGameSystem().keyboard.releaseKey(Key.ENTER);
                }
            }

            if (game.getGameSystem().keyboard.isKeyPressed(Key.ESCAPE)) { // TODO Or else if?
                charIndex = 0;
                combinedText = "";
                if (State.isState(State.TRADE)) {
                    State.setState(State.PLAY);
                    currentDialogue = "";
                    entity.dialogue.index++;
                    game.getGameSystem().keyboard.releaseKey(Key.ESCAPE);
                }
            }

        } else { // If the dialogue ended
            entity.dialogue.index = 0;
            if (State.isState(State.DIALOGUE))
                State.setState(State.PLAY);
            if (State.isState(State.CUTSCENE)) world.cutsceneSystem.phase++;
        }

        for (String line : currentDialogue.split("\n")) {
            context.fillText(line, textX, textY);
            textY += 40;
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
     * Renders a dream effect that goes from darkest to brightest.
     */
    private void renderSleepEffect() {
        // If the counter is less than 120, darken the render by 0.01 until it is completely dark
        if (counter < 120) {
            counter++;
            world.environmentSystem.lighting.filterAlpha += 0.01f;
            // If the value exceeds the maximum darkness, set it to 1
            if (world.environmentSystem.lighting.filterAlpha > 1f) world.environmentSystem.lighting.filterAlpha = 1f;
        }
        // When you reach 120, brighten the render by 0.01 until it is completely bright
        if (counter >= 120) {
            world.environmentSystem.lighting.filterAlpha -= 0.01f;
            if (world.environmentSystem.lighting.filterAlpha <= 0f) {
                world.environmentSystem.lighting.filterAlpha = 0f;
                world.environmentSystem.lighting.dayState = world.environmentSystem.lighting.day;
                world.environmentSystem.lighting.dayCounter = 0;
                world.entitySystem.player.currentFrame = world.entitySystem.player.sheet.down[0];
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

}
