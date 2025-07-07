package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class SoundManager {
    JPanel gamePanel;

    public Clip shootClip;
    public Clip sliceClip;
    public Clip hitClip;
    public Clip smashClip;
    public Clip deathClip;
    public Clip swapClip;
    public Clip menuTheme;
    public Clip mainTheme;

    public SoundManager(JPanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void playClip(Clip clip) {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();  // Stop current sound if needed
        clip.setFramePosition(0);           // Rewind to the beginning
        clip.start();
    }

    public void startMusic(){
       if (mainTheme == null) return;
       if (menuTheme.isRunning()) menuTheme.stop();
       if (mainTheme.isRunning()) mainTheme.stop();
       mainTheme.setFramePosition(0);
       mainTheme.loop(Clip.LOOP_CONTINUOUSLY);
       mainTheme.start();
    }

    public void startMenuMusic(){
        if(menuTheme == null) return;
        if (menuTheme.isRunning()) menuTheme.stop();
        if (mainTheme.isRunning()) mainTheme.stop();
        menuTheme.setFramePosition(0);
        menuTheme.loop(Clip.LOOP_CONTINUOUSLY);
        menuTheme.start();
    }

    public void stopMusic(){
        if (mainTheme == null) return;
        if (menuTheme.isRunning()) menuTheme.stop();
        if (mainTheme.isRunning()) mainTheme.stop();
    }

    public void loadSounds() {
        shootClip = loadClip("res/shoot.wav");
        sliceClip = loadClip("res/slice1.wav");
        hitClip   = loadClip("res/hit.wav");
        smashClip = loadClip("res/smash.wav");
        deathClip = loadClip("res/death.wav");
        swapClip  = loadClip("res/swap2.wav");
        menuTheme = loadClip("res/menuTheme.wav");
        mainTheme = loadClip("res/mainTheme3.wav");
    }

    private Clip loadClip(String path) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
