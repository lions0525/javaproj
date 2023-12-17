import javax.swing.*;
import java.awt.*;

public class Gamemap extends JPanel {
    Music music2;
    private JFrame gameFrame;
    private JPanel gamemap_panel;
    private JPanel interface_panel;
    private JPanel yutimg_panel;
    private JPanel score_panel;
    private JLabel backgroundImgLabel;
    private JPanel recordYut;
    private ImageIcon doe = new ImageIcon("./images/doe.png");
    private ImageIcon gae = new ImageIcon("./images/gae.png");
    private ImageIcon girl = new ImageIcon("./images/girl.png");
    private ImageIcon yut = new ImageIcon("./images/yut.png");
    private ImageIcon moe = new ImageIcon("./images/moe.png");
    private ImageIcon throwing = new ImageIcon("./images/throwing.png");
    public JButton bthrow;

    public void creatbutton() {
        ImageIcon throwimg = new ImageIcon("./images/throwbutton.png");
        Image sthrow = throwimg.getImage();
        Image ctimg = sthrow.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon cthrowimg = new ImageIcon(ctimg);
        bthrow = new JButton(cthrowimg);
        Dimension buttonSize = new Dimension(50, 50);
        bthrow.setPreferredSize(buttonSize);

        // 버튼을 interface_panel에 추가하기 위해 GridBagConstraints 사용
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        interface_panel.add(bthrow, gbc);
        bthrow.setVisible(false);
    }



    public void make_Gamemap(Music Music1) {
        gameFrame = new JFrame("gameFrame");
        gamemap_panel = new JPanel();
        interface_panel = new JPanel();
        yutimg_panel = new JPanel();
        score_panel = new JPanel();
        recordYut=new JPanel();

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // gameFrame->GridBagLayout
        gameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // left panel==game_map
        gamemap_panel.setBackground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gameFrame.add(gamemap_panel, gbc);

        // right panel->interface_panel
        interface_panel.setBackground(Color.blue);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // interface_panel->GridBagLayout
        interface_panel.setLayout(new GridBagLayout());
        GridBagConstraints interfaceGbc = new GridBagConstraints();

        //yut img panel
        yutimg_panel.setBackground(Color.white);
        interfaceGbc.gridx = 0;
        interfaceGbc.gridy = 0;
        interfaceGbc.weightx = 1.0;
        interfaceGbc.weighty = 1.0;
        interfaceGbc.fill = GridBagConstraints.BOTH;
        interface_panel.add(yutimg_panel, interfaceGbc);


        recordYut.setBackground(Color.green);
        interfaceGbc.gridx = 0;
        interfaceGbc.gridy = 1;
        interfaceGbc.weightx = 1.0;
        interfaceGbc.weighty = 1.0;
        interfaceGbc.fill = GridBagConstraints.BOTH;
        interface_panel.add(recordYut, interfaceGbc);

        // score panel
        score_panel.setBackground(Color.black);
        interfaceGbc.gridx = 0;
        interfaceGbc.gridy = 2;
        interfaceGbc.weightx = 1.0;
        interfaceGbc.weighty = 4.0;
        interfaceGbc.fill = GridBagConstraints.BOTH;
        interface_panel.add(score_panel, interfaceGbc);
        gameFrame.add(interface_panel, gbc);

        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setUndecorated(true);

        // initial img
        setImage(throwing);
        creatMenu(Music1);
        creatbutton();
        gameFrame.setVisible(true);
        new Gamerule(this).game();


    }
    public void setImage(ImageIcon img){
        Dimension size = new Dimension(200, 100);
        Image sizeImg = img.getImage();
        Image update = sizeImg.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        ImageIcon newImg = new ImageIcon(update);

        // 이미지 교체를 위해 backgroundImgLabel 업데이트 또는 추가
        if (backgroundImgLabel == null) {
            backgroundImgLabel = new JLabel(newImg);
            backgroundImgLabel.setBackground(Color.CYAN);
            backgroundImgLabel.setSize(size);
            yutimg_panel.add(backgroundImgLabel);
        } else {
            backgroundImgLabel.setIcon(newImg);
        }
    }
    public void addImage(ImageIcon img){
        Dimension size=new Dimension(200,100);
        Image sizeimg=img.getImage();
        Image update=sizeimg.getScaledInstance(size.width,size.height,Image.SCALE_SMOOTH);
        ImageIcon newimg=new ImageIcon(update);

        JLabel background_img=new JLabel(newimg);
        background_img.setBackground(Color.CYAN);
        background_img.setSize(size);

        recordYut.add(background_img);
    }




    //menubar
    public void creatMenu(Music Music1){
        JMenuBar mb=new JMenuBar();
        JMenu screenMenu = new JMenu("option");
        screenMenu.addSeparator();
        JMenuItem Sound=new JMenuItem("Sound");
        JMenuItem Exit=new JMenuItem("Exit");
        Sound.addActionListener(e -> Music1.music_control());
        Exit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "want to exit?", "exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                //yes->quit
                System.exit(0);
            }
        });
        screenMenu.add(Sound);
        screenMenu.add(Exit);
        mb.add(screenMenu);
        gameFrame.setJMenuBar(mb);


    }

}