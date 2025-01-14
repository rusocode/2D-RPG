package com.punkipunk;

import com.punkipunk.core.Game;
import com.punkipunk.input.keyboard.Key;
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

    private final Game game;
    private final World world;
    private Image[] minimap;

    public Minimap(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void create() {
        Canvas[] minimapCanvases = new Canvas[MAPS];
        minimap = new Image[MAPS];
        int width = tile * MAX_MAP_COL;
        int height = tile * MAX_MAP_ROW;
        for (int map = 0; map < MAPS; map++) {
            minimapCanvases[map] = new Canvas(width, height);
            GraphicsContext gc = minimapCanvases[map].getGraphicsContext2D();
            for (int row = 0; row < MAX_MAP_ROW; row++) {
                for (int col = 0; col < MAX_MAP_COL; col++) {
                    int tileIndex = world.map.tileIndex[map][row][col];
                    int x = tile * col;
                    int y = tile * row;
                    gc.drawImage(world.map.tileData[tileIndex].texture, x, y);
                }
            }
            // Inmediatamente despues de dibujar, convertimos cada Canvas a una Image usando el metodo snapshot()
            minimap[map] = minimapCanvases[map].snapshot(null, null); // Convert Canvas to Image
        }
    }

    public void render(final GraphicsContext gc) {
        if (game.system.keyboard.isKeyToggled(Key.MINIMAP)) {
            int width = 100;
            int height = 100;
            int x = WINDOW_WIDTH - width - 22;
            int y = 15;
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0);
            gc.strokeRect(x - 1, y - 1, width + 1, height + 1);
            gc.drawImage(minimap[world.map.num], x, y, width, height);

            // Draw a red square that represents the player's position
            double scale = (double) (tile * MAX_MAP_COL) / width;
            int playerX = (int) (x + (world.entities.player.position.x + world.entities.player.hitbox.getX()) / scale);
            int playerY = (int) (y + (world.entities.player.position.y + world.entities.player.hitbox.getY()) / scale);
            int playerSize = (int) (tile / scale);
            gc.setFill(Color.RED);
            gc.fillRect(playerX, playerY, playerSize, playerSize);
        }
    }

}
