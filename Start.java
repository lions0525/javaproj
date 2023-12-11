import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Start extends JFrame {
    JScrollPane scrollPane;
    ImageIcon menu;
    ImageIcon startimg = new ImageIcon("./images/startbutton.png");
    ImageIcon optionimg = new ImageIcon("./images/optionbutton.png");
    ImageIcon exitimg = new ImageIcon("./images/exitbutton.png");

    Image simg = startimg.getImage();
    Image oimg = optionimg.getImage();
    Image eimg = exitimg.getImage();

    public Start() {
        // 이미지 크기 조절
        Image csimg = simg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);
        Image cosimg = oimg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);
        Image ceimg = eimg.getScaledInstance(170, 130, Image.SCALE_SMOOTH);

        ImageIcon cstartimg = new ImageIcon(csimg);
        ImageIcon coptionimg = new ImageIcon(cosimg);
        ImageIcon cexitimg = new ImageIcon(ceimg);

        menu = new ImageIcon("./images/menu.png");

        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                // 이미지를 프레임의 크기에 맞춰서 그립니다.
                g.drawImage(menu.getImage(), 0, 0, getWidth(), getHeight(), this);
                setOpaque(false);
                super.paintComponent(g);
            }
        };

        JButton bgame = new JButton(cstartimg);
        JButton boption = new JButton(coptionimg);
        JButton bquit = new JButton(cexitimg);

        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(170, 130);
        bgame.setPreferredSize(buttonSize);
        boption.setPreferredSize(buttonSize);
        bquit.setPreferredSize(buttonSize);

        background.add(bgame);
        background.add(boption);
        background.add(bquit);
        scrollPane = new JScrollPane(background);
        setContentPane(scrollPane);

        System.out.println(new File("music/main-music.wav").exists());

        try{
            Music.music("music/main-music.wav");
        }catch(UnsupportedAudioFileException |IOException|LineUnavailableException e){
            System.out.println("error");
        }

        // 나가기 버튼에 ActionListener 추가
        bquit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "want to exit?", "exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // 예를 선택하면 애플리케이션 종료
                    System.exit(0);
                }
            }
        });

        boption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "want to exit?", "exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // 예를 선택하면 애플리케이션 종료
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] args){
        Start frame = new Start();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면으로 설정
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}