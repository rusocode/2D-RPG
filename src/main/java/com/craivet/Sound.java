package com.craivet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * Esta clase solo lee audios en formato wav.
 *
 * <p>Java no puede manejar archivos comunes de alta calidad (48000,0 Hz, 32 bits, etc.), si encuentra algo como esto:
 * <code>line with format PCM_SIGNED 192000.0 Hz, 24 bit, stereo, 6 bytes/frame, little-endian not supported.</code>
 * Tendra que degradar su archivo, usar el convertidor web y ajustarlo a ~20000.
 */

public class Sound {

	Clip clip;
	URL[] sounds = new URL[30];

	public Sound() {
		sounds[0] = getClass().getClassLoader().getResource("sounds/BlueBoyAdventure.wav");
		sounds[1] = getClass().getClassLoader().getResource("sounds/coin.wav");
		sounds[2] = getClass().getClassLoader().getResource("sounds/powerup.wav");
		sounds[3] = getClass().getClassLoader().getResource("sounds/unlock.wav");
		sounds[4] = getClass().getClassLoader().getResource("sounds/fanfare.wav");
		sounds[5] = getClass().getClassLoader().getResource("sounds/hitmonster.wav");
		sounds[6] = getClass().getClassLoader().getResource("sounds/receivedamage.wav");
		sounds[7] = getClass().getClassLoader().getResource("sounds/swingweapon.wav");

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
