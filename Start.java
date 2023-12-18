import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Start extends JFrame {
    public static Music backgroundMusic;
    ImageIcon menu;
    ImageIcon startimg = new ImageIcon("./images/startbutton.png");
    ImageIcon optionimg = new ImageIcon("./images/optionbutton.png");
    ImageIcon exitimg = new ImageIcon("./images/exitbutton.png");

    Image simg = startimg.getImage();
    Image oimg = optionimg.getImage();
    Image eimg = exitimg.getImage();

    public Start() {

        //img size change
        Image csimg = simg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);
        Image cosimg = oimg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);
        Image ceimg = eimg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);

        ImageIcon cstartimg = new ImageIcon(csimg);
        ImageIcon coptionimg = new ImageIcon(cosimg);
        ImageIcon cexitimg = new ImageIcon(ceimg);

        menu = new ImageIcon("./images/menu.png");

        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                //img size to panel size
                g.drawImage(menu.getImage(), 0, 0, getWidth(), getHeight(), this);
                setOpaque(false);
                super.paintComponent(g);
            }
        };

        JButton bgame = new JButton(cstartimg);
        JButton boption = new JButton(coptionimg);
        JButton bquit = new JButton(cexitimg);

        //button size
        Dimension buttonSize = new Dimension(170, 130);
        bgame.setPreferredSize(buttonSize);
        boption.setPreferredSize(buttonSize);
        bquit.setPreferredSize(buttonSize);

        //add buttons
        background.add(bgame);
        background.add(boption);
        background.add(bquit);
        setContentPane(background);

        //music function call
        try {
            backgroundMusic = new Music();
            backgroundMusic.playMusic("./music/main-music.wav", 0, true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("error");
        }

        //start button
        bgame.addActionListener(e -> new Gamemap().make_Gamemap(backgroundMusic));
        //exit button
        bquit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "want to exit?", "exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                //yes->quit
                System.exit(0);
            }
        });
        //option button
        boption.addActionListener(e -> backgroundMusic.music_control());
    }

    public static void main(String[] args) {
        Start frame = new Start();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //to full screen
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}