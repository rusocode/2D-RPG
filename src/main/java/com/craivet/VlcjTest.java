package com.craivet;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import com.craivet.gfx.Assets;

/**
 * La version de las bibliotecas nativas de LibVLC es la 3.0.18 para win64 y se encuentran en la carpeta natives dentro
 * de esta aplicacion.
 *
 * <p>Estas bibliotecas nativas proporcionan la API publica para LibVLC y es lo que le permite integrar reproductores
 * multimedia VLC en sus propias aplicaciones.
 *
 * <p>Para que vlcj pueda encontrar las bibliotecas nativas de LibVLC se utiliza la siguiente linea de codigo:
 * <pre>{@code NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "natives");}</pre>
 *
 * <br><br>
 * <a href="https://capricasoftware.co.uk/projects/vlcj-4/tutorials/garbage-collection">Garbage Collection</a>
 * <a href="https://www.tutorialspoint.com/vlcj/vlcj_audio_player.htm#:~:text=Open%20project%20mediaPlayer%20as%20created%20in%20Environment%20Setup%20chapter%20in%20Eclipse.&text=Now%20press%20play%20button%20in,button%20will%20pause%20the%20audio">...</a>.
 * <a href="https://stackoverflow.com/questions/45881054/can-not-include-vlc-library-in-java-application">...</a>
 */

public class VlcjTest extends JFrame {

	/* Su aplicacion debe mantener referencias fijas a las instancias del reproductor multimedia que cree. Si no hace
	 * esto, su reproductor multimedia se convertira inesperadamente en basura recolectada por el GC en algun momento
	 * futuro indeterminado y vera un bloqueo fatal eventualmente o de inmediato, dependiendo de la suerte que tenga. */
	private final AudioPlayerComponent audioPlayerComponent;

	public VlcjTest() {
		new JFrame();
		setBounds(100, 100, 600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				audioPlayerComponent.release();
				System.exit(0);
			}
		});

		// Carga las bibliotecas nativas de VLC antes de utilizar el reproductor
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "native\\vlc-3.0.18-win64");

		audioPlayerComponent = new AudioPlayerComponent();

		// Agrega un detector de eventos al reproductor de medios
		audioPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			// Controladores de eventos basicos
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				mediaPlayer.media().play(Assets.swing_weapon);
				System.out.println("playing");
			}

			@Override
			public void finished(MediaPlayer mediaPlayer) {
				mediaPlayer.media().play(Assets.swing_weapon);
				/*SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("finished");
						exit(0);
					}
				}); */
			}

			@Override
			public void error(MediaPlayer mediaPlayer) {
				System.out.println("error");
				exit(1);
			}
		});

		// audioPlayerComponent.mediaPlayer().media().play(Assets.swing_weapon);

	}

	private void exit(int result) {
		// No esta permitido volver a llamar a LibVLC desde un subproceso de manejo de eventos, por lo que se usa submit
		audioPlayerComponent.mediaPlayer().submit(new Runnable() {
			@Override
			public void run() {
				audioPlayerComponent.mediaPlayer().release();
				System.exit(result);
			}
		});
	}

	public static void main(String[] args) {
		new VlcjTest().setVisible(true);
	}

}
