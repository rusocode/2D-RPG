package com.punkipunk.audio;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

/**
 * This class only reads audio in wav format.
 */

public class Audio {

    private Clip clip;
    private FloatControl fc; // This class accepts values between -80f and 6f, so 6 is the maximum and -80 has no sound
    public int volumeScale = 2; // There are only 5 volume scales
    private float volume;

    public void play(URL url) {
        try {
            // Get the clip
            clip = AudioSystem.getClip();
            // Opens the clip with the format and audio data present in the provided audio input stream
            clip.open(AudioSystem.getAudioInputStream(url));
            // Gets control to be able to pass a value to the clip and change its volume
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // This check is only necessary if the music is already playing.
            checkVolume();
            // Start the clip
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(null, "The audio format is not supported.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading audio file.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(null, "Could not get audio clip. \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Stops the clip.
     */
    public void stop() {
        if (clip != null) clip.stop();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Check the volume.
     */
    public void checkVolume() {
        switch (volumeScale) {
            case 0 -> volume = -80f;
            case 1 -> volume = -20f;
            case 2 -> volume = -12f;
            case 3 -> volume = -5f;
            case 4 -> volume = 1f;
            case 5 -> volume = 6f;
        }
        if (fc != null) fc.setValue(volume);
    }

}