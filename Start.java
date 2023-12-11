import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame {
    JScrollPane scrollPane;
    ImageIcon menu;
    ImageIcon startimg = new ImageIcon("images/startbutton.png");
    ImageIcon optionimg = new ImageIcon("images/optionbutton.png");
    ImageIcon exitimg = new ImageIcon("images/exitbutton.png");
    Image simg = startimg.getImage();
    Image oimg = optionimg.getImage();
    Image eimg = exitimg.getImage();
    Image csimg = simg.getScaledInstance(60, 30, Image.SCALE_SMOOTH);
    Image cosimg = oimg.getScaledInstance(60, 30, Image.SCALE_SMOOTH);
    Image ceimg = eimg.getScaledInstance(60, 30, Image.SCALE_SMOOTH);

    ImageIcon cstartimg = new ImageIcon(csimg);
    ImageIcon coptionimg = new ImageIcon(cosimg);
    ImageIcon cexitimg = new ImageIcon(ceimg);

    public Start() {
        menu = new ImageIcon("images/menu.png");
        JPanel background = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(menu.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
            }
        };

        JButton bgame = new JButton(cstartimg);
        JButton boption = new JButton(coptionimg);
        JButton bquit = new JButton(cexitimg);

        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(60, 30);
        bgame.setPreferredSize(buttonSize);
        boption.setPreferredSize(buttonSize);
        bquit.setPreferredSize(buttonSize);

        background.add(bgame);
        background.add(boption);
        background.add(bquit);
        scrollPane = new JScrollPane(background);
        setContentPane(scrollPane);

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
    }

    public static void main(String[] args) {
        Start frame = new Start();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}