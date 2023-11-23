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
            for (int i = 0; i < world.entities.items[1].length; i++) {
                if (world.entities.items[world.map.num][i] == null) {
                    world.entities.items[world.map.num][i] = new DoorIron(game, world, 25, 28);
                    world.entities.items[world.map.num][i].temp = true;
                    break;
                }
            }

            // Search a vacant slot for the dummy
            for (int i = 0; i < world.entities.mobs[1].length; i++) {
                if (world.entities.mobs[world.map.num][i] == null) {
                    world.entities.mobs[world.map.num][i] = new PlayerDummy(game, world);
                    world.entities.mobs[world.map.num][i].direction = world.entities.player.direction;
                    world.entities.mobs[world.map.num][i].pos.x = world.entities.player.pos.x;
                    world.entities.mobs[world.map.num][i].pos.y = world.entities.player.pos.y;
                    break;
                }
            }

            world.entities.player.drawing = false;
            phase++;
        }

        // Mueve la camara hacia el boss
        if (phase == 1) {
            world.entities.player.pos.y -= 2;
            if (world.entities.player.pos.y < tile * 16) phase++;
        }

        // Despierta al boss
        if (phase == 2) {
            for (int i = 0; i < world.entities.mobs[1].length; i++) {
                if (world.entities.mobs[world.map.num][i] != null && world.entities.mobs[world.map.num][i].stats.name.equals(Skeleton.NAME)) {
                    world.entities.mobs[world.map.num][i].sleep = false; // Ahora el boss se despierta
                    game.ui.entity = world.entities.mobs[world.map.num][i]; // Le pasa el boss a la ui para que pueda renderizar la ventana de dialogo
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
            for (int i = 0; i < world.entities.mobs[1].length; i++) {
                if (world.entities.mobs[world.map.num][i] != null && world.entities.mobs[world.map.num][i].stats.name.equals(PlayerDummy.NAME)) {
                    // Restaura la posicion del player
                    world.entities.player.pos.x = world.entities.mobs[world.map.num][i].pos.x;
                    world.entities.player.pos.y = world.entities.mobs[world.map.num][i].pos.y;
                    // Borra al personaje ficticio
                    world.entities.mobs[world.map.num][i] = null;
                    break;
                }
            }

            // Ahora se puede dibujar al player
            world.entities.player.drawing = true;

            // Reinicia la escena
            n = na;
            phase = 0;
            game.state = PLAY_STATE;
        }

    }

}
