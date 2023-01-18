package com.craivet;

import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.JFrame;
import java.util.Objects;

/**
 * <a href="https://www.tutorialspoint.com/vlcj/vlcj_audio_player.htm#:~:text=Open%20project%20mediaPlayer%20as%20created%20in%20Environment%20Setup%20chapter%20in%20Eclipse.&text=Now%20press%20play%20button%20in,button%20will%20pause%20the%20audio">...</a>.
 * <a href="https://stackoverflow.com/questions/45881054/can-not-include-vlc-library-in-java-application">...</a>
 */

public class OggPlayer {

	public OggPlayer() {
		JFrame frame = new JFrame("My First Media Player");
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		AudioPlayerComponent audioPlayerComponent = new AudioPlayerComponent();
		// EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		// frame.setContentPane(mediaPlayerComponent);

		frame.setVisible(true);

		// mediaPlayerComponent.mediaPlayer().media().play(args[0]);

		// "\\C:\\Users\\juand\\Documents\\IntelliJ IDEA\\Game2D-3\\target\\classes\\sounds\\swing_weapon.ogg"

		String path = Objects.requireNonNull(OggPlayer.class.getClassLoader().getResource("sounds/swing_weapon.ogg")).getPath();
		String formatPath;
		formatPath = path.replace("%20", " ");
		String asd = formatPath.replaceAll("/", "\\\\");
		System.out.println(asd);

		audioPlayerComponent.mediaPlayer().media().startPaused(asd);
		audioPlayerComponent.mediaPlayer().controls().play();
	}

	public static void main(String[] args) {
		new OggPlayer();
	}

}
