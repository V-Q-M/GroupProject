import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {
    GamePanel gamePanel;

    Clip shootClip;
    Clip sliceClip;
    Clip hitClip;
    Clip deathClip;
    Clip menuTheme;
    Clip mainTheme;

    public SoundManager(GamePanel gamePanel){
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
       mainTheme.start();
    }

    protected void loadSounds() {
        shootClip = loadClip("res/shoot.wav");
        sliceClip = loadClip("res/slice1.wav");
        hitClip   = loadClip("res/hit.wav");
        deathClip = loadClip("res/death.wav");
        menuTheme = loadClip("res/menuTheme.wav");
        mainTheme = loadClip("res/mainTheme2.wav");
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
