package com.craivet;

import javax.swing.*;
import java.io.*;

import static com.craivet.utils.Constants.*;

public class Config {

	private final Game game;
	private static final String ON = "On";
	private static final String OFF = "Off";

	public Config(Game game) {
		this.game = game;
	}

	public void saveConfig() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
			bw.write(game.fullScreen ? ON : OFF);
			bw.newLine();
			bw.write(String.valueOf(game.music.volumeScale));
			bw.newLine();
			bw.write(String.valueOf(game.soundVLCJ.volumeScale));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al guardar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void loadConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
			game.fullScreen = ON.equals(br.readLine());
			game.music.volumeScale = Integer.parseInt(br.readLine());
			game.soundVLCJ.volumeScale = Integer.parseInt(br.readLine());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}


