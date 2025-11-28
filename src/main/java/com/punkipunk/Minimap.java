package com.punkipunk;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * Algunas consideraciones adicionales usando JavaFX para el Minimap:
 * <ul>
 * <li>Este enfoque crea imagenes inmutables de los minimapas. Si necesitas actualizar los minimapas dinamicamente durante el
 * juego, podrias necesitar un enfoque diferente o recrear las imagenes cuando sea necesario.
 * <li>Si los minimapas son grandes y tienes muchos de ellos, este enfoque podria consumir mas memoria que el original con Canvas.
 * En ese caso, podrias considerar mantener los Canvas y convertirlos a Image solo cuando sea necesario renderizarlos.
 * </ul>
 */

public class Minimap {

    private final IGame game;
    private final World world;
    private Image[] minimap;

    public Minimap(IGame game, World world) {
        this.game = game;
        this.world = world;
    }

    public void create() {
        Canvas[] minimapCanvases = new Canvas[MapID.values().length];
        minimap = new Image[MapID.values().length];
        int width = tile * MAX_MAP_COL;
        int height = tile * MAX_MAP_ROW;
        for (int map = 0; map < MapID.values().length; map++) {
            minimapCanvases[map] = new Canvas(width, height);
            GraphicsContext gc = minimapCanvases[map].getGraphicsContext2D();
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    int tileIndex = world.map.tileIndex[map][row][col];
                    int x = tile * col;
                    int y = tile * row;
                    gc.drawImage(world.map.tiles.get(tileIndex).texture, x, y);
                }
            }
            // Convierte cada canvas a una imagen con snapshot() despues de dibujar
            minimap[map] = minimapCanvases[map].snapshot(null, null);
        }
    }

    public void render(final GraphicsContext context) {
        if (game.getGameSystem().keyboard.isKeyToggled(Key.MINIMAP)) {
            int width = 100;
            int height = 100;
            int x = WINDOW_WIDTH - width - 22;
            int y = 15;
            context.setStroke(Color.BLACK);
            context.setLineWidth(0);
            context.strokeRect(x - 1, y - 1, width + 1, height + 1);
            context.drawImage(minimap[world.map.id.ordinal()], x, y, width, height);

            // Dibuja un cuadrado rojo que represente la posicion del player
            double scale = (double) (tile * MAX_MAP_COL) / width;
            int playerX = (int) (x + (world.entitySystem.player.position.x + world.entitySystem.player.hitbox.getX()) / scale);
            int playerY = (int) (y + (world.entitySystem.player.position.y + world.entitySystem.player.hitbox.getY()) / scale);
            int playerSize = (int) (tile / scale);
            context.setFill(Color.RED);
            context.fillRect(playerX, playerY, playerSize, playerSize);
        }
    }

}
