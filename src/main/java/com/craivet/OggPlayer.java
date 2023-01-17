package com.craivet;

import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.JFrame;

/**
 * <a href="https://www.tutorialspoint.com/vlcj/vlcj_audio_player.htm#:~:text=Open%20project%20mediaPlayer%20as%20created%20in%20Environment%20Setup%20chapter%20in%20Eclipse.&text=Now%20press%20play%20button%20in,button%20will%20pause%20the%20audio">...</a>.
 * */

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

		System.out.println(OggPlayer.class.getClassLoader().getResourceAsStream("sounds/swing_weapon.ogg").toString());

		// "C:\\Users\\juand\\Documents\\IntelliJ IDEA\\Game2D-3\\src\\main\\resources\\sounds\\swing_weapon.ogg"
		// audioPlayerComponent.mediaPlayer().media().startPaused(OggPlayer.class.getClassLoader().getResource("sounds/swing_weapon.ogg").getPath());
		audioPlayerComponent.mediaPlayer().controls().play();
	}

	public static void main(String[] args) {
		new OggPlayer();
	}

}
