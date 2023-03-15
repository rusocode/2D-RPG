package com.craivet;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase MediaPlayerFactory es una clase de utilidad esencial que permite crear instancias de objetos MediaPlayer y
 * otros objetos relacionados con la reproduccion de medios. Es responsable de crear y controlar la configuracion global
 * de MediaPlayer, mientras que MediaPlayer es responsable de reproducir archivos multimedia individuales.
 *
 * <br><br>
 * <a href="https://capricasoftware.co.uk/projects/vlcj-4/tutorials/thread-model">Thread Model</a>
 */

public class SoundVLCJ {

	private final List<MediaPlayer> mediaPlayers;
	private final MediaPlayerFactory mediaPlayerFactory;

	public int volumeScale = 2; // Solo hay 5 escalas de volumen

	public SoundVLCJ() {
		// Carga las bibliotecas nativas de VLC antes de utilizar el reproductor
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "native\\vlc-3.0.18-win64");
		mediaPlayers = new ArrayList<>();
		// El parametro --no-video evita crear recursos innecesarios para la decodificacion y representacion de video
		mediaPlayerFactory = new MediaPlayerFactory("--no-video");
	}

	public void play(String path) {
		MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
		mediaPlayer.media().start(path);
		mediaPlayers.add(mediaPlayer);

		mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

			@Override
			public void playing(MediaPlayer mediaPlayer) {
			}

			@Override
			public void finished(MediaPlayer mediaPlayer) {
				/* De esta manera, se liberan los recursos en un hilo separado sin afectar el rendimiento de la
				 * aplicacion principal. Ademas, esta opcion aprovecha el pool de hilos interno de vlcj para ejecutar la
				 * liberacion de recursos en un hilo separado, lo que puede ser mas eficiente que crear un nuevo hilo
				 * cada vez que se necesita liberar recursos. */
				mediaPlayer.submit(() -> {
					mediaPlayer.release();
					mediaPlayers.remove(mediaPlayer);
					System.out.println("Audio eliminado");
				});
			}

			@Override
			public void error(MediaPlayer mediaPlayer) {
			}

		});

	}

	public void stopAll() {
		for (MediaPlayer mediaPlayer : mediaPlayers)
			mediaPlayer.controls().stop();
	}

	/**
	 * Libera los componentes del reproductor multimedia para evitar fugas de recursos nativos.
	 */
	public void release() {
		// Se supone que n
		/* for (MediaPlayer mediaPlayer : mediaPlayers) {
			mediaPlayer.release();
			mediaPlayers.remove(mediaPlayer);
		} */
		mediaPlayers.clear();
		mediaPlayerFactory.release();
	}

}
