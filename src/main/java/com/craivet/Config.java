package com.craivet;

import java.io.*;

public class Config {

	Game game;

	public Config(Game game) {
		this.game = game;
	}

	public void saveConfig() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

			if (game.fullScreen) bw.write("On");
			if (!game.fullScreen) bw.write("Off");
			bw.newLine();

			bw.write(String.valueOf(game.music.volumeScale));
			bw.newLine();

			bw.write(String.valueOf(game.sound.volumeScale));
			bw.newLine();

			bw.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void loadConfig() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));

			String s = br.readLine();

			if (s.equals("On")) game.fullScreen = true;
			if (s.equals("Off")) game.fullScreen = false;

			s = br.readLine();
			game.music.volumeScale = Integer.parseInt(s);

			s = br.readLine();
			game.sound.volumeScale = Integer.parseInt(s);

			br.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}


