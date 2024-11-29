package com.punkipunk.audio;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;

import java.net.URL;

import static com.punkipunk.utils.Global.*;

// TODO No deberia ir en el paquete controller?

public class AudioController {

    private final Game game;

    private final Audio ambient;
    private final Audio music;
    private final Audio sound;

    public AudioController(Game game) {
        this.game = game;
        this.music = new Audio();
        this.ambient = new Audio();
        this.sound = new Audio();
    }

    public void playSound(URL url) {
        sound.play(url);
    }

    public void playAmbient(URL url) {
        ambient.stop();
        ambient.play(url);
        ambient.loop();
    }

    public void playMusic(URL url) {
        music.stop();
        music.play(url);
        music.loop();
    }

    public void stop() {
        ambient.stop();
        music.stop();
    }

    public void playZoneAmbient() {
        switch (game.system.world.map.zone) {
            case OVERWORLD -> playAmbient(Assets.getAudio(AudioAssets.AMBIENT_OVERWORLD));
            case DUNGEON -> playAmbient(Assets.getAudio(AudioAssets.AMBIENT_DUNGEON));
            case BOSS -> playMusic(Assets.getAudio(AudioAssets.MUSIC_BOSS));
        }
    }

    public Audio getAmbient() {
        return ambient;
    }

    public Audio getMusic() {
        return music;
    }

    public Audio getSound() {
        return sound;
    }

}
