package com.punkipunk.audio;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;

import static com.punkipunk.utils.Global.*;

public class AudioManager {

    private final Game game;

    public AudioManager(Game game) {
        this.game = game;
    }

    public void playSpawnSound() {
        game.playSound(Assets.getAudio(AudioAssets.SPAWN2));
    }

    public void playZoneAmbient() {
        switch (game.systems.world.map.zone) {
            case OVERWORLD -> game.playAmbient(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
            case DUNGEON -> game.playAmbient(Assets.getAudio(AudioAssets.AMBIENT_DUNGEON));
            case BOSS -> game.playMusic(Assets.getAudio(AudioAssets.MUSIC_BOSS));
        }
    }

    public void playHoverSound() {
        game.playSound(Assets.getAudio(AudioAssets.HOVER));
    }

    public void playClickSound() {
        game.playSound(Assets.getAudio(AudioAssets.CLICK2));
    }

    public void stopMusic() {
        game.systems.music.stop();
    }

}
