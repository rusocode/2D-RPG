package com.craivet;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class Sound2 {

	// private final AudioPlayerComponent audioPlayerComponent;

	public int volumeScale = 2; // Solo hay 5 escalas de volumen

	public Sound2() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "native\\vlc-3.0.18-win64");
		// audioPlayerComponent = new AudioPlayerComponent();
	}

	/* public void play(String path) {
		audioPlayerComponent.mediaPlayer().media().play(path);
	}

	public void stop() {
		audioPlayerComponent.mediaPlayer().controls().stop();
	}

	public void loop() {
		audioPlayerComponent.mediaPlayer().controls().setRepeat(true);
	}*/

}
