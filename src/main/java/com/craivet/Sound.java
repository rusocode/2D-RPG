package com.craivet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

	Clip clip;
	URL[] sounds = new URL[30];

	public Sound() {
		sounds[0] = getClass().getClassLoader().getResource("sounds/BlueBoyAdventure.wav");
		sounds[1] = getClass().getClassLoader().getResource("sounds/coin.wav");
		sounds[2] = getClass().getClassLoader().getResource("sounds/powerup.wav");
		sounds[3] = getClass().getClassLoader().getResource("sounds/unlock.wav");
		sounds[4] = getClass().getClassLoader().getResource("sounds/fanfare.wav");
	}

	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(sounds[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void play() {
		clip.start();
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}

}
