import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class Music extends JDialog{
    private Clip clip;
    private float currentVol = 0.5f;

    public void playMusic(String filename, boolean loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        playMusic(filename, null, loop);
    }

    //music run function
    public void playMusic(String filename, Integer volume, boolean loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        if (loop)
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        if (volume != null)
            currentVol = volume / 100f;
        setVolume(currentVol);
        clip.start();
    }

    public void music_control() {
        //music frame
        JFrame controlFrame = new JFrame("Volume Control");
        controlFrame.setSize(300, 100);
        controlFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //music slider
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        //volume change
        volumeSlider.addChangeListener(e -> setVolume(volumeSlider.getValue() / 100.0f));
        controlFrame.getContentPane().add(BorderLayout.CENTER, volumeSlider);
        controlFrame.setVisible(true);
    }

    public float getVolume() {
        return currentVol;
    }

    private void setVolume(float volume) {
        if (clip == null)
            return;
        currentVol = volume;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }
}
