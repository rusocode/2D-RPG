package com.craivet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

public class OggPlayer {

	public static void main(String[] args) {
		/*try {
			// Crea una nueva instancia de FileInputStream para el archivo de audio
			FileInputStream audioFile = new FileInputStream("path/to/audio.ogg");
			// Crea una nueva instancia de DspState
			DspState dspState = new DspState();
			// Crea una nueva instancia de Block
			Block block = new Block(dspState);
			// Crea una nueva instancia de Comment
			Comment comment = new Comment();
			// Crea una nueva instancia de Info
			Info info = new Info();

			// Llama al método `init` en la instancia de Info para inicializar la información del archivo de audio
			info.init();

			// Llama al método init en la instancia de Comment para inicializar los comentarios del archivo de audio
			comment.init();

			// Llama al método synthesis_headerin en la instancia de Info y Comment para leer la información del archivo de audio
			com.jcraft.jogg.Packet packet = new com.jcraft.jogg.Packet();
			com.jcraft.jogg.Page page = new com.jcraft.jogg.Page();
			com.jcraft.jogg.StreamState streamState = new com.jcraft.jogg.StreamState();
			streamState.init(audioFile);
			int serial = streamState.serialno();
			info.synthesis_headerin(comment, packet);

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}*/
	}
}