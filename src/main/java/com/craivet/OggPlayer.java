package com.craivet;

import com.craivet.gfx.Assets;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * La version de las bibliotecas nativas de LibVLC es la 3.0.18 para win64 y se encuentran en la carpeta natives dentro
 * de esta aplicacion.
 *
 * <p>Estas bibliotecas nativas proporcionan la API pUblica para LibVLC y es lo que le permite integrar reproductores
 * multimedia VLC en sus propias aplicaciones.
 *
 * <p>Para que vlcj pueda encontrar las bibliotecas nativas de LibVLC se utiliza la siguiente linea de codigo:
 * <pre>{@code NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "natives");}</pre>
 *
 * <br>
 * <a href="https://www.tutorialspoint.com/vlcj/vlcj_audio_player.htm#:~:text=Open%20project%20mediaPlayer%20as%20created%20in%20Environment%20Setup%20chapter%20in%20Eclipse.&text=Now%20press%20play%20button%20in,button%20will%20pause%20the%20audio">...</a>.
 * <a href="https://stackoverflow.com/questions/45881054/can-not-include-vlc-library-in-java-application">...</a>
 */

public class OggPlayer {

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private AudioPlayerComponent audioPlayerComponent;

	public OggPlayer() {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.release();
				System.exit(0);
			}
		});

		// LINEA IMPORTANTE!
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Users\\juand\\Downloads\\vlc-3.0.18");

		// "\\C:\\Users\\juand\\Documents\\IntelliJ IDEA\\Game2D-3\\target\\classes\\sounds\\swing_weapon.ogg"
		String path = Objects.requireNonNull(OggPlayer.class.getClassLoader().getResource("audio/sounds/swing_weapon3.ogg")).getPath();
		String formatPath = path.replace("%20", " ");
		String asd = formatPath.replaceAll("/", "\\\\");
		// System.out.println(asd);

		String path2 = String.valueOf(this.getClass().getClassLoader().getResource("audio/sounds/swing_weapon3.ogg"));
		System.out.println(path2);

		// audioPlayerComponent = new AudioPlayerComponent();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		frame.setContentPane(mediaPlayerComponent);

		frame.setVisible(true);

		// C:/Users/juand/Documents/IntelliJ%20IDEA/Game2D-3/target/classes/audio/sounds/swing_weapon3.ogg
		mediaPlayerComponent.mediaPlayer().media().play(Assets.swing_weapon2);
		// audioPlayerComponent.mediaPlayer().media().startPaused(asd);
		// audioPlayerComponent.mediaPlayer().controls().play();

	}

	public static void main(String[] args) {
		new OggPlayer();
	}

}
