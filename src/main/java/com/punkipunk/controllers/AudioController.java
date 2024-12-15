package com.punkipunk.controllers;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.audio.Audio;

import java.net.URL;

import static com.punkipunk.utils.Global.*;

// TODO No deberia ir en el paquete controller?

public class AudioController {

    private final Game game;

    private final Audio ambient = new Audio();
    private final Audio music = new Audio();
    private final Audio sound = new Audio();

    public AudioController(Game game) {
        this.game = game;
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
