package com.craivet;

import com.craivet.gfx.Assets;

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

	private Clip clip;

	/**
	 * Obtiene, abre y inicia el clip.
	 *
	 * @param url la url del audio.
	 */
	public void play(URL url) {
		try {
			// Obtiene el clip
			clip = AudioSystem.getClip();
			// Abre el clip con el formato y los datos de audio presentes en el flujo de entrada de audio proporcionado
			clip.open(AudioSystem.getAudioInputStream(url));
			// Inicia el clip
			clip.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Detiene el clip.
	 */
	public void stop() {
		clip.stop();
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

}
