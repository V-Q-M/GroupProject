package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {
    JPanel gamePanel;

    public Clip shootClip;
    public Clip sliceClip;
    public Clip hitClip;
    public Clip smashClip;
    public Clip deathClip;
    public Clip swapClip;
    public Clip buttonHoverClip;
    public Clip buttonClickClip;
    public Clip summonClip;

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
        shootClip = loadClip("/shoot.wav");
        sliceClip = loadClip("/slice1.wav");
        hitClip   = loadClip("/hit.wav");
        smashClip = loadClip("/smash.wav");
        deathClip = loadClip("/death.wav");
        swapClip  = loadClip("/swap2.wav");
        summonClip = loadClip("/summon.wav");
        menuTheme = loadClip("/menuTheme.wav");
        mainTheme = loadClip("/mainTheme2.wav");
        buttonHoverClip = loadClip("/buttonHover.wav");
        buttonClickClip = loadClip("/buttonClick.wav");
    }

    private Clip loadClip(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Audio resource not found: " + path);
            }
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(is))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
