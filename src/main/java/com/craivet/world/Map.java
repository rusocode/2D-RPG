package com.craivet.world;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;

import static com.craivet.utils.Global.*;

public class Map {

    private final Game game;
    private final World world;

    public int num, zone, nextZone;

    public Tile[] tileData;
    public int[][][] tileIndex;

    public Map(Game game, World world) {
        this.game = game;
        this.world = world;
        tileIndex = new int[MAPS][MAX_MAP_ROW][MAX_MAP_COL];
    }

    public void changeArea() {
        // If there is a change of area
        if (nextZone != zone) {
            if (nextZone == BOSS) game.playMusic(Assets.getAudio(AudioAssets.MUSIC_BOSS));
            if (nextZone == DUNGEON) game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_DUNGEON));
            if (zone == DUNGEON && nextZone == OVERWORLD) game.playMusic(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
        }
        zone = nextZone;
        world.entities.factory.createMobs();
    }


}
