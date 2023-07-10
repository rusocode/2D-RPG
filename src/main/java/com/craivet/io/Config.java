package com.craivet.io;

import com.craivet.Game;

import javax.swing.*;
import java.io.*;

import static com.craivet.util.Global.*;

public class Config {

	private final Game game;
	private static final String ON = "On";
	private static final String OFF = "Off";

	public Config(Game game) {
		this.game = game;
		loadConfig();
	}

	public void saveConfig() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
			// bw.write(game.fullScreen ? ON : OFF);
			// bw.newLine();
			bw.write(String.valueOf(game.music.volumeScale));
			bw.newLine();
			bw.write(String.valueOf(game.sound.volumeScale));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al guardar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
			// game.fullScreen = ON.equals(br.readLine());
			game.music.volumeScale = Integer.parseInt(br.readLine());
			game.sound.volumeScale = Integer.parseInt(br.readLine());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}


