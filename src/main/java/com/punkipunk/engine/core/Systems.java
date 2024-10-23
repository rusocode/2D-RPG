package com.punkipunk.engine.core;

import com.punkipunk.Game;
import com.punkipunk.Minimap;
import com.punkipunk.UI;
import com.punkipunk.ai.AStar;
import com.punkipunk.audio.Audio;
import com.punkipunk.controllers.GameController;
import com.punkipunk.input.keyboard.KeyboardHandler;
import com.punkipunk.io.File;
import com.punkipunk.physics.Collision;
import com.punkipunk.physics.Event;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.item.ItemGenerator;
import javafx.scene.Scene;

/**
 * Central system that manages all subsystems.
 */

public class Systems {

    public KeyboardHandler keyboard;
    public World world;
    public UI ui;
    public ItemGenerator itemGenerator;
    public Minimap minimap;
    public Audio music;
    public Audio sound;
    public File file;
    public Collision collision;
    public Event event;
    public AStar aStar;
    public Updater updater;
    public Renderer renderer;
    public OldGameLoop oldGameLoop;

    public Systems(Game game, Scene scene, GameController gameController) {
        world = new World(game);
        keyboard = new KeyboardHandler(game, scene, gameController);
        ui = new UI(game, world);
        itemGenerator = new ItemGenerator(game, world);
        minimap = new Minimap(game, world);
        music = new Audio();
        sound = new Audio();
        file = new File(game, world);
        collision = new Collision(world);
        event = new Event(game, world);
        aStar = new AStar(world);
        updater = new Updater(world, game);
        renderer = new Renderer(game, world, ui, minimap);
        oldGameLoop = new OldGameLoop();
    }

    public void reset(boolean fullReset) {
        ui.console.clear();
        world.entities.player.reset(fullReset);
        world.entities.factory.createMobs();
        world.entities.removeTempEntities();
        world.entities.player.bossBattleOn = false;
        if (fullReset) {
            world.entities.factory.createEntities();
            world.environment.lighting.resetDay();
            keyboard.resetToggledKeys();
        }
    }

}