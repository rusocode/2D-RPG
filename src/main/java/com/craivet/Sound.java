package com.craivet;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

/**
 * Esta clase solo lee audios en formato wav.
 *
 * <p>
 * Java no puede manejar archivos comunes de alta calidad (48000,0 Hz, 32 bits, etc.), si encuentra algo como esto:
 * <code>line with format PCM_SIGNED 192000.0 Hz, 24 bit, stereo, 6 bytes/frame, little-endian not supported.</code>
 * Tendra que degradar su archivo, usar el convertidor web y ajustarlo a ~20000.
 *
 * <p>
 * TODO Se podrian guardar los recursos de sonido en esta clase para mas organizacion
 *
 * <p>
 * <a href="https://mvnrepository.com/artifact/com.googlecode.soundlibs/vorbisspi/1.0.3.3">Ogg Vorbis SPI for .ogg support</a>
 */

public class Sound {

	private Clip clip;
	private FloatControl fc; // Esta clase acepta valores entre -80f y 6f, por lo que 6 es el maximo y -80 no tiene sonido
	public int volumeScale = 2; // Solo hay 5 escalas de volumen
	private float volume;

	public void play(URL url) {
		try {
			// Obtiene el clip
			clip = AudioSystem.getClip();
			// Abre el clip con el formato y los datos de audio presentes en el flujo de entrada de audio proporcionado
			clip.open(AudioSystem.getAudioInputStream(url));
			// Obtiene el control para poder pasar un valor al clip y cambiar su volumen
			fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			// Esta comprobacion es necesaria solo si la musica ya se esta reproduciendo
			checkVolume();
			// Inicia el clip
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			JOptionPane.showMessageDialog(null, "El formato de audio no es compatible.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al leer el archivo de audio.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (LineUnavailableException e) {
			JOptionPane.showMessageDialog(null, "No se pudo obtener el clip de audio.", "Error", JOptionPane.ERROR_MESSAGE);
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

	/**
	 * Comprueba el volumen.
	 */
	public void checkVolume() {
		switch (volumeScale) {
			case 0:
				volume = -80f;
				break;
			case 1:
				volume = -20f;
				break;
			case 2:
				volume = -12f;
				break;
			case 3:
				volume = -5f;
				break;
			case 4:
				volume = 1f;
				break;
			case 5:
				volume = 6f;
				break;
		}
		fc.setValue(volume);
	}

}
