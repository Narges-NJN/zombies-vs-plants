package util;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundHandler {
    public static void playSound(String path, boolean play){
        File music = new File(path);
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            System.out.println("Sound Unavailable");
        }
        try {
            clip.open(AudioSystem.getAudioInputStream(music));
        } catch (LineUnavailableException e) {
            System.out.println("Sound Unavailable");
        } catch (IOException e) {
            System.out.println("Sound IO exception");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported Audio File");
        }
        if (play)
            clip.start();
        else clip.stop();
    }
}
