package com.punkipunk.world.management;

import com.punkipunk.core.Game;
import com.punkipunk.entity.item.IronDoor;
import com.punkipunk.entity.mob.Lizard;
import com.punkipunk.entity.player.PlayerDummy;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

public class CutsceneManager {

    // Scenes types
    public final int na = 0, boss = 1;
    private final Game game;
    private final World world;
    public int n; // Scene number
    public int phase; // Scene phase

    public CutsceneManager(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public void render() {
        // TODO switch for multiple scenes
        if (n == boss) sceneSkeleton();
    }

    private void sceneSkeleton() {

        // Place the iron door
        if (phase == 0) {
            // Look for a free space in the item array to add the iron door
            for (int i = 0; i < world.entities.items[1].length; i++) {
                if (world.entities.items[world.map.num][i] == null) {
                    world.entities.items[world.map.num][i] = new IronDoor(game, world, 25, 28);
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

        // Move the camera towards the boss
        if (phase == 1) {
            world.entities.player.pos.y -= 2;
            if (world.entities.player.pos.y < tile * 16) phase++;
        }

        // Wake up the boss
        if (phase == 2) {
            for (int i = 0; i < world.entities.mobs[1].length; i++) {
                if (world.entities.mobs[world.map.num][i] != null && world.entities.mobs[world.map.num][i].stats.name.equals(Lizard.NAME)) {
                    world.entities.mobs[world.map.num][i].sleep = false; // Now the boss wakes up
                    game.system.ui.entity = world.entities.mobs[world.map.num][i]; // Passes the boss to the UI so it can render the dialog window
                    phase++;
                    break;
                }
            }
        }

        // The boss speaks
        if (phase == 3) game.system.ui.renderDialogueWindow();

        // Return the camera to the player
        if (phase == 4) {
            // Find the fictional character (PlayerDummy)
            for (int i = 0; i < world.entities.mobs[1].length; i++) {
                if (world.entities.mobs[world.map.num][i] != null && world.entities.mobs[world.map.num][i].stats.name.equals(PlayerDummy.NAME)) {
                    // Restores the position of the player
                    world.entities.player.pos.x = world.entities.mobs[world.map.num][i].pos.x;
                    world.entities.player.pos.y = world.entities.mobs[world.map.num][i].pos.y;
                    // Delete the fictional character
                    world.entities.mobs[world.map.num][i] = null;
                    break;
                }
            }

            // Now you can draw the player
            world.entities.player.drawing = true;

            // Restart the scene
            n = na;
            phase = 0;
            State.setState(State.PLAY);
        }

    }

}
