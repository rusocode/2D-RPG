package com.craivet.world.management;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.PlayerDummy;
import com.craivet.world.entity.item.DoorIron;
import com.craivet.world.entity.mob.Skeleton;

import static com.craivet.utils.Global.*;

public class CutsceneManager {

    private final Game game;
    private final World world;
    public int n; // Scene number
    public int phase; // Scene phase

    // Scenes types
    public final int na = 0;
    public final int boss = 1;

    public CutsceneManager(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void render() {
        // TODO switch for multiple scenes
        if (n == boss) sceneSkeleton();
    }

    private void sceneSkeleton() {

        // Coloca la puerta de hierro
        if (phase == 0) {
            // Busca un espacio sobrante en el array de items para agregar la puerta de hierro
            for (int i = 0; i < world.items[1].length; i++) {
                if (world.items[world.map][i] == null) {
                    world.items[world.map][i] = new DoorIron(game, world, 25, 28);
                    world.items[world.map][i].temp = true;
                    break;
                }
            }

            // Search a vacant slot for the dummy
            for (int i = 0; i < world.mobs[1].length; i++) {
                if (world.mobs[world.map][i] == null) {
                    world.mobs[world.map][i] = new PlayerDummy(game, world);
                    world.mobs[world.map][i].pos.x = world.player.pos.x;
                    world.mobs[world.map][i].pos.y = world.player.pos.y;
                    world.mobs[world.map][i].direction = world.player.direction; // TODO No funciona!
                    break;
                }
            }

            world.player.drawing = false;
            phase++;
        }

        // Mueve la camara hacia el boss
        if (phase == 1) {
            world.player.pos.y -= 2;
            if (world.player.pos.y < tile * 16) phase++;
        }

        // Despierta al boss
        if (phase == 2) {
            for (int i = 0; i < world.mobs[1].length; i++) {
                if (world.mobs[world.map][i] != null && world.mobs[world.map][i].stats.name.equals(Skeleton.NAME)) {
                    world.mobs[world.map][i].sleep = false; // Ahora el boss se despierta
                    game.ui.entity = world.mobs[world.map][i]; // Le pasa el boss a la ui para que pueda renderizar la ventana de dialogo
                    phase++;
                    break;
                }
            }
        }

        // Habla el boss
        if (phase == 3) game.ui.renderDialogueWindow();

        // Devuelve la camara al player
        if (phase == 4) {
            // Busca al personaje ficticio (PlayerDummy)
            for (int i = 0; i < world.mobs[1].length; i++) {
                if (world.mobs[world.map][i] != null && world.mobs[world.map][i].stats.name.equals(PlayerDummy.NAME)) {
                    // Restaura la posicion del player
                    world.player.pos.x = world.mobs[world.map][i].pos.x;
                    world.player.pos.y = world.mobs[world.map][i].pos.y;
                    // Borra al personaje ficticio
                    world.mobs[world.map][i] = null;
                    break;
                }
            }

            // Ahora se puede dibujar al player
            world.player.drawing = true;

            // Reinicia la escena
            n = na;
            phase = 0;
            game.state = PLAY_STATE;
        }

    }

}
