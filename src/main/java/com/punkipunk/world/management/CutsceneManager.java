package com.punkipunk.world.management;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class CutsceneManager {

    // Tipos de escena
    public final int na = 0, boss = 1;
    private final Game game;
    private final World world;
    public int n; // Numero de escena
    public int phase; // Fase de escena

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

            world.entitySystem.createItem(ItemType.IRON_DOOR, world.map.num, 25, 28);

            world.entitySystem.createMob(MobType.PLAYER_DUMMY, world.map.num,
                    world.entitySystem.player.position.x,
                    world.entitySystem.player.position.y
            ).direction = world.entitySystem.player.direction;

            world.entitySystem.player.drawing = false;
            phase++;
        }

        // Mueve la camara hacia el boss
        if (phase == 1) {
            world.entitySystem.player.position.y -= 2;
            if (world.entitySystem.player.position.y < tile * 16) phase++;
        }

        // Despierta al boss
        if (phase == 2) {
            world.entitySystem.getMobs(world.map.num).stream()
                    .filter(mob -> mob.getType() == MobType.LIZARD)
                    .findFirst()
                    .ifPresent(lizard -> {
                        lizard.sleep = false;
                        game.gameSystem.ui.entity = lizard; // Pasa el boss a la interfaz de usuario para que pueda representar la ventana de dialogo
                        phase++;
                    });
        }

        // El boss habla
        if (phase == 3) game.gameSystem.ui.renderDialogueWindow();

        // Devuelve la camara al player
        if (phase == 4) {
            // Encuentra el personaje ficticio
            world.entitySystem.getMobs(world.map.num).stream()
                    .filter(mob -> mob.getType() == MobType.PLAYER_DUMMY)
                    .findFirst()
                    .ifPresent(dummy -> {
                        // Restaura la posicion del player
                        world.entitySystem.player.position.x = dummy.position.x;
                        world.entitySystem.player.position.y = dummy.position.y;
                        // Elimina al personaje ficticio
                        world.entitySystem.removeMob(world.map.num, dummy);
                    });

            // Ahora puede dibujar al player
            world.entitySystem.player.drawing = true;

            // Reinicia la escena
            n = na;
            phase = 0;
            State.setState(State.PLAY);
        }

    }

}
