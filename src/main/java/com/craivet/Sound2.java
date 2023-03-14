package com.craivet;

import java.io.File;
import java.util.Map;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;

public class Sound2 extends StreamPlayer implements StreamPlayerListener {

	private final String audioAbsolutePath = "Logic - Ballin [Bass Boosted].mp3";

	public Sound2() {
		try {

			// Register to the Listeners
			addStreamPlayerListener(this);

			// Open a File
			// open(new File("...")) //..Here must be the file absolute path
			// open(INPUTSTREAM)
			// open(AUDIOURL)

			// Example
			open(new File(audioAbsolutePath));

			// Seek by bytes
			// seekBytes(500000L);

			// Seek +x seconds starting from the current position
			seekSeconds(15);
			seekSeconds(15);

			/* Seek starting from the begginning of the audio */
			// seekTo(200);

			// Play it
			play();
			// pause();

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void opened(Object o, Map<String, Object> map) {

	}

	@Override
	public void progress(int i, long l, byte[] bytes, Map<String, Object> map) {

	}

	@Override
	public void statusUpdated(StreamPlayerEvent streamPlayerEvent) {

	}
}
