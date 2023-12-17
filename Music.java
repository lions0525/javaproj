import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Music extends JDialog{
    private Clip clip;
    private Clip clip2;
    //music run function
    public void music_run(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        setVolume(0.5f);
        clip.start();
    }
    public void sound_run(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
        clip2 = AudioSystem.getClip();
        clip2.open(audioInputStream);
        setVolume(1f);
        clip2.start();
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

    private void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}
