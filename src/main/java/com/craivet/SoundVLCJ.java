package com.craivet;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

import javax.swing.*;

public class SoundVLCJ {

	private final AudioPlayerComponent audioPlayerComponent;

	public int volumeScale = 2; // Solo hay 5 escalas de volumen

	public SoundVLCJ() {
		// Carga las bibliotecas nativas de VLC antes de utilizar el reproductor
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "native\\vlc-3.0.18-win64");
		audioPlayerComponent = new AudioPlayerComponent();

		audioPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				System.out.println("playing");
			}

			@Override
			public void finished(MediaPlayer mediaPlayer) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("finished");
						// exit(0);
					}
				});
			}

			@Override
			public void error(MediaPlayer mediaPlayer) {
				System.out.println("error");
				exit(1);
			}
		});

	}

	private void exit(int result) {
		audioPlayerComponent.mediaPlayer().release();
		// System.exit(result);
	}

	public void play(String path) {
		/* Creando un objeto por cada sonido soluciona el problema de los fps pero genera un "JNA: callback object has
		 * been garbage collected". */
		// audioPlayerComponent = new AudioPlayerComponent(); // Crea un objeto por cada reproduccion
		audioPlayerComponent.mediaPlayer().media().play(path);
	}

	public void stop() {
		audioPlayerComponent.mediaPlayer().controls().stop();
	}

	public void loop() {
		audioPlayerComponent.mediaPlayer().controls().setRepeat(true);
	}

	public AudioPlayerComponent getAudioPlayerComponent() {
		return audioPlayerComponent;
	}

}
