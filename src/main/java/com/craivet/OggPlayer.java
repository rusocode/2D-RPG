package com.craivet;

import com.craivet.gfx.Assets;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
 * <br>
 * <a href="https://www.tutorialspoint.com/vlcj/vlcj_audio_player.htm#:~:text=Open%20project%20mediaPlayer%20as%20created%20in%20Environment%20Setup%20chapter%20in%20Eclipse.&text=Now%20press%20play%20button%20in,button%20will%20pause%20the%20audio">...</a>.
 * <a href="https://stackoverflow.com/questions/45881054/can-not-include-vlc-library-in-java-application">...</a>
 */

public class OggPlayer extends JFrame {

	private final AudioPlayerComponent audioPlayerComponent;

	public OggPlayer() {
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

		// Carga las bibliotecas nativas de VLC desde la ruta especificada
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "native\\vlc-3.0.18-win64");

		audioPlayerComponent = new AudioPlayerComponent();
		audioPlayerComponent.mediaPlayer().media().play(Assets.swing_weapon);

	}

	public static void main(String[] args) {
		new OggPlayer().setVisible(true);
	}

}
